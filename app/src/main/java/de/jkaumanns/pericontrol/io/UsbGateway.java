package de.jkaumanns.pericontrol.io;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Joerg on 01.09.2016.
 */
public class UsbGateway extends BroadcastReceiver implements IGateway {
    private final static byte delimiterStart1 = (byte) 0xCA;
    private final static byte delimiterStart2 = (byte) 0xFA;
    private final static byte delimiterEnd1 = (byte) 0xCA;
    private final static byte delimiterEnd2 = (byte) 0xFE;
    public final String ACTION_USB_PERMISSION = "de.jkaumanns.pericontrol.USB_PERMISSION";
    private boolean startBytesFound = false;
    private LinkedList byteList = new LinkedList();
    private int readPosition = 0;
    private byte[] lastMessage = null;
    private byte[] buffer = new byte[1024];
    private int messageId = 0;

    private WeakReference<Activity> weakActivity;
    private UsbManager usbManager;
    private UsbDevice usbDevice;
    private UsbSerialDevice serialPort;
    private UsbDeviceConnection connection;
    private boolean isConnected = false;
    private HashMap<Integer, LinkedList<byte[]>> messages;
    // Tricky: Sync a async message
    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() { //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] content) {
            for (int i = 0; i < content.length; i++) byteList.add(content[i]);
            extractMessage();
        }
    };

    public UsbGateway(Activity activity) {
        usbManager = (UsbManager) activity.getSystemService(Activity.USB_SERVICE);
        weakActivity = new WeakReference<>(activity);
    }

    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
            boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
            if (granted) {
                connection = usbManager.openDevice(usbDevice);
                serialPort = UsbSerialDevice.createUsbSerialDevice(usbDevice, connection);
                if (serialPort != null) {
                    if (serialPort.open()) { //Set Serial Connection Parameters.
                        serialPort.setBaudRate(9600);
                        serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                        serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                        serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                        serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                        serialPort.read(mCallback);
                        isConnected = true;
                    } else {
                        Log.d("SERIAL", "PORT NOT OPEN");
                        isConnected = false;
                    }
                } else {
                    Log.d("SERIAL", "PORT IS NULL");
                    isConnected = false;
                }
            } else {
                Log.d("SERIAL", "PERM NOT GRANTED");
                isConnected = false;
            }
        } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
            startUsb();
        } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
            endUsb();
        }
    }

    private void endUsb() {
        serialPort.close();
        isConnected = false;
    }

    private void startUsb() {
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                usbDevice = entry.getValue();
                int deviceVID = usbDevice.getVendorId();
                if (deviceVID == 0x2341) //Arduino Vendor ID
                {
                    PendingIntent pi = PendingIntent.getBroadcast(weakActivity.get(), 0, new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(usbDevice, pi);
                    keep = false;
                } else {
                    connection = null;
                    usbDevice = null;
                    isConnected = false;
                }

                if (!keep)
                    break;
            }
        }
    }

    private void extractMessage() {
        while (!byteList.isEmpty()) {
            byte b = (byte) byteList.poll();
            if (!startBytesFound) {
                if (b == delimiterStart1 && !byteList.isEmpty()) {
                    byte b2 = (byte) byteList.peek();
                    if (b2 == delimiterStart2) {
                        startBytesFound = true;
                        byteList.poll(); // remove the second delimiter from list
                    }
                } else if (byteList.isEmpty()) {
                    byteList.addFirst(b);
                    break;
                }
            } else {
                if (b == delimiterEnd1) {
                    if (!byteList.isEmpty()) {
                        byte b2 = (byte) byteList.peek();
                        if (b2 == delimiterEnd2) {
                            // here we go. Message ended. buffer contains message.
                            byteList.poll(); // first remove last delimiter from queue
                            // check if content equals match.
                            lastMessage = new byte[readPosition - 2];
                            System.arraycopy(buffer, 2, lastMessage, 0, readPosition - 2);
                            addMessageToQueue(lastMessage);
                            startBytesFound = false;
                            readPosition = 0;
                        } else {
                            buffer[readPosition++] = b;
                        }
                    } else {
                        byteList.addFirst(b);
                        break;
                    }
                } else {
                    buffer[readPosition++] = b;
                }
            }
        }
    }


    private void addMessageToQueue(byte[] lastMessage) {
        if (!messages.containsKey((int) lastMessage[0])) {
            messages.put((int) lastMessage[0], new LinkedList<byte[]>());
        }
        LinkedList<byte[]> queue = messages.get((int) lastMessage[0]);
        queue.add(lastMessage);
    }

    @Override
    public int writeMessage(IPeriProtocollMessage message) {
        int returnMessageId = Integer.MIN_VALUE;
        if (isConnected) {
            returnMessageId = messageId++;
            message.setMessageId((byte) returnMessageId);
            serialPort.write(message.getMessage());
        }
        return returnMessageId;
    }

    @Override
    public IPeriProtocollMessage retrieveMessage(int messageId) {
        return retrieveMessage(messageId, 2000);
    }

    @Override
    public IPeriProtocollMessage retrieveMessage(int messageId, int timeoutMills) {
        long startMillis = System.currentTimeMillis();
        IPeriProtocollMessage msg = null;
        while (System.currentTimeMillis() <= startMillis + timeoutMills) {
            if (messages.containsKey(messageId)) {
                LinkedList<byte[]> contents = messages.get(messageId);
                if (contents.size() > 0) {
                    msg = new PeriProtocollMessage();
                    msg.setRawResponse(contents.poll());
                    break;
                }
            }
        }
        return msg;
    }

    @Override
    public void clean(int messageId) {
        if (messages.containsKey(messageId)) {
            messages.remove(messageId);
        }
    }


    @Override
    public ArrayList<IPeriProtocollMessage> retrieveMessage(int messageId, int maxMessageCount, int timeoutMills) {
        int counter = maxMessageCount;
        long startMillis = System.currentTimeMillis();
        ArrayList<IPeriProtocollMessage> responses = new ArrayList<>();
        while (System.currentTimeMillis() <= startMillis + timeoutMills) {
            if (messages.containsKey(messageId)) {
                LinkedList<byte[]> contents = messages.get(messageId);
                if (contents.size() > 0) {
                    PeriProtocollMessage msg = new PeriProtocollMessage();
                    msg.setRawResponse(contents.poll());
                    responses.add(msg);
                    counter--;
                    if (counter == 0) break;
                }
            }
        }
        return responses;
    }
}

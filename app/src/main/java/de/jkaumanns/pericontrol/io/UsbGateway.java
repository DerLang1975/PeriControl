package de.jkaumanns.pericontrol.io;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import de.jkaumanns.pericontrol.PeriControlMain;

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
    private HashMap<Integer, LinkedList<byte[]>> messages = new HashMap<>();
    // Tricky: Sync a async message
    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() { //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] content) {
            Log.d("PeriC", "Begin onReceivedData: " + PeriControlMain.byteArrayToHex(content));
            for (int i = 0; i < content.length; i++) byteList.add(content[i]);
            extractMessage();
        }
    };

    public UsbGateway(Activity activity) {
        Log.d("PeriC", "Begin UsbGateway");
        usbManager = (UsbManager) activity.getSystemService(Activity.USB_SERVICE);
        weakActivity = new WeakReference<>(activity);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        activity.registerReceiver(this, filter);
        startUsb();
        Log.d("PeriC", "End UsbGateway");
    }

    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("PeriC", "Begin onReceive");
        if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
            Log.d("PeriC", "onReceive->ACTION_USB_PERMISSION");
            boolean granted = intent.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
            if (granted) {
                Log.d("PeriC", "onReceive->Granted");
                connection = usbManager.openDevice(usbDevice);
                serialPort = UsbSerialDevice.createUsbSerialDevice(usbDevice, connection);
                if (serialPort != null) {
                    Log.d("PeriC", "onReceive->got a serialPort ");
                    if (serialPort.open()) { //Set Serial Connection Parameters.
                        Log.d("PeriC", "onReceive->serialPort opened");
                        serialPort.setBaudRate(9600);
                        serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                        serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                        serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                        serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                        serialPort.read(mCallback);
                        isConnected = true;
                    } else {
                        Log.d("PeriC", "onReceive->PORT NOT OPEN");
                        isConnected = false;
                    }
                } else {
                    Log.d("PeriC", "onReceive->PORT IS NULL");
                    isConnected = false;
                }
            } else {
                Log.d("PeriC", "onReceive->PERM NOT GRANTED");
                isConnected = false;
            }
        } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
            startUsb();
        } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
            endUsb();
        }
    }

    private void endUsb() {
        Log.d("PeriC", "Begin endUsb");
        serialPort.close();
        isConnected = false;
    }

    private void startUsb() {
        Log.d("PeriC", "Begin startUsb");
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            Log.d("PeriC", "startUsb->Usb devices found");
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                Log.d("PeriC", "startUsb->investigate device");
                usbDevice = entry.getValue();
                int deviceVID = usbDevice.getVendorId();
                Log.d("PeriC", "startUsb->deviceId " + deviceVID);
                if (deviceVID == 0x403) // 403=1027=FTDI 0x2341) //Arduino Vendor ID
                {
                    Log.d("PeriC", "startUsb->product id: " + usbDevice.getProductId());
                    Log.d("PeriC", "startUsb->requestingPermission");
                    PendingIntent pi = PendingIntent.getBroadcast(weakActivity.get(), 0, new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(usbDevice, pi);
                    keep = false;
                } else {
                    Log.d("PeriC", "startUsb->Setting all to null");
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
                            lastMessage = new byte[readPosition];
                            System.arraycopy(buffer, 0, lastMessage, 0, readPosition);
                            Log.d("PeriC", "extracted response message: " + PeriControlMain.byteArrayToHex(lastMessage));
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
        int msgId = lastMessage[0];
        msgId <<= 8;
        msgId |= lastMessage[1];
        if (!messages.containsKey(msgId)) {
            messages.put(msgId, new LinkedList<byte[]>());
        }
        LinkedList<byte[]> queue = messages.get(msgId);
        queue.add(lastMessage);
    }

    @Override
    public IPeriProtocolMessage writeMessage(IPeriProtocolMessage message) {
        Log.d("PeriC", "Begin writeMessage");
        int returnMessageId = Integer.MIN_VALUE;
        if (isConnected) {
            Log.d("PeriC", "writeMessage->Connected");
            returnMessageId = messageId++;
            if (messageId > 65535) messageId = 0;
            Log.d("PeriC", "writeMessage->MessageId: " + returnMessageId);
            message.setMessageId(returnMessageId);
            byte b[] = message.getMessage();
            Log.d("PeriC", "writeMessage->msg: " + PeriControlMain.byteArrayToHex(b));
            serialPort.write(b);
        }
        return message;
    }

    @Override
    public IPeriProtocolMessage retrieveMessage(IPeriProtocolMessage message) {
        return retrieveMessage(message, 2000);
    }

    @Override
    public IPeriProtocolMessage retrieveMessage(IPeriProtocolMessage message, int timeoutMills) {
        Log.d("PeriC", "Begin UsbGateway.retrieveMessage: " + message.getMessageId() + " - " + timeoutMills);
        long startMillis = System.currentTimeMillis();
        IPeriProtocolMessage msg = null;
        while (System.currentTimeMillis() <= startMillis + timeoutMills) {
            if (messages.containsKey(message.getMessageId())) {
                LinkedList<byte[]> contents = messages.get(message.getMessageId());
                if (contents.size() > 0) {
                    Log.d("PeriC", "At least one content for message id");
                    if (isConnected()) {
                        message.setRawResponse(contents.poll());
                    } else {
                        message.setRawResponse(new byte[]{});
                    }
                    Log.d("PeriC", "setting first content as message and return it.");
                    msg = message;
                    break;
                }
            }
        }
        Log.d("PeriC", "End UsbGateway.retrieveMessage");
        return msg;
    }

    @Override
    public void clean(int messageId) {
        if (messages.containsKey(messageId)) {
            messages.remove(messageId);
        }
    }


    @Override
    public ArrayList<IPeriProtocolMessage> retrieveMessage(IPeriProtocolMessage message, int maxMessageCount, int timeoutMills) {
        int counter = maxMessageCount;
        long startMillis = System.currentTimeMillis();
        ArrayList<IPeriProtocolMessage> responses = new ArrayList<>();
        while (System.currentTimeMillis() <= startMillis + timeoutMills) {
            if (messages.containsKey(message.getMessageId())) {
                LinkedList<byte[]> contents = messages.get(message.getMessageId());
                if (contents.size() > 0) {
                    message.setRawResponse(contents.poll());
                    responses.add(message);
                    counter--;
                    if (counter == 0) break;
                }
            }
        }
        return responses;
    }
}

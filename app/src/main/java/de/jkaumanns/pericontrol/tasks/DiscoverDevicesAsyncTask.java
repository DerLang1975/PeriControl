package de.jkaumanns.pericontrol.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Button;

import java.lang.ref.WeakReference;

import de.jkaumanns.pericontrol.R;
import de.jkaumanns.pericontrol.io.GatewayFactory;
import de.jkaumanns.pericontrol.io.IGateway;
import de.jkaumanns.pericontrol.io.IPeriProtocolDeviceInformationMessage;
import de.jkaumanns.pericontrol.io.IPeriProtocolMessage;
import de.jkaumanns.pericontrol.io.PeriProtocolMessageFactory;
import de.jkaumanns.pericontrol.model.Device;
import de.jkaumanns.pericontrol.view.adapter.DeviceArrayAdapter;

/**
 * Created by Joerg on 01.09.2016.
 */
public class DiscoverDevicesAsyncTask extends AsyncTask<Integer, Object, Void> {

    private final DeviceArrayAdapter deviceAdapter;
    private WeakReference<Activity> weakActivity;

    public DiscoverDevicesAsyncTask(IGateway gateway, DeviceArrayAdapter deviceAdapter, Activity activity) {
        this.deviceAdapter = deviceAdapter;
        weakActivity = new WeakReference<Activity>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Activity activity = weakActivity.get();
        if (activity != null) {
            final Button btn = (Button) activity.findViewById(R.id.btnGetDevices);
            final Button btn2 = (Button) activity.findViewById(R.id.btnGetDevice);
            synchronized (btn) {
                btn.setEnabled(false);
                btn.setText("Discovering");
                btn.invalidate();
            }
            synchronized (btn2) {
                btn2.setEnabled(false);
                btn2.setText("Discovering");
                btn2.invalidate();
            }
        }
        deviceAdapter.clear();
        deviceAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Activity activity = weakActivity.get();
        if (activity != null) {
            Button btn = (Button) activity.findViewById(R.id.btnGetDevices);
            final Button btn2 = (Button) activity.findViewById(R.id.btnGetDevice);
            synchronized (btn) {
                btn.setEnabled(true);
                btn.setText("Discover");
                btn.invalidate();
            }
            synchronized (btn2) {
                btn2.setEnabled(true);
                btn2.setText("Discover");
                btn2.invalidate();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        Activity activity = weakActivity.get();
        if (activity != null) {
            synchronized (deviceAdapter) {
                if (values.length > 1 && values[1] != null) {
                    deviceAdapter.add((Device) values[1]);
                    deviceAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected Void doInBackground(Integer... params) {
        if (params.length > 0) {
            searchDefinedDevices(params);
        } else {
            searchAllAnsweringDevices();
        }

//            MessageHandler handler = MessageHandler.newInstance();
//            for(int i=2; i<range; i++) {
//                Device device = null;
//                messageGetMode[0] = (byte)i;
//                int messageId = bluetooth.sendMessage(messageGetMode);
//                Message msg = handler.waitForContent(messageId, i);
//                if(msg != null && msg.startsWith(new byte[]{COMMAND_GET_MODE})) {
//                    device = new Device(msg.getClientId(), bluetooth);
//                    device.setMode(msg.getContent()[1]);
//                    deviceIdMessage[0] = (byte)i;
//                    deviceChannelCountMessage[0] = (byte)i;
//                    deviceNameMessage[0] = (byte)i;
//                    messageId = bluetooth.sendMessage(deviceIdMessage);
//                    msg = handler.waitForContent(messageId, i);
//                    if(msg != null && msg.startsWith(new byte[]{COMMAND_GET_DEVICE_ID})) {
//                        Log.d("DiscoverDevices", "DeviceId received " + msg.getContent()[1]);
//                        messageId = bluetooth.sendMessage(deviceChannelCountMessage);
//                        msg = handler.waitForContent(messageId, i);
//                        if(msg != null && msg.startsWith(new byte[]{COMMAND_GET_CHANNEL_COUNT})) {
//                            device.setDeviceChannels(msg.getContent()[1]);
//                            Log.d("DiscoverDevices", "ChannelCount received " + msg.getContent()[1]);
//                        }
//                        messageId = bluetooth.sendMessage(deviceNameMessage);
//                        msg = handler.waitForContent(messageId, i);
//                        if(msg != null && msg.startsWith(new byte[]{COMMAND_GET_DEVICE_NAME})) {
//                            String s = new String(msg.getContent(), 1, msg.getContent().length - 1);
//                            device.setDeviceName(s);
//                            Log.d("DiscoverDevices", "DeviceName received " + s);
//                        }
//                    }
//                }
//                publishProgress((byte)i, device);
//            }
        return null;
    }

    private void searchDefinedDevices(Integer[] params) {
        IGateway gateway = GatewayFactory.getGatewayInstance(weakActivity.get());
        for (int i = 0; i < params.length; i++) {
            IPeriProtocolMessage sendMessage = PeriProtocolMessageFactory.createGetDeviceInformationMessage(params[i].byteValue());
            IPeriProtocolMessage message = gateway.writeMessage(sendMessage);
            IPeriProtocolDeviceInformationMessage msg = (IPeriProtocolDeviceInformationMessage) gateway.retrieveMessage(message);
            if (msg != null) {
                Device device = new Device(weakActivity.get());
                device.setDeviceUid(msg.getUid());
                device.setName(msg.getName());
                device.setDeviceId(msg.getDeviceId());
                device.setRssi(msg.getRssi());
                device.setDevicePortCount(msg.getPortCount());
                publishProgress(device.getDeviceId(), device);
            }
            gateway.clean(message.getMessageId());
        }
    }

    private void searchAllAnsweringDevices() {
        IGateway gateway = GatewayFactory.getGatewayInstance(weakActivity.get());
        IPeriProtocolMessage sendMessage = PeriProtocolMessageFactory.createDiscoverDevicesMessage();
        IPeriProtocolMessage message = gateway.writeMessage(sendMessage);
        long startMillis = System.currentTimeMillis();
        while (System.currentTimeMillis() <= startMillis + 5000) {
            IPeriProtocolDeviceInformationMessage msg = (IPeriProtocolDeviceInformationMessage) gateway.retrieveMessage(message);
            Device device = new Device(weakActivity.get());
            device.setDeviceUid(msg.getUid());
            device.setName(msg.getName());
            device.setDeviceId(msg.getDeviceId());
            device.setRssi(msg.getRssi());
            device.setDevicePortCount(msg.getPortCount());
            publishProgress(device.getDeviceId(), device);
        }
        gateway.clean(message.getMessageId());
    }
}

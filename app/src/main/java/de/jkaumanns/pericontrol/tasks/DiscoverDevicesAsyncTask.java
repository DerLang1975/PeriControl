package de.jkaumanns.pericontrol.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Button;

import java.lang.ref.WeakReference;

import de.jkaumanns.pericontrol.R;
import de.jkaumanns.pericontrol.io.GatewayFactory;
import de.jkaumanns.pericontrol.io.IGateway;
import de.jkaumanns.pericontrol.io.IPeriProtocollMessage;
import de.jkaumanns.pericontrol.io.PeriProtocollMessageFactory;
import de.jkaumanns.pericontrol.model.Device;
import de.jkaumanns.pericontrol.view.adapter.DeviceArrayAdapter;

/**
 * Created by Joerg on 01.09.2016.
 */
public class DiscoverDevicesAsyncTask extends AsyncTask<Void, Object, Void> {

    //        private Bluetooth bluetooth;
    private final DeviceArrayAdapter deviceAdapter;
    private WeakReference<Activity> weakActivity;

//        private final byte COMMAND_GET_MODE                     = (byte)0x01;
//        private byte[] messageGetMode                           = {(byte)0x00, COMMAND_GET_MODE};
//
//        private final byte COMMAND_GET_DEVICE_ID                = (byte)0x03;
//        private final byte COMMAND_GET_CHANNEL_COUNT			= (byte)0x07;
//        private final byte COMMAND_GET_DEVICE_NAME				= (byte)0x09;
//
//        private byte[] deviceIdMessage = {0x00, COMMAND_GET_DEVICE_ID};
//        private byte[] deviceNameMessage = {0x00, COMMAND_GET_DEVICE_NAME};
//        private byte[] deviceChannelCountMessage = {0x00, COMMAND_GET_CHANNEL_COUNT};
//        private int range;

    public DiscoverDevicesAsyncTask(IGateway gateway, DeviceArrayAdapter deviceAdapter, Activity activity) {
//            this.bluetooth = bluetooth;
        this.deviceAdapter = deviceAdapter;
        weakActivity = new WeakReference<Activity>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Activity activity = weakActivity.get();
        if (activity != null) {
            // TODO
//                final TextView txt = (TextView)activity.findViewById(R.id.txtMode);
//                synchronized (txt) {
//                    txt.setText("Discovery startet: 0%");
//                    txt.invalidate();
//                }
            final Button btn = (Button) activity.findViewById(R.id.btnGetDevices);
            synchronized (btn) {
                btn.setEnabled(false);
                btn.setText("Discovering");
                btn.invalidate();
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
            // TODO
//                TextView txt = (TextView)activity.findViewById(R.id.txtMode);
//                synchronized (txt) {
//                    txt.setText("Discovery done.");
//                    txt.invalidate();
//                }
            Button btn = (Button) activity.findViewById(R.id.btnGetDevices);
            synchronized (btn) {
                btn.setEnabled(true);
                btn.setText(R.string.btnDiscoverLabel);
                btn.invalidate();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        Activity activity = weakActivity.get();
        if (activity != null) {
            int percent;
            // TODO
//                if(range == 254) percent = (int)(((100.0)/(254-2))*((int)(((byte)values[0])&0xFF)));
//                else percent = (int)(((100.0)/(range-1))*((int)(((byte)values[0])&0xFF)));
//                TextView txt = (TextView)activity.findViewById(R.id.txtMode);
//                synchronized (txt) {
//                    txt.setText("Discovery ongoing: " + percent + "%");
//                    txt.invalidate();
//                }
            synchronized (deviceAdapter) {
                if (values.length > 1 && values[1] != null) {
                    deviceAdapter.add((Device) values[1]);
                    deviceAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        IGateway gateway = GatewayFactory.getGatewayInstance();
        IPeriProtocollMessage sendMessage = PeriProtocollMessageFactory.createDiscoverDevicesMessage();
        int messageId = gateway.writeMessage(sendMessage);
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
}

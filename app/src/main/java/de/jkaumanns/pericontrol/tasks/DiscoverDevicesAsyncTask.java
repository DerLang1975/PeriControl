package de.jkaumanns.pericontrol.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
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
        Log.d("PeriC", "Begin searchAllAnsweringDevices");
        IGateway gateway = GatewayFactory.getGatewayInstance(weakActivity.get());
        IPeriProtocolMessage sendMessage = PeriProtocolMessageFactory.createDiscoverDevicesMessage();
        Log.d("PeriC", "searchAllAnsweringDevices->sendMessage");
        IPeriProtocolMessage message = gateway.writeMessage(sendMessage);
        long startMillis = System.currentTimeMillis();
        while (System.currentTimeMillis() <= startMillis + 5000) {
            Log.d("PeriC", "searchAllAnsweringDevices->start retrieveMessage");
            IPeriProtocolDeviceInformationMessage msg = (IPeriProtocolDeviceInformationMessage) gateway.retrieveMessage(message);
            if (msg != null) {
                Log.d("PeriC", "searchAllAnsweringDevices->Found a device");
                Device device = new Device(weakActivity.get());
                device.setDeviceUid(msg.getUid());
                device.setName(msg.getName());
                device.setDeviceId(msg.getDeviceId());
                device.setRssi(msg.getRssi());
                device.setDevicePortCount(msg.getPortCount());
                publishProgress(device.getDeviceId(), device);
            }
        }
        Log.d("PeriC", "searchAllAnsweringDevices->Finish Discover device");
        gateway.clean(message.getMessageId());
    }
}

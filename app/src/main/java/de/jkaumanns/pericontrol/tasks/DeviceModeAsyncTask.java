package de.jkaumanns.pericontrol.tasks;

import android.os.AsyncTask;
import android.util.Log;

import de.jkaumanns.pericontrol.io.IGateway;
import de.jkaumanns.pericontrol.io.IPeriProtocolChangeModeMessage;
import de.jkaumanns.pericontrol.io.IPeriProtocolMessage;
import de.jkaumanns.pericontrol.io.PeriProtocolMessageFactory;
import de.jkaumanns.pericontrol.model.Device;
import de.jkaumanns.pericontrol.view.adapter.DeviceArrayAdapter;

/**
 * Created by Joerg on 15.09.2016.
 */
public class DeviceModeAsyncTask extends AsyncTask<Byte, IPeriProtocolChangeModeMessage, Object> {

    private DeviceArrayAdapter deviceAdapter;
    private IGateway gateway;

    public DeviceModeAsyncTask(DeviceArrayAdapter deviceAdapter, IGateway gateway) {
        this.deviceAdapter = deviceAdapter;
        this.gateway = gateway;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onProgressUpdate(IPeriProtocolChangeModeMessage... values) {
        Log.d("PeriC", "Begin DeviceModeAsyncTask.onProgressUpdate");
        if (values != null && values[0].getOldMode() != values[0].getNewMode()) {
            Log.d("PeriC", "updated mode as old != new");
            deviceAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Object doInBackground(Byte... params) {
        Log.d("PeriC", "Begin DeviceModeAsyncTask.doInBackground: " + params[0].toString());
        if (params.length == 0) return null;
        if (params.length == 2) changeModeSingleDevice(params[1], params[0]);
        else changeModeAllDevices(params[0]);
        Log.d("PeriC", "End DeviceModeAsyncTask.doInBackground: " + params[0].toString());
        return null;
    }

    private void changeModeAllDevices(byte newMode) {
        for (int i = 0; i < deviceAdapter.getCount(); i++) {
            changeModeSingleDevice((byte) i, newMode);
        }
    }

    private void changeModeSingleDevice(byte deviceId, byte newMode) {
        Device device = deviceAdapter.getItem(deviceId).device;
        IPeriProtocolMessage message = PeriProtocolMessageFactory.createChangeModeMessage(device.getDeviceId(), newMode);
        message = gateway.writeMessage(message);
        IPeriProtocolMessage response = gateway.retrieveMessage(message);
        if (response != null) {
            Log.d("PeriC", "got a return value from gateway");
            if (response instanceof IPeriProtocolChangeModeMessage) {
                IPeriProtocolChangeModeMessage retValue = (IPeriProtocolChangeModeMessage) response;
                Log.d("PeriC", "setting new mode in device");
                device.switchMode(retValue.getNewMode());
                Log.d("PeriC", "publish progress");
                publishProgress(retValue);
            }
        } else {
            Log.d("PeriC", "publish empty progress");
            publishProgress(null);
        }
    }
}

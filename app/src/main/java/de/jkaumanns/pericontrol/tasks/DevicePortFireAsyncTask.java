package de.jkaumanns.pericontrol.tasks;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import de.jkaumanns.pericontrol.io.IGateway;
import de.jkaumanns.pericontrol.io.IPeriProtocolManualFireMessage;
import de.jkaumanns.pericontrol.io.IPeriProtocolMessage;
import de.jkaumanns.pericontrol.io.PeriProtocolMessageFactory;
import de.jkaumanns.pericontrol.model.Device;

/**
 * Created by Joerg on 06.09.2016.
 */
public class DevicePortFireAsyncTask extends AsyncTask<Integer, Boolean, Boolean> {

    private Device device;
    private IGateway gateway;
    private View view;

    public DevicePortFireAsyncTask(IGateway gateway, Device device, View v) {
        this.device = device;
        this.gateway = gateway;
        this.view = v;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        if (values[0]) {
            view.setBackgroundColor(Color.GREEN);
        } else {
            view.setBackgroundColor(Color.YELLOW);
        }
        view.invalidate();
    }

    @Override
    protected void onCancelled(Boolean o) {
        super.onCancelled(o);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        Log.d("PeriC", "Begin DevicePortFireAsyncTask.doInBackground");
        if (params.length == 0) return null;
        IPeriProtocolMessage message = PeriProtocolMessageFactory.createFirePort(device.getDeviceId(), params[0].byteValue());
        message = gateway.writeMessage(message);
        IPeriProtocolMessage response = gateway.retrieveMessage(message);
        boolean retValue = false;
        if (response != null) {
            if (response instanceof IPeriProtocolManualFireMessage) {
                retValue = ((IPeriProtocolManualFireMessage) response).isPortFired();
                publishProgress(retValue);
            }
        }
        Log.d("PeriC", "End DevicePortFireAsyncTask.doInBackground");
        return retValue;
    }
}

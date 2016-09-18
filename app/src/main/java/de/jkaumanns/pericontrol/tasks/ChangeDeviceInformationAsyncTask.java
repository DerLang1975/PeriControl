package de.jkaumanns.pericontrol.tasks;

import android.os.AsyncTask;

import de.jkaumanns.pericontrol.io.GatewayFactory;
import de.jkaumanns.pericontrol.io.IGateway;
import de.jkaumanns.pericontrol.io.IPeriProtocolMessage;
import de.jkaumanns.pericontrol.io.PeriProtocolMessageFactory;
import de.jkaumanns.pericontrol.model.Device;

/**
 * Created by Joerg on 18.09.2016.
 */
public class ChangeDeviceInformationAsyncTask extends AsyncTask<Device, Boolean, Object> {

    public ChangeDeviceInformationAsyncTask() {
        super();
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
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);
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
    protected Object doInBackground(Device... params) {
        if (params.length > 0) {
            // assume that gatewayfactory was already setup.
            IGateway gateway = GatewayFactory.getGatewayInstance(null);
            IPeriProtocolMessage msg = PeriProtocolMessageFactory.createSetDeviceInformationMessage(params[0]);
            msg = gateway.writeMessage(msg);
        }
        return null;
    }
}

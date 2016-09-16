package de.jkaumanns.pericontrol.tasks;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import de.jkaumanns.pericontrol.R;
import de.jkaumanns.pericontrol.io.IGateway;
import de.jkaumanns.pericontrol.io.IPeriProtocolMeasurePortMessage;
import de.jkaumanns.pericontrol.io.IPeriProtocolMessage;
import de.jkaumanns.pericontrol.io.PeriProtocolMessageFactory;
import de.jkaumanns.pericontrol.model.Device;
import de.jkaumanns.pericontrol.view.component.DevicePortButtonView;

/**
 * Created by Joerg on 15.09.2016.
 */
public class DeviceMeasurePortAsyncTask extends AsyncTask<Integer, IPeriProtocolMeasurePortMessage, Boolean> {

    private Device device;
    private IGateway gateway;
    private View view;

    public DeviceMeasurePortAsyncTask(IGateway gateway, Device device, View v) {
        this.device = device;
        this.gateway = gateway;
        this.view = v;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    @Override
    protected void onProgressUpdate(IPeriProtocolMeasurePortMessage... values) {
        if (values.length > 0 && values[0] != null) {
            view.setEnabled(true);
            if (view instanceof LinearLayout) {
                DevicePortButtonView v = (DevicePortButtonView) (view).getParent();
                v.setChannelPower(values[0].getPower());
                v.setChannelResistance(values[0].getResistance());
                if (values[0].getPowerValue() >= 1.5f) {
                    v.setBackgroundColor(Color.GREEN);
                } else if (values[0].getPowerValue() >= 1.0f) {
                    v.setBackgroundColor(Color.YELLOW);
                } else {
                    v.setBackgroundColor(Color.RED);
                }
            }
        } else {
            DevicePortButtonView v = (DevicePortButtonView) (view).getParent();
            v.setChannelPower("");
            v.setChannelResistance("");
            v.setBackgroundResource(R.color.background_color);
        }
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        super.onCancelled(aBoolean);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        Log.d("PeriC", "Begin DeviceMeasurePortAsyncTask.doInBackground");
        if (params.length == 0) return null;
        IPeriProtocolMessage message = PeriProtocolMessageFactory.createMeasurePort(device.getDeviceId(), params[0].byteValue());
        message = gateway.writeMessage(message);
        IPeriProtocolMessage response = gateway.retrieveMessage(message);
        if (response != null) {
            if (response instanceof IPeriProtocolMeasurePortMessage) {
                IPeriProtocolMeasurePortMessage retValue = (IPeriProtocolMeasurePortMessage) response;
                publishProgress(retValue);
            }
        } else {
            publishProgress(null);
        }
        Log.d("PeriC", "End DeviceMeasurePortAsyncTask.doInBackground");
        return true;

    }
}

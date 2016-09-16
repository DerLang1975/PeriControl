package de.jkaumanns.pericontrol.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import de.jkaumanns.pericontrol.R;
import de.jkaumanns.pericontrol.model.Device;
import de.jkaumanns.pericontrol.view.fragments.DeviceAdminDetailFragment;
import de.jkaumanns.pericontrol.view.fragments.DevicePortsFragment;

/**
 * Created by Joerg on 31.08.2016.
 */
public class DeviceArrayAdapter extends ArrayAdapter<DeviceArrayAdapter.DeviceArrayAdapterHolder> {
    private final Context context;
    private final ArrayList<DeviceArrayAdapterHolder> values;

    public DeviceArrayAdapter(Context context, ArrayList<DeviceArrayAdapterHolder> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("PeriC", "Begin DeviceArrayAdapter.geView: " + position + " - " + (convertView == null));
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;
        if (rowView == null) rowView = inflater.inflate(R.layout.row_layout_device, parent, false);
        TextView txtDeviceId = (TextView) rowView.findViewById(R.id.txtDeviceId);
        TextView txtDeviceName = (TextView) rowView.findViewById(R.id.txtDeviceName);
        TextView txtDeviceChannels = (TextView) rowView.findViewById(R.id.txtDeviceChannelCount);
        TextView txtDeviceUid = (TextView) rowView.findViewById(R.id.txtDeviceUid);
        TextView txtRssiValue = (TextView) rowView.findViewById(R.id.txtRssiValue);
        ProgressBar bar = (ProgressBar) rowView.findViewById(R.id.prgRssi);
        // change the icon for Windows and iPhone
        String s = Byte.toString(values.get(position).device.getDeviceId());
        txtDeviceId.setText(s);
        s = values.get(position).device.getDeviceName();
        txtDeviceName.setText(s);
        s = Byte.toString(values.get(position).device.getDevicePortCount());
        txtDeviceChannels.setText(s);
        txtDeviceUid.setText(values.get(position).device.getDeviceUid());
        txtRssiValue.setText(Integer.toString(values.get(position).device.getRssi()));
        bar.setProgress(values.get(position).device.getRssi());
        Log.d("PeriC", "info set, setting mode");
        switch (values.get(position).device.getMode()) {
            case Device.MODE_IDLE:
                rowView.setBackgroundColor(Color.TRANSPARENT);
                break;
            case Device.MODE_TEST:
                rowView.setBackgroundColor(Color.YELLOW);
                break;
            case Device.MODE_FIRE:
                rowView.setBackgroundColor(Color.RED);
                break;
            default:
                rowView.setBackgroundColor(Color.TRANSPARENT);
                break;
        }
        Log.d("PeriC", "End DeviceArrayAdapter.geView");
        return rowView;
    }

    public static class DeviceArrayAdapterHolder {
        public Device device;
        public DevicePortsFragment devicePortsFragment;
        public DeviceAdminDetailFragment deviceAdminDetailFragment;
    }
}

package de.jkaumanns.pericontrol.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.jkaumanns.pericontrol.R;
import de.jkaumanns.pericontrol.model.Device;

/**
 * Created by Joerg on 31.08.2016.
 */
public class DeviceArrayAdapter extends ArrayAdapter<Device> {
    private final Context context;
    private final ArrayList<Device> values;

    public DeviceArrayAdapter(Context context, ArrayList<Device> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout_device, parent, false);
        TextView txtDeviceId = (TextView) rowView.findViewById(R.id.txtDeviceId);
        TextView txtDeviceName = (TextView) rowView.findViewById(R.id.txtDeviceName);
        TextView txtDeviceChannels = (TextView) rowView.findViewById(R.id.txtDeviceChannelCount);
        // change the icon for Windows and iPhone
        String s = Byte.toString(values.get(position).getDeviceId());
        txtDeviceId.setText(s);
        s = values.get(position).getDeviceName();
        txtDeviceName.setText(s);
        s = Byte.toString(values.get(position).getDevicePortCount());
        txtDeviceChannels.setText(s);
        switch (values.get(position).getMode()) {
            case (byte) 0x0A:
                rowView.setBackgroundColor(Color.WHITE);
                break;
            case (byte) 0x5A:
                rowView.setBackgroundColor(Color.YELLOW);
                break;
            case (byte) 0x8A:
                rowView.setBackgroundColor(Color.RED);
                break;
            default:
                rowView.setBackgroundColor(Color.TRANSPARENT);
                break;
        }
        return rowView;
    }
}

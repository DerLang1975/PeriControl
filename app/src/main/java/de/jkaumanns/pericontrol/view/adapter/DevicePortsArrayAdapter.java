package de.jkaumanns.pericontrol.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import de.jkaumanns.pericontrol.R;
import de.jkaumanns.pericontrol.model.Device;
import de.jkaumanns.pericontrol.model.DevicePort;
import de.jkaumanns.pericontrol.model.DevicePortGroup;
import de.jkaumanns.pericontrol.view.component.DevicePortButtonView;

/**
 * Created by Joerg on 01.09.2016.
 */
public class DevicePortsArrayAdapter extends ArrayAdapter<DevicePortGroup> {
    private final Context context;
    private final ArrayList<DevicePortGroup> values;
    private final Device device;
    private Button.OnClickListener btnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DevicePort channel = (DevicePort) v.getTag(R.integer.tagIdDevicePort);
            device.channelClicked(channel, v);
        }
    };

    public DevicePortsArrayAdapter(Context context, Device device) {
        super(context, -1, device.getPortGroups());
        this.context = context;
        this.device = device;
        this.values = device.getPortGroups();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout_device_port_group, parent, false);
        DevicePortGroup group = values.get(position);
        for (int i = 0; i < 5; i++)
            initChannelButton(i, group, rowView);
        return rowView;
    }

    private void initChannelButton(int i, DevicePortGroup group, View rowView) {
        try {
            int id = R.id.class.getField("btnChannelRowX" + (i + 1)).getInt(0);
            DevicePortButtonView btnv = (DevicePortButtonView) rowView.findViewById(id);
            DevicePort channel = group.getPortGroup().get(i);
            btnv.setChannelNo(channel.getPortId());
            if (channel.getPower() != Float.MIN_VALUE)
                btnv.setChannelPower(Float.toString(channel.getPower()));
            else btnv.setChannelPower("");
            if (channel.getResistance() != Integer.MIN_VALUE)
                btnv.setChannelResistance(Integer.toString(channel.getResistance()));
            else btnv.setChannelResistance("");
            btnv.setVisibility(channel.isAvailable() ? View.VISIBLE : View.INVISIBLE);
            LinearLayout ll = (LinearLayout) btnv.findViewById(R.id.deviceChannelButton);
            ll.setOnClickListener(btnOnClickListener);
            ll.setTag(R.integer.tagIdDevicePort, channel);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}

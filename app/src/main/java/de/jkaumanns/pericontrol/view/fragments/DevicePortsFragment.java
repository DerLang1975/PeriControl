package de.jkaumanns.pericontrol.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import de.jkaumanns.pericontrol.R;
import de.jkaumanns.pericontrol.io.GatewayFactory;
import de.jkaumanns.pericontrol.io.IGateway;
import de.jkaumanns.pericontrol.model.Device;
import de.jkaumanns.pericontrol.tasks.DeviceModeAsyncTask;
import de.jkaumanns.pericontrol.view.adapter.DeviceArrayAdapter;
import de.jkaumanns.pericontrol.view.adapter.DevicePortsArrayAdapter;

/**
 * Created by Joerg on 01.09.2016.
 */
public class DevicePortsFragment extends Fragment {
    private Device device;
    private ListView lstDeviceChannels;
    private DevicePortsArrayAdapter devicePortsAdapter;
    private DeviceArrayAdapter deviceAdapter;
    private View view;

    private View.OnClickListener onRadioButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DeviceModeAsyncTask dmat;
            IGateway gateway = GatewayFactory.getGatewayInstance(null);
            switch (v.getId()) {
                case R.id.rbtnIdle:
                    dmat = new DeviceModeAsyncTask(deviceAdapter, gateway);
                    dmat.execute(Device.MODE_IDLE, device.getDeviceId());
                    break;
                case R.id.rbtnTest:
                    dmat = new DeviceModeAsyncTask(deviceAdapter, gateway);
                    dmat.execute(Device.MODE_TEST, device.getDeviceId());
                    break;
                case R.id.rbtnArmed:
                    dmat = new DeviceModeAsyncTask(deviceAdapter, gateway);
                    dmat.execute(Device.MODE_FIRE, device.getDeviceId());
                    break;
            }
        }
    };

    public static DevicePortsFragment newInstance(Device device, DeviceArrayAdapter deviceAdapter) {
        DevicePortsFragment fragment = new DevicePortsFragment();
        Bundle args = new Bundle();
        args.putSerializable("device", device);
        fragment.setAdapter(deviceAdapter);
        fragment.setArguments(args);
        return fragment;
    }

    public void setAdapter(DeviceArrayAdapter deviceAdapter) {
        this.deviceAdapter = deviceAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device = (Device) getArguments().getSerializable("device");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view
        view = inflater.inflate(R.layout.fragment_device_ports, container, false);

        TextView txt = (TextView) view.findViewById(R.id.deviceName);
        txt.setText(device.getDeviceName());

        RadioButton rbtn = (RadioButton) view.findViewById(R.id.rbtnTest);
        rbtn.setOnClickListener(onRadioButtonClick);
        rbtn = (RadioButton) view.findViewById(R.id.rbtnIdle);
        rbtn.setOnClickListener(onRadioButtonClick);
        rbtn = (RadioButton) view.findViewById(R.id.rbtnArmed);
        rbtn.setOnClickListener(onRadioButtonClick);

        devicePortsAdapter = new DevicePortsArrayAdapter(this.getContext(), device);
        lstDeviceChannels = (ListView) view.findViewById(R.id.lstViewDeviceChannels);
        lstDeviceChannels.setAdapter(devicePortsAdapter);

        return view;
    }
}

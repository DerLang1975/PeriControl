package de.jkaumanns.pericontrol.view.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.jkaumanns.pericontrol.R;
import de.jkaumanns.pericontrol.model.Device;
import de.jkaumanns.pericontrol.view.adapter.DeviceArrayAdapter;
import de.jkaumanns.pericontrol.view.validation.DeviceIdValidator;
import de.jkaumanns.pericontrol.view.validation.DeviceNameValidator;
import de.jkaumanns.pericontrol.view.validation.DevicePortTimeoutValidator;

/**
 * Created by Joerg on 01.09.2016.
 */
public class DeviceAdminDetailFragment extends Fragment {

    private Device device;
    private DeviceArrayAdapter deviceAdapter;
    private View view;
    private View.OnClickListener onClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public static DeviceAdminDetailFragment newInstance(Device device, DeviceArrayAdapter deviceAdapter) {
        DeviceAdminDetailFragment fragment = new DeviceAdminDetailFragment();
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
        view = inflater.inflate(R.layout.fragment_device_admin_details, container, false);

        TextInputEditText txtDeviceUid = (TextInputEditText) view.findViewById(R.id.txtDeviceUid);
        TextInputEditText txtDevicePortCount = (TextInputEditText) view.findViewById(R.id.txtDevicePortCount);
        txtDeviceUid.setText(device.getDeviceUid());
        txtDevicePortCount.setText(String.valueOf(device.getDevicePortCount()));

        TextInputEditText txtDeviceId = (TextInputEditText) view.findViewById(R.id.txtDeviceId);
        txtDeviceId.setText(String.valueOf(device.getDeviceId()));
        txtDeviceId.addTextChangedListener(new DeviceIdValidator(txtDeviceId));
        TextInputEditText txtDeviceName = (TextInputEditText) view.findViewById(R.id.txtDeviceName);
        txtDeviceName.setText(device.getDeviceName());
        txtDeviceName.addTextChangedListener(new DeviceNameValidator(txtDeviceName));
        TextInputEditText txtDevicePortTimeout = (TextInputEditText) view.findViewById(R.id.txtDevicePortTimeout);
        txtDevicePortTimeout.setText(String.valueOf(device.getPortTimeout()));
        txtDevicePortTimeout.addTextChangedListener(new DevicePortTimeoutValidator(txtDevicePortTimeout));

        Button btnTtransmit = (Button) view.findViewById(R.id.btnTransmit);
        btnTtransmit.setOnClickListener(onClicked);
        return view;
    }
}

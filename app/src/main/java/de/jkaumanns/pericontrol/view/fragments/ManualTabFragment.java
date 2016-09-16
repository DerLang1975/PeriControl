package de.jkaumanns.pericontrol.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import de.jkaumanns.pericontrol.R;
import de.jkaumanns.pericontrol.io.GatewayFactory;
import de.jkaumanns.pericontrol.io.IGateway;
import de.jkaumanns.pericontrol.model.Device;
import de.jkaumanns.pericontrol.tasks.DeviceModeAsyncTask;
import de.jkaumanns.pericontrol.tasks.DiscoverDevicesAsyncTask;
import de.jkaumanns.pericontrol.view.adapter.DeviceArrayAdapter;

/**
 * Created by Joerg on 31.08.2016.
 */
public class ManualTabFragment extends Fragment {

    private ListView lstDevices;

    private DeviceArrayAdapter deviceAdapter;
    private DiscoverDevicesAsyncTask discoverDevices;
    private Button btnDiscoverDevices;
    private Button btnDiscoverDevice;

    private EditText numberPicker;
    private View.OnClickListener btnDiscoverDevicesClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IGateway gateway = GatewayFactory.getGatewayInstance(getActivity());
            discoverDevices = new DiscoverDevicesAsyncTask(gateway, deviceAdapter, getActivity());
            discoverDevices.execute();
        }
    };
    private View.OnClickListener btnDiscoverDeviceClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IGateway gateway = GatewayFactory.getGatewayInstance(getActivity());
            discoverDevices = new DiscoverDevicesAsyncTask(gateway, deviceAdapter, getActivity());
            int selectedItem = Integer.parseInt(numberPicker.getText().toString());
            discoverDevices.execute(selectedItem);
        }
    };
    private View.OnClickListener onRadioButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DeviceModeAsyncTask dmat;
            IGateway gateway = GatewayFactory.getGatewayInstance(null);
            switch (v.getId()) {
                case R.id.rbtnIdle:
                    dmat = new DeviceModeAsyncTask(deviceAdapter, gateway);
                    dmat.execute(Device.MODE_IDLE);
                    btnDiscoverDevice.setEnabled(true);
                    btnDiscoverDevices.setEnabled(true);
                    numberPicker.setEnabled(true);
                    break;
                case R.id.rbtnTest:
                    dmat = new DeviceModeAsyncTask(deviceAdapter, gateway);
                    dmat.execute(Device.MODE_TEST);
                    btnDiscoverDevice.setEnabled(false);
                    btnDiscoverDevices.setEnabled(false);
                    numberPicker.setEnabled(false);
                    break;
                case R.id.rbtnArmed:
                    dmat = new DeviceModeAsyncTask(deviceAdapter, gateway);
                    dmat.execute(Device.MODE_FIRE);
                    btnDiscoverDevice.setEnabled(false);
                    btnDiscoverDevices.setEnabled(false);
                    numberPicker.setEnabled(false);
                    break;
            }
        }
    };

    public ManualTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manual_tab, container, false);

        deviceAdapter = new DeviceArrayAdapter(this.getContext(), new ArrayList<DeviceArrayAdapter.DeviceArrayAdapterHolder>());
        lstDevices = (ListView) view.findViewById(R.id.lstDevices);
        lstDevices.setAdapter(deviceAdapter);
        lstDevices.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Device device = deviceAdapter.getItem(position).device;
                        DevicePortsFragment fragment = deviceAdapter.getItem(position).devicePortsFragment;
                        if (fragment == null) {
                            fragment = DevicePortsFragment.newInstance(device, deviceAdapter);
                            deviceAdapter.getItem(position).devicePortsFragment = fragment;
                        }
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        ft.replace(R.id.flFragmentDetail, fragment);
                        ft.commit();
                    }
                }
        );
        btnDiscoverDevices = (Button) (view.findViewById(R.id.btnGetDevices));
        btnDiscoverDevices.setOnClickListener(btnDiscoverDevicesClick);
        btnDiscoverDevice = (Button) (view.findViewById(R.id.btnGetDevice));
        btnDiscoverDevice.setOnClickListener(btnDiscoverDeviceClick);
        numberPicker = (EditText) view.findViewById(R.id.deviceIdSelector);
        (view.findViewById(R.id.rbtnIdle)).setOnClickListener(onRadioButtonClick);
        (view.findViewById(R.id.rbtnTest)).setOnClickListener(onRadioButtonClick);
        (view.findViewById(R.id.rbtnArmed)).setOnClickListener(onRadioButtonClick);
        return view;
    }
}

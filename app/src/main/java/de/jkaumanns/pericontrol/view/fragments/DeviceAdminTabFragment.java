package de.jkaumanns.pericontrol.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import de.jkaumanns.pericontrol.R;
import de.jkaumanns.pericontrol.io.GatewayFactory;
import de.jkaumanns.pericontrol.io.IGateway;
import de.jkaumanns.pericontrol.model.Device;
import de.jkaumanns.pericontrol.tasks.DiscoverDevicesAsyncTask;
import de.jkaumanns.pericontrol.view.adapter.DeviceArrayAdapter;

/**
 * Created by Joerg on 31.08.2016.
 */
public class DeviceAdminTabFragment extends Fragment {

    private ListView lstDevices;

    private DeviceArrayAdapter deviceAdapter;
    private DiscoverDevicesAsyncTask discoverDevices;
    private Button btnDiscoverDevices;

    private View.OnClickListener btnDiscoverDevicesClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IGateway gateway = GatewayFactory.getGatewayInstance(getActivity());
            discoverDevices = new DiscoverDevicesAsyncTask(gateway, deviceAdapter, getActivity());
            discoverDevices.execute();
        }
    };

    public DeviceAdminTabFragment() {
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
        View view = inflater.inflate(R.layout.fragment_device_admin_tab, container, false);

        deviceAdapter = new DeviceArrayAdapter(this.getContext(), new ArrayList<DeviceArrayAdapter.DeviceArrayAdapterHolder>());
        lstDevices = (ListView) view.findViewById(R.id.lstDevices);
        lstDevices.setAdapter(deviceAdapter);
        lstDevices.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("PeriC", "Begin lstDevices.setOnItemClickListener");
                        Device device = deviceAdapter.getItem(position).device;
                        DeviceAdminDetailFragment fragment = deviceAdapter.getItem(position).deviceAdminDetailFragment;
                        if (fragment == null) {
                            Log.d("PeriC", "Begin lstDevices.setOnItemClickListener-> fragment null");
                            fragment = DeviceAdminDetailFragment.newInstance(device, deviceAdapter);
                            deviceAdapter.getItem(position).deviceAdminDetailFragment = fragment;
                        }
                        Log.d("PeriC", "Begin lstDevices.setOnItemClickListener-> fragment null");
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        ft.replace(R.id.flFragmentDeviceAdminDetail, fragment);
                        ft.commit();
                    }
                }
        );
        btnDiscoverDevices = (Button) (view.findViewById(R.id.btnGetDevices));
        btnDiscoverDevices.setOnClickListener(btnDiscoverDevicesClick);
        return view;
    }
}

package de.jkaumanns.pericontrol.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import de.jkaumanns.pericontrol.R;
import de.jkaumanns.pericontrol.io.IGateway;
import de.jkaumanns.pericontrol.model.Device;
import de.jkaumanns.pericontrol.tasks.DiscoverDevicesAsyncTask;
import de.jkaumanns.pericontrol.view.adapter.DeviceArrayAdapter;

/**
 * Created by Joerg on 31.08.2016.
 */
public class ManualTabFragment extends Fragment {

    private IGateway gateway;
    private ListView lstDevices;
    private DeviceArrayAdapter deviceAdapter;
    private DiscoverDevicesAsyncTask discoverDevices;

    private View.OnClickListener btnDiscoverDevicesClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

//            if(bluetooth.isConnected()) {
//                discoverDevices = new DiscoverDevices(bluetooth, deviceAdapter, null);
//                discoverDevices.setRange(3);
//                discoverDevices.execute();
//            }
        }
    };

    public ManualTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
//            bluetooth = Bluetooth.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manual_tab, container, false);

        deviceAdapter = new DeviceArrayAdapter(this.getContext(), new ArrayList<Device>());
        lstDevices = (ListView) view.findViewById(R.id.lstDevices);
        lstDevices.setAdapter(deviceAdapter);
        lstDevices.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Device device = deviceAdapter.getItem(position);
                        DevicePortsFragment fragment = device.getFragment();
                        if (fragment == null) {
                            fragment = DevicePortsFragment.newInstance(device);
                            device.setFragment(fragment);
                        }
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        ft.replace(R.id.flFragmentDetail, fragment);
                        ft.commit();
                    }
                }
        );
        (view.findViewById(R.id.btnGetDevices)).setOnClickListener(btnDiscoverDevicesClick);
        return view;
    }
}

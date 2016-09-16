package de.jkaumanns.pericontrol;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import de.jkaumanns.pericontrol.io.GatewayFactory;
import de.jkaumanns.pericontrol.view.component.LockableViewPager;
import de.jkaumanns.pericontrol.view.fragments.DeviceAdminTabFragment;
import de.jkaumanns.pericontrol.view.fragments.ManualTabFragment;

public class PeriControlMain extends AppCompatActivity {


    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private LockableViewPager viewPager;

    private ManualTabFragment manualTab;
    private DeviceAdminTabFragment deviceAdminTab;
    //private CreateShowTabFragment showTab;

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peri_control_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Peri - Control");

        manualTab = new ManualTabFragment();
        deviceAdminTab = new DeviceAdminTabFragment();

        viewPager = (LockableViewPager) findViewById(R.id.viewpager);
        viewPager.setSwipeLocked(true);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        GatewayFactory.getGatewayInstance(this);
    }

    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(manualTab, "Manual Mode");

//        adapter.addFragment(new ShowTabFragment(), "Show Mode");
//        showTab = new CreateShowTabFragment();
//        adapter.addFragment(showTab, "Create Show");

        adapter.addFragment(deviceAdminTab, "Device Administration");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

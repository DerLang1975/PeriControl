package de.jkaumanns.pericontrol.model;

import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

import de.jkaumanns.pericontrol.view.fragments.DevicePortsFragment;

/**
 * Created by Joerg on 01.09.2016.
 */
public class Device implements Serializable {

    public static final byte MODE_IDLE = 0x0A;
    public static final byte MODE_TEST = 0x2A;
    public static final byte MODE_FIRE = 0x4A;

    private String uniqueIdentifier;
    private byte id;
    private byte portCount;
    private byte mode;
    private String name;
    private ArrayList<DevicePortGroup> portGroups;
    private DevicePortsFragment fragment;
    private String deviceUid;
    private int rssi;

    public byte getDeviceId() {
        return id;
    }

    public void setDeviceId(byte deviceId) {
        this.id = deviceId;
    }

    public byte getDevicePortCount() {
        return portCount;
    }

    public void setDevicePortCount(byte devicePortCount) {
        this.portCount = portCount;
        portGroups = new ArrayList<DevicePortGroup>();
        for (int i = 0; i < portCount; i += 5) {
            int channelFrom = i + 1;
            int maxPerRow = 5;
            int visible = (portCount - i) < maxPerRow ? (portCount - i) : maxPerRow;
            int channelTo = i + visible;
            portGroups.add(new DevicePortGroup(channelFrom, channelTo, maxPerRow));
        }
    }

    public String getDeviceName() {
        return name;
    }

    public byte getMode() {
        return mode;
    }

    public void switchMode(byte newMode) {
        this.mode = newMode;
    }

    public ArrayList<DevicePortGroup> getPortGroups() {
        return portGroups;
    }

    public void channelClicked(DevicePort port, View v) {
        if (mode == Device.MODE_TEST) {
            // TODO
//            DeviceChannelAsyncTask dc = new DeviceChannelAsyncTask(bluetooth, this, view);
//            dc.execute(DeviceChannelAsyncTask.CHANNEL_RESISTANCE, channel.getChannelId());
        } else if (mode == Device.MODE_FIRE) {
            // TODO
//            DeviceChannelAsyncTask dc = new DeviceChannelAsyncTask(bluetooth, this, view);
//            dc.execute(DeviceChannelAsyncTask.FIRE_CHANNEL, channel.getChannelId());
        }
    }

    public DevicePortsFragment getFragment() {
        return fragment;
    }

    public void setFragment(DevicePortsFragment fragment) {
        this.fragment = fragment;
    }

    public String getDeviceUid() {
        return deviceUid;
    }

    public void setDeviceUid(String deviceUid) {
        this.deviceUid = deviceUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}

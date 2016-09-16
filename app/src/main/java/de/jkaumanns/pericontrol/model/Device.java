package de.jkaumanns.pericontrol.model;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import de.jkaumanns.pericontrol.io.GatewayFactory;
import de.jkaumanns.pericontrol.io.IGateway;
import de.jkaumanns.pericontrol.tasks.DeviceFirePortAsyncTask;
import de.jkaumanns.pericontrol.tasks.DeviceMeasurePortAsyncTask;

/**
 * Created by Joerg on 01.09.2016.
 */
public class Device implements Serializable {
    public static final byte MODE_IDLE = 0x0A;
    public static final byte MODE_TEST = 0x2A;
    public static final byte MODE_FIRE = 0x4A;
    private static final long serialVersionUID = -6099312954099962806L;
    private WeakReference<Activity> weakActivity;

    private byte id;
    private byte portCount;
    private byte mode;
    private String name;
    private ArrayList<DevicePortGroup> portGroups;
    private String deviceUid;
    private int rssi;
    private int portTimeout;

    public Device(Activity activity) {
        weakActivity = new WeakReference<>(activity);
    }

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
        Log.d("PeriC", "Begin setDevicePortCount: " + devicePortCount);
        this.portCount = devicePortCount;
        portGroups = new ArrayList<>();
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
            IGateway gateway = GatewayFactory.getGatewayInstance(weakActivity.get());
            DeviceMeasurePortAsyncTask dpf = new DeviceMeasurePortAsyncTask(gateway, this, v);
            dpf.execute(port.getPortId());
        } else if (mode == Device.MODE_FIRE) {
            IGateway gateway = GatewayFactory.getGatewayInstance(weakActivity.get());
            DeviceFirePortAsyncTask dpf = new DeviceFirePortAsyncTask(gateway, this, v);
            dpf.execute(port.getPortId());
        }
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

    @Override
    public boolean equals(Object o) {
        if (o instanceof Device) {
            return ((Device) o).getDeviceUid() == getDeviceUid();
        }
        return false;
    }

    @Override
    public String toString() {
        return getDeviceName();
    }

    @Override
    public int hashCode() {
        return getDeviceUid().hashCode();
    }

    public int getPortTimeout() {
        return portTimeout;
    }

    public void setPortTimeout(int timeout) {
        portTimeout = timeout;
    }

}

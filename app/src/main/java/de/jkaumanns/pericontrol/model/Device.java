package de.jkaumanns.pericontrol.model;

/**
 * Created by Joerg on 01.09.2016.
 */
public class Device {

    private String uniqueIdentifier;
    private byte id;
    private byte channelCount;
    private byte mode;
    private String name;

    public byte getDeviceId() {
        return id;
    }

    public byte getDeviceChannelCount() {
        return channelCount;
    }

    public String getDeviceName() {
        return name;
    }

    public byte getMode() {
        return mode;
    }
}

package de.jkaumanns.pericontrol.model;

/**
 * Created by Joerg on 01.09.2016.
 */
public class DevicePort {

    int portId = -1;
    boolean available = false;
    int resistance;
    float power;

    public DevicePort(int portId, boolean available) {
        this.portId = portId;
        this.available = available;
        resistance = Integer.MIN_VALUE;
        power = Float.MIN_VALUE;
    }

    public int getPortId() {
        return portId;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getResistance() {
        return resistance;
    }

    public float getPower() {
        return power;
    }
}

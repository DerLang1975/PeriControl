package de.jkaumanns.pericontrol.model;

import java.io.Serializable;

/**
 * Created by Joerg on 01.09.2016.
 */
public class DevicePort implements Serializable {
    private static final long serialVersionUID = -4124273230291241956L;

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

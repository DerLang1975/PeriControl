package de.jkaumanns.pericontrol.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Joerg on 01.09.2016.
 */
public class DevicePortGroup implements Serializable {
    private static final long serialVersionUID = 6195116709791611748L;

    private ArrayList<DevicePort> portGroup = new ArrayList<>();

    public DevicePortGroup(int channelFrom, int channelTo, int maxPerGroup) {
        for (int i = 0; i < maxPerGroup; i++) {
            if (channelFrom + i <= channelTo) {
                portGroup.add(new DevicePort(channelFrom + i, true));
            } else {
                portGroup.add(new DevicePort(channelFrom + i, false));
            }
        }
    }

    public ArrayList<DevicePort> getPortGroup() {
        return portGroup;
    }
}

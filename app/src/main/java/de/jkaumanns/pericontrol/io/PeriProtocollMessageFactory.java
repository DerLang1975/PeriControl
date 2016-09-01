package de.jkaumanns.pericontrol.io;

/**
 * Created by Joerg on 01.09.2016.
 */
public class PeriProtocollMessageFactory {

    private static final byte COMMAND_GET_DEVICE_UID = (byte) 0x1F;

    public static IPeriProtocollMessage createDiscoverDevicesMessage() {
        PeriProtocollMessage msg = new PeriProtocollMessage();
        msg.setCommand(COMMAND_GET_DEVICE_UID);
        msg.setDeviceId(IPeriProtocollMessage.BROADCAST_MESSAGE);
        return msg;
    }

}

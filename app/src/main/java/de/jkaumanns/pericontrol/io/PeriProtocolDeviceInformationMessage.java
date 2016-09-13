package de.jkaumanns.pericontrol.io;

/**
 * Created by Joerg on 10.09.2016.
 */
public class PeriProtocolDeviceInformationMessage extends PeriProtocolMessage implements IPeriProtocolDeviceInformationMessage {

    private String name;
    private byte portCount;
    private int portTimeout;
    private byte deviceMode;
    private String uid;

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte getDeviceId() {
        return deviceId;
    }

    @Override
    public byte getPortCount() {
        return portCount;
    }

    @Override
    public int getPortTimeout() {
        return portTimeout;
    }

    @Override
    protected void checkContent() {
        if (command == PeriProtocolMessageFactory.COMMAND_GET_DEVICE_INFORMATION) {
            // (6+7+8): UID; (9): ID; (10): PortCount; (11+12): PortTimeOut; (13): Mode; (14->33): DeviceName
            uid = (char) rawResponse[6] + (char) rawResponse[7] + (char) rawResponse[8] + "";
            deviceId = rawResponse[9];
            portCount = rawResponse[10];
            portTimeout = rawResponse[11];
            portTimeout <<= 8;
            portTimeout |= rawResponse[12];
            deviceMode = rawResponse[13];
            name = "";
            for (int i = 0; i < 20; i++) {
                name += (char) rawResponse[14 + i] + "";
            }
            name = name.trim();
        } else if (command == PeriProtocolMessageFactory.COMMAND_GET_DEVICE_UID) {
            uid = (char) rawResponse[6] + (char) rawResponse[7] + (char) rawResponse[8] + "";
        } else if (command == PeriProtocolMessageFactory.COMMAND_GET_DEVICE_ID) {
            deviceId = rawResponse[6];
        } else if (command == PeriProtocolMessageFactory.COMMAND_GET_DEVICE_NAME) {
            name = "";
            for (int i = 0; i < 20; i++) {
                name += (char) rawResponse[6 + i] + "";
            }
            name = name.trim();
        } else if (command == PeriProtocolMessageFactory.COMMAND_GET_DEVICE_PORT_COUNT) {
            portCount = rawResponse[6];
        }
    }
}

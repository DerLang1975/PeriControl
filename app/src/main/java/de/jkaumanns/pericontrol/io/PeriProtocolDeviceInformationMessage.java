package de.jkaumanns.pericontrol.io;

import android.util.Log;

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
        int start = START_ARRAY_ID_OF_MESSAGE;
        if (command == PeriProtocolMessageFactory.COMMAND_GET_DEVICE_INFORMATION) {
            // (6+7+8): UID; (9): ID; (10): PortCount; (11+12): PortTimeOut; (13): Mode; (14->33): DeviceName
            uid = String.valueOf((char) rawResponse[start++]);
            uid += String.valueOf((char) rawResponse[start++]);
            uid += String.valueOf((char) rawResponse[start++]);
            deviceId = rawResponse[start++];
            portCount = rawResponse[start++];
            Log.d("PeriC", "portTimeout: " + portTimeout);
            portTimeout = rawResponse[start++];
            Log.d("PeriC", "portTimeout: " + portTimeout);
            portTimeout <<= 8;
            Log.d("PeriC", "portTimeout: " + portTimeout);
            portTimeout |= rawResponse[start++];
            Log.d("PeriC", "portTimeout: " + portTimeout);
            deviceMode = rawResponse[start++];
            name = "";
            for (int i = 0; i < 20; i++) {
                name += (char) rawResponse[start++] + "";
            }
            name = name.trim();
        } else if (command == PeriProtocolMessageFactory.COMMAND_GET_DEVICE_UID) {
            uid = String.valueOf((char) rawResponse[start++]);
            uid += String.valueOf((char) rawResponse[start++]);
            uid += String.valueOf((char) rawResponse[start++]);
        } else if (command == PeriProtocolMessageFactory.COMMAND_GET_DEVICE_ID) {
            deviceId = rawResponse[start++];
        } else if (command == PeriProtocolMessageFactory.COMMAND_GET_DEVICE_NAME) {
            name = "";
            for (int i = 0; i < 20; i++) {
                name += (char) rawResponse[start++] + "";
            }
            name = name.trim();
        } else if (command == PeriProtocolMessageFactory.COMMAND_GET_DEVICE_PORT_COUNT) {
            portCount = rawResponse[start++];
        }
    }
}

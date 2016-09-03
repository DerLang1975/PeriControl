package de.jkaumanns.pericontrol.io;

import java.util.ArrayList;

/**
 * Created by Joerg on 01.09.2016.
 */
public class PeriProtocollMessage implements IPeriProtocollMessage {

    private byte deviceId;
    private byte command;
    private int messageId;
    private int rssi;
    private ArrayList<Byte> parameters = new ArrayList<>();
    private byte[] rawResponse;

    private String uid;
    private String name;
    private byte portCount;
    private int portTimeout;

    @Override
    public void setRawResponse(byte[] rawResponse) {
        this.rawResponse = rawResponse;
        messageId = rawResponse[0];
        messageId <<= 8;
        messageId |= rawResponse[1];
        deviceId = rawResponse[2];
        rssi = rawResponse[3];
        rssi <<= 8;
        rssi |= rawResponse[4];
        command = rawResponse[5];
        checkContent();
    }

    private void checkContent() {
        switch (command) {
            case PeriProtocollMessageFactory.COMMAND_GET_DEVICE_INFORMATION:
                // (6+7+8): UID; (9): ID; (10): PortCount; (11+12): PortTimeOut;(13+14+15+16+17+18+19+20+21+22+23+24+25+26+27+28+29+30+31+32): DeviceName
                uid = (char) rawResponse[6] + (char) rawResponse[7] + (char) rawResponse[8] + "";
                deviceId = rawResponse[9];
                portCount = rawResponse[10];
                portTimeout = rawResponse[11];
                portTimeout <<= 8;
                portTimeout |= rawResponse[12];
                name = "";
                for (int i = 0; i < 20; i++) {
                    name += (char) rawResponse[13 + i] + "";
                }
                name = name.trim();
                break;
            case PeriProtocollMessageFactory.COMMAND_GET_DEVICE_UID:
                uid = (char) rawResponse[6] + (char) rawResponse[7] + (char) rawResponse[8] + "";
                break;
            case PeriProtocollMessageFactory.COMMAND_GET_DEVICE_NAME:
                name = "";
                for (int i = 0; i < 20; i++) {
                    name += (char) rawResponse[6 + i] + "";
                }
                name = name.trim();
                break;
            case PeriProtocollMessageFactory.COMMAND_GET_DEVICE_PORT_COUNT:
                portCount = rawResponse[6];
                break;
        }
    }

    @Override
    public void setCommand(byte command) {
        this.command = command;
    }

    @Override
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public void addParameter(byte param) {
        parameters.add(param);
    }

    @Override
    public void addParameter(int param) {

    }

    @Override
    public void addParameter(long param) {

    }

    @Override
    public void addParameter(float param) {

    }

    @Override
    public byte[] getMessage() {
        byte[] b = new byte[4 + parameters.size()];
        b[0] = (byte) (messageId >> 8);
        b[1] = (byte) (messageId & 0xFF);
        b[2] = deviceId;
        b[3] = command;
        for (int i = 0; i < parameters.size(); i++) {
            b[4 + i] = parameters.get(i);
        }
        return b;
    }

    @Override
    public int getRssi() {
        return rssi;
    }

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
    public void setDeviceId(byte deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public byte getPortCount() {
        return portCount;
    }

    @Override
    public int getPortTimeout() {
        return portTimeout;
    }
}

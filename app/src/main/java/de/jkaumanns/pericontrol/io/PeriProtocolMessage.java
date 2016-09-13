package de.jkaumanns.pericontrol.io;

import java.util.ArrayList;

/**
 * Created by Joerg on 01.09.2016.
 */
public abstract class PeriProtocolMessage implements IPeriProtocolMessage {

    protected byte deviceId;
    protected byte command;
    protected byte[] rawResponse;
    private int messageId;
    private int rssi;
    private ArrayList<Byte> parameters = new ArrayList<>();

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

    protected abstract void checkContent();

    @Override
    public void setCommand(byte command) {
        this.command = command;
    }

    @Override
    public int getMessageId() {
        return messageId;
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
    public void setDeviceId(byte deviceId) {
        this.deviceId = deviceId;
    }

}

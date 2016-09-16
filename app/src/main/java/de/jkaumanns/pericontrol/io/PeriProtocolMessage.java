package de.jkaumanns.pericontrol.io;

import java.util.ArrayList;

/**
 * Created by Joerg on 01.09.2016.
 */
public abstract class PeriProtocolMessage implements IPeriProtocolMessage {

    protected final int START_ARRAY_ID_OF_MESSAGE = 6;

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
        byte[] b = new byte[8 + parameters.size()];
        int k = 0;
        b[k++] = (byte) 0xCA;
        b[k++] = (byte) 0xFA;
        b[k++] = (byte) (messageId >> 8);
        b[k++] = (byte) (messageId & 0xFF);
        b[k++] = deviceId;
        b[k++] = command;
        for (int i = 0; i < parameters.size(); i++) {
            b[k++] = parameters.get(i);
        }
        b[k++] = (byte) 0xCA;
        b[k++] = (byte) 0xFE;
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

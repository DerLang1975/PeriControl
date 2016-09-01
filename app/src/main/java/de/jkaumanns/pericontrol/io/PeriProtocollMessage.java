package de.jkaumanns.pericontrol.io;

import java.util.ArrayList;

/**
 * Created by Joerg on 01.09.2016.
 */
public class PeriProtocollMessage implements IPeriProtocollMessage {

    private byte deviceId;
    private byte command;
    private byte messageId;
    private ArrayList<Byte> parameters = new ArrayList<>();

    @Override
    public void setDeviceId(byte deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public void setCommand(byte command) {
        this.command = command;
    }

    @Override
    public void setMessageId(byte messageId) {
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
        byte[] b = new byte[3 + parameters.size()];
        b[0] = messageId;
        b[1] = deviceId;
        b[2] = command;
        for (int i = 0; i < parameters.size(); i++) {
            b[3 + i] = parameters.get(i);
        }
        return b;
    }
}

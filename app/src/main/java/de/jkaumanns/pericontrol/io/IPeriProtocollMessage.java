package de.jkaumanns.pericontrol.io;

/**
 * Created by Joerg on 01.09.2016.
 */
public interface IPeriProtocollMessage {

    static final byte BROADCAST_MESSAGE = (byte) 0xFF;

    void setDeviceId(byte deviceId);

    void setCommand(byte command);

    void setMessageId(byte messageId);

    void addParameter(byte param);

    void addParameter(int param);

    void addParameter(long param);

    void addParameter(float param);

    byte[] getMessage();
}

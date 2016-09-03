package de.jkaumanns.pericontrol.io;

/**
 * Created by Joerg on 01.09.2016.
 */
public interface IPeriProtocollMessage {

    static final byte BROADCAST_MESSAGE = (byte) 0xFF;

    void setCommand(byte command);

    void setMessageId(int messageId);

    void addParameter(byte param);

    void addParameter(int param);

    void addParameter(long param);

    void addParameter(float param);

    byte[] getMessage();

    void setRawResponse(byte[] rawResponse);

    int getRssi();

    String getUid();

    String getName();

    byte getDeviceId();

    void setDeviceId(byte deviceId);

    byte getPortCount();

    int getPortTimeout();
}

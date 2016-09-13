package de.jkaumanns.pericontrol.io;

/**
 * Created by Joerg on 01.09.2016.
 */
public interface IPeriProtocolMessage {

    static final byte BROADCAST_MESSAGE = (byte) 0xFF;

    void setCommand(byte command);

    void setDeviceId(byte deviceId);

    void addParameter(byte param);

    void addParameter(int param);

    void addParameter(long param);

    void addParameter(float param);

    byte[] getMessage();

    void setRawResponse(byte[] rawResponse);

    int getRssi();

    int getMessageId();

    void setMessageId(int messageId);
}

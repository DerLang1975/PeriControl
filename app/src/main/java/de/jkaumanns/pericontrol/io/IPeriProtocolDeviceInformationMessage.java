package de.jkaumanns.pericontrol.io;

/**
 * Created by Joerg on 10.09.2016.
 */
public interface IPeriProtocolDeviceInformationMessage extends IPeriProtocolMessage {

    String getName();

    byte getPortCount();

    int getPortTimeout();

    byte getDeviceId();

    String getUid();
}

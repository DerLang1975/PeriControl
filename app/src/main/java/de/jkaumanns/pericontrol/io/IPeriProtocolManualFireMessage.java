package de.jkaumanns.pericontrol.io;

/**
 * Created by Joerg on 10.09.2016.
 */
public interface IPeriProtocolManualFireMessage extends IPeriProtocolMessage {

    boolean isPortFired();

    byte getPort();
}

package de.jkaumanns.pericontrol.io;

/**
 * Created by Joerg on 15.09.2016.
 */
public interface IPeriProtocolChangeModeMessage extends IPeriProtocolMessage {

    byte getOldMode();

    byte getNewMode();

}

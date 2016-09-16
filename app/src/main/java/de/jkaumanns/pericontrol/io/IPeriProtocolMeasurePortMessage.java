package de.jkaumanns.pericontrol.io;

/**
 * Created by Joerg on 10.09.2016.
 */
public interface IPeriProtocolMeasurePortMessage extends IPeriProtocolMessage {

    String getResistance();

    String getPower();

    String getFireVoltage();

    double getPowerValue();

    byte getPort();
}

package de.jkaumanns.pericontrol.io;

/**
 * Created by Joerg on 10.09.2016.
 */
public class PeriProtocolMeasurePortMessage extends PeriProtocolMessage implements IPeriProtocolMeasurePortMessage {

    // in milli-Ohm
    private int resistanceValue;
    // in milli-Volts
    private int fireVoltageValue;

    @Override
    protected void checkContent() {
        // TODO: get the values from byte stream...
    }

    @Override
    public String getResistance() {
        int value = resistanceValue / 1000;
        int r = resistanceValue % 1000;
        String rest = (r + "").substring(0, 2);
        String retValue = value + "," + rest;
        return retValue;
    }

    @Override
    public String getPower() {
        int power = fireVoltageValue / resistanceValue;
        int value = power / 1000;
        int r = power % 1000;
        String rest = (r + "").substring(0, 1);
        String retValue = value + "," + rest;
        return retValue;
    }

    @Override
    public String getFireVoltage() {
        int value = fireVoltageValue / 1000;
        int r = fireVoltageValue % 1000;
        String rest = (r + "").substring(0, 2);
        String retValue = value + "," + rest;
        return retValue;
    }
}

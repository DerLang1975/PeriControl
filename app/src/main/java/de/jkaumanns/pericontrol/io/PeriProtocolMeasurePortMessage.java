package de.jkaumanns.pericontrol.io;

import android.util.Log;

/**
 * Created by Joerg on 10.09.2016.
 */
public class PeriProtocolMeasurePortMessage extends PeriProtocolMessage implements IPeriProtocolMeasurePortMessage {

    // in milli-Ohm
    private int resistanceValue;
    // in milli-Volts
    private int fireVoltageValue;

    private byte port;

    @Override
    protected void checkContent() {
        // 1: port; 2-5: resistanceValue; 6-9: fireVoltageValue
        int startId = START_ARRAY_ID_OF_MESSAGE;
        port = rawResponse[startId++];
        resistanceValue = rawResponse[startId++];
        resistanceValue <<= 8;
        resistanceValue |= rawResponse[startId++];
        resistanceValue <<= 8;
        resistanceValue |= rawResponse[startId++];
        resistanceValue <<= 8;
        resistanceValue |= rawResponse[startId++];
        Log.d("PeriC", "resistanceValue:" + resistanceValue);
        fireVoltageValue = rawResponse[startId++];
        fireVoltageValue <<= 8;
        fireVoltageValue |= rawResponse[startId++];
        fireVoltageValue <<= 8;
        fireVoltageValue |= rawResponse[startId++];
        fireVoltageValue <<= 8;
        fireVoltageValue |= rawResponse[startId++];
        Log.d("PeriC", "fireVoltageValue:" + fireVoltageValue);
    }

    @Override
    public String getResistance() {
        Log.d("PeriC", "Begin PeriProtocolMeasurePortMessage.getResistance");
        int value = resistanceValue / 1000;
        Log.d("PeriC", "PeriProtocolMeasurePortMessage.getResistance value: " + value);
        int r = resistanceValue % 1000;
        Log.d("PeriC", "PeriProtocolMeasurePortMessage.getResistance rest: " + r);
        String rest = (r + "").substring(0, 2);
        String retValue = value + "," + rest + " \u2126";
        Log.d("PeriC", "End PeriProtocolMeasurePortMessage.getResistance: " + retValue);
        return retValue;
    }

    @Override
    public String getPower() {
        Log.d("PeriC", "Begin PeriProtocolMeasurePortMessage.getPower");
        int power = fireVoltageValue * 1000 / resistanceValue;
        Log.d("PeriC", "PeriProtocolMeasurePortMessage.getResistance power: " + power);
        int value = power / 1000;
        Log.d("PeriC", "PeriProtocolMeasurePortMessage.getResistance value: " + value);
        int r = power % 1000;
        Log.d("PeriC", "PeriProtocolMeasurePortMessage.getResistance rest: " + r);
        String rest = (r + "").substring(0, 1);
        String retValue = value + "," + rest + " A";
        Log.d("PeriC", "End PeriProtocolMeasurePortMessage.getResistance: " + retValue);
        return retValue;
    }

    @Override
    public double getPowerValue() {
        Log.d("PeriC", "Begin PeriProtocolMeasurePortMessage.getPowerValue");
        double power = (fireVoltageValue * 1f) / resistanceValue;
        Log.d("PeriC", "End PeriProtocolMeasurePortMessage.getResistance: " + power);
        return power;
    }

    @Override
    public String getFireVoltage() {
        Log.d("PeriC", "Begin PeriProtocolMeasurePortMessage.getFireVoltage");
        int value = fireVoltageValue / 1000;
        int r = fireVoltageValue % 1000;
        String rest = (r + "").substring(0, 2);
        String retValue = value + "," + rest;
        Log.d("PeriC", "End PeriProtocolMeasurePortMessage.getResistance: " + retValue);
        return retValue;
    }

    @Override
    public byte getPort() {
        return port;
    }
}

package de.jkaumanns.pericontrol.io;

import de.jkaumanns.pericontrol.model.Device;

/**
 * Created by Joerg on 01.09.2016.
 */
public class PeriProtocolMessageFactory {

    public static final byte COMMAND_GET_DEVICE_INFORMATION = (byte) 0x01;
    public static final byte COMMAND_GET_DEVICE_UID = (byte) 0x03;
    public static final byte COMMAND_GET_DEVICE_ID = (byte) 0x05;
    public static final byte COMMAND_GET_DEVICE_NAME = (byte) 0x07;
    public static final byte COMMAND_GET_DEVICE_PORT_COUNT = (byte) 0x09;
    public static final byte COMMAND_GET_DEVICE_PORT_TIMEOUT = (byte) 0x0B;
    public static final byte COMMAND_GET_DEVICE_GENERAL1 = (byte) 0x0D;
    public static final byte COMMAND_GET_DEVICE_GENERAL2 = (byte) 0x0F;
    public static final byte COMMAND_SET_DEVICE_INFORMATION = (byte) 0x11;
    public static final byte COMMAND_GET_DEVICE_GENERAL4 = (byte) 0x13;
    public static final byte COMMAND_GET_DEVICE_GENERAL5 = (byte) 0x15;
    public static final byte COMMAND_GET_DEVICE_GENERAL6 = (byte) 0x17;
    public static final byte COMMAND_GET_DEVICE_GENERAL7 = (byte) 0x19;
    public static final byte COMMAND_GET_DEVICE_GENERAL8 = (byte) 0x1B;
    public static final byte COMMAND_GET_DEVICE_GENERAL9 = (byte) 0x1D;
    public static final byte COMMAND_GET_DEVICE_GENERALA = (byte) 0x1F;

    public static final byte COMMAND_SET_MODE = (byte) 0x31;

    public static final byte COMMAND_MEASURE_PORT = (byte) 0x52;

    public static final byte COMMAND_FIRE_CHANNEL = (byte) 0x62;

    public static IPeriProtocolMessage createDiscoverDevicesMessage() {
        IPeriProtocolDeviceInformationMessage msg = new PeriProtocolDeviceInformationMessage();
        msg.setCommand(COMMAND_GET_DEVICE_INFORMATION);
        msg.setDeviceId(IPeriProtocolMessage.BROADCAST_MESSAGE);
        return msg;
    }

    public static IPeriProtocolMessage createGetDeviceInformationMessage(byte deviceId) {
        IPeriProtocolDeviceInformationMessage msg = new PeriProtocolDeviceInformationMessage();
        msg.setCommand(COMMAND_GET_DEVICE_INFORMATION);
        msg.setDeviceId(deviceId);
        return msg;
    }

    public static IPeriProtocolMessage createGetDeviceName(byte deviceId) {
        IPeriProtocolDeviceInformationMessage msg = new PeriProtocolDeviceInformationMessage();
        msg.setCommand(COMMAND_GET_DEVICE_NAME);
        msg.setDeviceId(deviceId);
        return msg;
    }

    public static IPeriProtocolMessage createGetDevicePortCount(byte deviceId) {
        IPeriProtocolDeviceInformationMessage msg = new PeriProtocolDeviceInformationMessage();
        msg.setCommand(COMMAND_GET_DEVICE_PORT_COUNT);
        msg.setDeviceId(deviceId);
        return msg;
    }

    public static IPeriProtocolMessage createFirePort(byte deviceId, byte portNo) {
        IPeriProtocolManualFireMessage msg = new PeriProtocolManualFireMessage();
        msg.setCommand(COMMAND_FIRE_CHANNEL);
        msg.setDeviceId(deviceId);
        msg.addParameter(portNo);
        return msg;
    }

    public static IPeriProtocolMessage createMeasurePort(byte deviceId, byte portNo) {
        IPeriProtocolMeasurePortMessage msg = new PeriProtocolMeasurePortMessage();
        msg.setCommand(COMMAND_MEASURE_PORT);
        msg.setDeviceId(deviceId);
        msg.addParameter(portNo);
        return msg;
    }

    public static IPeriProtocolMessage createChangeModeMessage(byte deviceId, byte newMode) {
        IPeriProtocolChangeModeMessage msg = new PeriProtocolChangeModeMessage();
        msg.setCommand(COMMAND_SET_MODE);
        msg.setDeviceId(deviceId);
        msg.addParameter(newMode);
        return msg;
    }

    public static IPeriProtocolMessage createSetDeviceInformationMessage(Device device) {
        IPeriProtocolSetDeviceInformationMessage msg = new PeriProtocolSetDeviceInformationMessage();
        msg.setCommand(COMMAND_SET_DEVICE_INFORMATION);
        msg.setDeviceId(device.getDeviceId());
        msg.addParameter(device.getDeviceId());
        msg.addParameter((byte) (device.getPortTimeout() >> 8) & 0xFF);
        msg.addParameter((byte) (device.getPortTimeout() & 0xFF));
        int i = 0;
        for (; i < device.getName().length(); i++) msg.addParameter(device.getName().charAt(i));
        for (; i < 20; i++) msg.addParameter((byte) ' ');
        return msg;
    }
}

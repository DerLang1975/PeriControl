package de.jkaumanns.pericontrol.io;

/**
 * Created by Joerg on 10.09.2016.
 */
public class PeriProtocolManualFireMessage extends PeriProtocolMessage implements IPeriProtocolManualFireMessage {

    private boolean isPortFired = false;

    @Override
    protected void checkContent() {
        if (command == PeriProtocolMessageFactory.COMMAND_FIRE_CHANNEL) {
            this.isPortFired = rawResponse[6] == (byte) 0x01;
        }
    }

    @Override
    public boolean isPortFired() {
        return this.isPortFired;
    }
}

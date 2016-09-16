package de.jkaumanns.pericontrol.io;

/**
 * Created by Joerg on 15.09.2016.
 */
public class PeriProtocolChangeModeMessage extends PeriProtocolMessage implements IPeriProtocolChangeModeMessage {

    private byte oldMode;
    private byte newMode;

    @Override
    public byte getOldMode() {
        return oldMode;
    }

    @Override
    public byte getNewMode() {
        return newMode;
    }

    @Override
    protected void checkContent() {
        int start = START_ARRAY_ID_OF_MESSAGE;
        oldMode = rawResponse[start++];
        newMode = rawResponse[start++];
    }
}

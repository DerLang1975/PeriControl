package de.jkaumanns.pericontrol.io;

/**
 * Created by Joerg on 18.09.2016.
 */
public class PeriProtocolSetDeviceInformationMessage extends PeriProtocolDeviceInformationMessage implements IPeriProtocolSetDeviceInformationMessage {

    boolean isUpdateSuccessful = false;

    @Override
    public boolean updateSuccessful() {
        return isUpdateSuccessful;
    }

    @Override
    protected void checkContent() {
        super.checkContent();
        // TODO how to enhance it to get first information and then all the rest of usual device information...
    }
}

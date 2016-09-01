package de.jkaumanns.pericontrol.io;

/**
 * Created by Joerg on 01.09.2016.
 */
public class UsbGateway implements IGateway {

    @Override
    public int writeMessage(IPeriProtocollMessage message) {
        return 0;
    }

    @Override
    public IPeriProtocollMessage retrieveMessage(int messageId) {
        return null;
    }

    @Override
    public IPeriProtocollMessage retrieveMessage(int messageId, int timeoutMills) {
        return null;
    }

    @Override
    public IPeriProtocollMessage retrieveMessage(int messageId, int maxMessageCount, int timeoutMills) {
        return null;
    }
}

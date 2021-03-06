package de.jkaumanns.pericontrol.io;

import java.util.ArrayList;

/**
 * Created by Joerg on 01.09.2016.
 */
public interface IGateway {

    /**
     * Queues the message and sends it to the gateway as soon as possible.
     *
     * @param message The message to send to the devices.
     * @return the message id which is related to the send message.
     */
    IPeriProtocolMessage writeMessage(IPeriProtocolMessage message);

    /**
     * Waits for the message belonging to the message id till the timeout of 2 seconds has reached.
     *
     * @param message the message from Gateway belonging to the message id.
     * @return Either null if message was not retrieved from Gateway or the response message.
     */
    IPeriProtocolMessage retrieveMessage(IPeriProtocolMessage message);

    /**
     * Waits for the message belonging to the message id till the timeout has reached.
     *
     * @param message    the message from Gateway belonging to the message id.
     * @param timeoutMills the time to wait for a response in milliseconds.
     * @return Either null if message was not retrieved from Gateway or the response message.
     */
    IPeriProtocolMessage retrieveMessage(IPeriProtocolMessage message, int timeoutMills);

    /**
     * Waits for the message belonging to the message id till the timeout has reached.
     *
     * @param message
     * @param maxMessageCount
     * @param timeoutMills
     * @return
     */
    ArrayList<IPeriProtocolMessage> retrieveMessage(IPeriProtocolMessage message, int maxMessageCount, int timeoutMills);

    /**
     * removes the messageId from the list.
     *
     * @param messageId
     */
    void clean(int messageId);
}

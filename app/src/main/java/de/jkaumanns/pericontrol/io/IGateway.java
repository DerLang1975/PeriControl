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
    int writeMessage(IPeriProtocollMessage message);

    /**
     * Waits for the message belonging to the message id till the timeout of 2 seconds has reached.
     *
     * @param messageId the message from Gateway belonging to the message id.
     * @return Either null if message was not retrieved from Gateway or the response message.
     */
    IPeriProtocollMessage retrieveMessage(int messageId);

    /**
     * Waits for the message belonging to the message id till the timeout has reached.
     *
     * @param messageId    the message from Gateway belonging to the message id.
     * @param timeoutMills the time to wait for a response in milliseconds.
     * @return Either null if message was not retrieved from Gateway or the response message.
     */
    IPeriProtocollMessage retrieveMessage(int messageId, int timeoutMills);

    /**
     * Waits for the message belonging to the message id till the timeout has reached.
     *
     * @param messageId
     * @param maxMessageCount
     * @param timeoutMills
     * @return
     */
    ArrayList<IPeriProtocollMessage> retrieveMessage(int messageId, int maxMessageCount, int timeoutMills);

    /**
     * removes the messageId from the list.
     *
     * @param messageId
     */
    void clean(int messageId);
}

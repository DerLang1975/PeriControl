package de.jkaumanns.pericontrol.io;

/**
 * Created by Joerg on 01.09.2016.
 */
public class GatewayFactory {

    private static IGateway gateway;

    public static IGateway getGatewayInstance() {
        if (gateway == null) {
            // TODO: determine what should be done. Either Bluetooth or USB
            gateway = new UsbGateway();
        }
        return gateway;
    }

}

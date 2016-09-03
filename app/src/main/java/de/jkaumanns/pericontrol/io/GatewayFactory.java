package de.jkaumanns.pericontrol.io;

import android.app.Activity;

/**
 * Created by Joerg on 01.09.2016.
 */
public class GatewayFactory {


    private static IGateway gateway;

    public static IGateway getGatewayInstance(Activity activity) {
        if (gateway == null && activity != null) {
            // TODO: determine what should be done. Either Bluetooth or USB
            gateway = new UsbGateway(activity);
        }
        return gateway;
    }

}

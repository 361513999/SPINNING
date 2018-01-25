package com.hhkj.spinning.www.utils;

import com.hhkj.spinning.www.common.BaseApplication;
import com.inuker.bluetooth.library.BluetoothClient;

/**
 * Created by dingjikerbo on 2016/8/27.
 */
public class ClientManager {

    private static BluetoothClient mClient;

    public static BluetoothClient getClient() {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(BaseApplication.application);
                }
            }
        }
        return mClient;
    }
}

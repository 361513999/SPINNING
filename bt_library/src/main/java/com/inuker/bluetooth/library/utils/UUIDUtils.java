package com.inuker.bluetooth.library.utils;

import java.util.UUID;

/**
 * Created by dingjikerbo on 2015/11/4.
 */
public class UUIDUtils {
//    public static final String UUID_FORMAT = "0000%04x-0000-1000-8000-00805f9b34fb";
public static final String UUID_FORMAT = "49535343-FE7D-4AE5-8FA9-9FAFD205E455";


    public static UUID makeUUID(int value) {
        return UUID.fromString(String.format(UUID_FORMAT, value));
    }

    public static int getValue(UUID uuid) {
        return (int) (uuid.getMostSignificantBits() >>> 32);
    }
}

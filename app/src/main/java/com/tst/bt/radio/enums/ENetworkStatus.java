package com.tst.bt.radio.enums;

/**
 * Created by Roman.
 */



//<stat>:
//        0 not registered, MT is not currently searching a new operator to register to
//        1 registered, home network
//        2 not registered, but MT is currently searching a new operator to register to
//        3 registration denied
//        4 unknown
//        5 registered, roaming

public enum ENetworkStatus {
    not_registered(0),
    registered(1),
    searching_operator(2),
    registration_denied(3),
    unknown(4),
    registered_roaming(5);

    private final int value;

    ENetworkStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ENetworkStatus parse(int value) {
        for (ENetworkStatus e : values()) {
            if (e.value == value) {
                return e;
            }
        }

        return null;
    }
}

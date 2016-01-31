package com.tst.bt.radio.enums;

/**
 * Created by Roman.
 */




//        0  CLCC_MT_CALL
//        1  CSMCC_DISCONNECT_MSG
//        2  CSMCC_ALERT_MSG
//        3  CSMCC_CALL_PROCESS_MSG
//        4  CSMCC_SYNC_MSG
//        5  CSMCC_PROGRESS_MSG
//        6  CSMCC_CALL_CONNECTED_MSG
//        129  CSMCC_ALL_CALLS_DISC_MSG
//        130  CSMCC_CALL_ID_ASSIGN_MSG
//        131  CSMCC_STATE_CHANGE_HELD
//        132  CSMCC_STATE_CHANGE_ACTIVE
//        133  CSMCC_STATE_CHANGE_DISCONNECTED
//        134  CSMCC_STATE_CHANGE_MO_DISCONNECTING

public enum ECallStatus {
    MT_CALL(0),
    DISCONNECT_MSG(1),
    ALERT_MSG(2),
    CALL_PROCESS_MSG(3),
    SYNC_MSG(4),
    PROGRESS_MSG(5),
    CALL_CONNECTED_MSG(6),
    ALL_CALLS_DISC_MSG(129),
    CALL_ID_ASSIGN_MSG(130),
    HELD(131),
    ACTIVE(132),
    DISCONNECTED(133),
    MO_DISCONNECTING(134);

    private final int value;

    ECallStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ECallStatus parse(int value) {
        for (ECallStatus e : values()) {
            if (e.value == value) {
                return e;
            }
        }

        return null;
    }
}

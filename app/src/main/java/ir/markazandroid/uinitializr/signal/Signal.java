package ir.markazandroid.uinitializr.signal;

import java.io.Serializable;

/**
 * Coded by Ali on 29/07/2017.
 */

public class Signal implements Serializable {

    public static final int SIGNAL_LOGIN = 0x00000001;
    public static final int SIGNAL_RESPONSE = 0x00000002;
    public static final int SIGNAL_LOGOUT = 0x00000004;
    public static final int SIGNAL_VIEW_DESTROYED = 0x00000008;
    public static final int SIGNAL_ACTIVITY_DESTROYED = 0x00000010 | SIGNAL_VIEW_DESTROYED;
    public static final int SIGNAL_REFRESH_RECORDS = 0x00000020;
    public static final int SIGNAL_VIDEO_STARTED = 0x00000040;
    public static final int SIGNAL_VIDEO_ENDED = 0x00000080;


    private String msg;
    private int type;

    private Serializable extras;

    public Signal(String msg, int type, Serializable extras) {
        this.msg = msg;
        this.type = type;
        this.extras = extras;
    }

    public Signal(String msg, int type) {
        this.msg = msg;
        this.type = type;
    }

    public Signal(int type) {
        this.type = type;
    }


    public Signal() {
    }

    public Serializable getExtras() {
        return extras;
    }

    public void setExtras(Serializable extras) {
        this.extras = extras;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

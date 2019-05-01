package ir.markazandroid.uinitializr.network;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ir.markazandroid.uinitializr.UInitializrApplication;
import ir.markazandroid.uinitializr.activity.LoginActivity;
import ir.markazandroid.uinitializr.signal.Signal;
import ir.markazandroid.uinitializr.signal.SignalManager;
import ir.markazandroid.uinitializr.signal.SignalReceiver;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Coded by Ali on 01/04/2017.
 */
abstract class CBack implements Callback, SignalReceiver {

    private State state;
    private boolean isCancelled = false;
    private Context mContext;
    private Handler handler;
    private String tag;


    public CBack(Context context, State state, String tag) {
        this.state = state;
        this.tag = tag;
        state.state = State.IS_RUNNING;
        mContext = context;
        handler = new Handler(context.getMainLooper());
        getSignalManager().addReceiver(this);
    }

    CBack(Context context, String tag) {
        mContext = context;
        handler = new Handler(context.getMainLooper());
        this.tag = tag;
        getSignalManager().addReceiver(this);
    }

    public State getState() {
        return state;
    }


    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        getSignalManager().removeReceiver(this);
        if (!isCancelled) {
            try {
                fail(e);
            } finally {
                if (state != null)
                    state.state = State.FAILED;
                getSignalManager().sendMainSignal(new Signal("Failed", Signal.SIGNAL_RESPONSE));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "اشکال در اتصال به اینترنت.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String s = response.body().string();
        Log.e("Response", s);
        try {
            if (!isCancelled) {
                getSignalManager().sendMainSignal(new Signal("Response", Signal.SIGNAL_RESPONSE));
                try {
                    if (!isSuccessfull(response.code())) return;
                    if (s.startsWith("{")) {
                        JSONObject object = new JSONObject(s);
                        if (!isSuccessfull(response.code(), object)) return;
                        result(object);
                    } else if (s.startsWith("[")) result(new JSONArray(s));
                    result(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (state != null) {
                    state.state = State.DONE;
                }
            }
        } finally {
            getSignalManager().removeReceiver(this);
            response.close();
        }
    }

    @Override
    public boolean onSignal(Signal signal) {
        if ((signal.getType() & Signal.SIGNAL_VIEW_DESTROYED) != 0
                && signal.getMsg().equals(tag)) {
            setCancelled(true);
            Log.d("CBack", "request for " + signal.getMsg() + " callBack canceled.");
            return true;
        }
        return false;
    }

    public void result(JSONObject response) {
    }

    public void result(JSONArray response) {
    }

    public void result(String response) {
    }

    public void fail(IOException e) {
    }

    public boolean isSuccessfull(int code, JSONObject response) {
        return true;
    }

    public boolean isSuccessfull(int code) {
        if (code == 403 || code == 401) {
            getSignalManager().sendMainSignal(new Signal("Logout", Signal.SIGNAL_LOGOUT));
            Log.e(toString(), "logout signal sent");
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            return false;
        }
        return true;
    }

    private SignalManager getSignalManager() {
        return ((UInitializrApplication) mContext.getApplicationContext())
                .getSignalManager();
    }
}

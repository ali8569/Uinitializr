package ir.markazandroid.uinitializr.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ir.markazandroid.uinitializr.UInitializrApplication;
import ir.markazandroid.uinitializr.activity.EnterNameActivity;

/**
 * Created by Ali on 23/01/2017.
 */
public class BootReceiver extends BroadcastReceiver {
    public static final int Starter_activity = 1254786;
    private Context context;

    @Override
    public void onReceive(Context context, Intent arg) {
        this.context = context;

        if (getPreferencesManager().isStartedAfterArduinoTest()) {
            Log.e("Receive", "true");
            getPreferencesManager().setArduinoTesting(false);
            Intent intent = new Intent(context.getApplicationContext(), EnterNameActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(intent);
        }
    }

    private PreferencesManager getPreferencesManager() {
        return ((UInitializrApplication) context.getApplicationContext()).getPreferencesManager();
    }
}

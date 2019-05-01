package ir.markazandroid.uinitializr.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import ir.markazandroid.uinitializr.UInitializrApplication;
import ir.markazandroid.uinitializr.network.NetworkManager;
import ir.markazandroid.uinitializr.object.User;
import ir.markazandroid.uinitializr.signal.Signal;
import ir.markazandroid.uinitializr.signal.SignalManager;
import ir.markazandroid.uinitializr.util.PreferencesManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Coded by Ali on 02/02/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    PreferencesManager getPreferencesManager() {
        return ((UInitializrApplication) getApplication()).getPreferencesManager();
    }

    private NetworkManager networkManager;


    protected NetworkManager getNetworkManager() {
        if (networkManager == null) {
            networkManager = new NetworkManager.Builder()
                    .from(this)
                    .tag(toString())
                    .build();
        }
        return networkManager;
    }


    protected SignalManager getSignalManager() {
        return ((UInitializrApplication) getApplication()).getSignalManager();
    }

    protected User getUser() {
        return ((UInitializrApplication) getApplication()).getUser();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSignalManager().sendSignal(new Signal(toString(), Signal.SIGNAL_ACTIVITY_DESTROYED));
    }
}

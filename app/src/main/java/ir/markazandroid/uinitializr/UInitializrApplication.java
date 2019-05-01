package ir.markazandroid.uinitializr;

import android.app.Application;
import android.util.Log;

import ir.markazandroid.uinitializr.network.JSONParser.Parser;
import ir.markazandroid.uinitializr.network.NetworkClient;
import ir.markazandroid.uinitializr.object.ErrorObject;
import ir.markazandroid.uinitializr.object.FieldError;
import ir.markazandroid.uinitializr.object.User;
import ir.markazandroid.uinitializr.signal.Signal;
import ir.markazandroid.uinitializr.signal.SignalManager;
import ir.markazandroid.uinitializr.signal.SignalReceiver;
import ir.markazandroid.uinitializr.util.PreferencesManager;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Coded by Ali on 10/3/2018.
 */
public class UInitializrApplication extends Application implements SignalReceiver {


    public static final String SHARED_PREFRENCES = "shared_pref";

    private SignalManager signalManager;
    private NetworkClient networkClient;
    private Parser parser;
    private PreferencesManager preferencesManager;
    private User user;


    public NetworkClient getNetworkClient() {
        if (networkClient == null) {
            networkClient = new NetworkClient(this);
        }
        return networkClient;
    }


    public SignalManager getSignalManager() {
        if (signalManager == null) signalManager = new SignalManager(this);
        return signalManager;
    }

    public Parser getParser() throws NoSuchMethodException {
        if (parser == null) {
            parser = new Parser()
                    .addClass(ErrorObject.class)
                    .addClass(FieldError.class)
                    .addClass(User.class)
                    .addClass(User.Role.class);
        }
        return parser;
    }

    public PreferencesManager getPreferencesManager() {
        if (preferencesManager == null)
            preferencesManager = new PreferencesManager(getSharedPreferences(SHARED_PREFRENCES, MODE_PRIVATE));
        return preferencesManager;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        getSignalManager().addReceiver(this);

        CalligraphyConfig.Builder config = new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath);

        // if (!Locale.getDefault().getLanguage().equals("en")){
        config.setDefaultFontPath("font/BYekan.ttf");
        // }

        CalligraphyConfig.initDefault(config.build());


    }

    @Override
    public boolean onSignal(Signal signal) {
        if (signal.getType() == Signal.SIGNAL_LOGIN) {
            setUser((User) signal.getExtras());
            Log.e(toString(), "login signal recived " /*+ user.getUsername()*/);
            //Toast.makeText(this,"ورود با موفقیت انجام شد.",Toast.LENGTH_SHORT).show();
            return true;
        } else if (signal.getType() == Signal.SIGNAL_LOGOUT) {
            Log.e(toString(), "logout signal recived ");
            getNetworkClient().deleteCookies();
            //DeleteToken deleteToken = new DeleteToken();
            //deleteToken.execute();
            setUser(null);
            return true;
        }
        return false;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

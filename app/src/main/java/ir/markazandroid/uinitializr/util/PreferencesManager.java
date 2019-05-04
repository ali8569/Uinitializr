package ir.markazandroid.uinitializr.util;

import android.content.SharedPreferences;

/**
 * Coded by Ali on 09/01/2018.
 */

public class PreferencesManager {


    private SharedPreferences sharedPreferences;

    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String IS_ARDUINO_TESTING = "IS_ARDUINO_TESTING";

    public PreferencesManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public String getUsername() {
        return sharedPreferences.getString(USERNAME, null);
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD, null);
    }

    public void setUsername(String username) {
        sharedPreferences.edit().putString(USERNAME, username).apply();
    }

    public void setPassword(String password) {
        sharedPreferences.edit().putString(PASSWORD, password).commit();
    }

    public boolean isStartedAfterArduinoTest() {
        return sharedPreferences.getBoolean(IS_ARDUINO_TESTING, false);
    }

    public void setArduinoTesting(boolean isTesting) {
        sharedPreferences.edit().putBoolean(IS_ARDUINO_TESTING, isTesting).apply();
    }
}

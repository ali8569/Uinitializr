package ir.markazandroid.uinitializr;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class TestActivity extends AppCompatActivity {

    //Variable to store brightness value
    private int brightness;
    //Content resolver used as a handle to the system's settings
    private ContentResolver cResolver;
    //Window object, that will store a reference to the current win«dow
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //Get the content resolver
        cResolver = getContentResolver();

//Get the current window
        window = getWindow();

        // To handle the auto
        //Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        //Get the current system brightness
        // brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);

        //Set the system brightness using the brightness variable value
        //Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        //Get the current window attributes
        WindowManager.LayoutParams layoutpars = window.getAttributes();
        //Set the brightness of this window
        brightness = 100;
        layoutpars.screenBrightness = brightness / (float) 255;
        //Apply attribute changes to this window
        window.setAttributes(layoutpars);
    }
}

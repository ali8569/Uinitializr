package ir.markazandroid.uinitializr.activity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import ir.markazandroid.uinitializr.R;
import ir.markazandroid.uinitializr.hardware.PortReader;

public class ArduinoTester extends BaseActivity {

    private TextView output;
    private Button send, test;
    private PortReader portReader;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino_tester);
        input=findViewById(R.id.input);
        output=findViewById(R.id.output);
        send=findViewById(R.id.send);
        test = findViewById(R.id.test);

        output.setMovementMethod(new ScrollingMovementMethod());

        portReader = new PortReader(this, "/dev/ttyS4");
        portReader.setListener(this::onDataReceive);
        portReader.start();

        send.setOnClickListener(v -> portReader.write(input.getText().toString()));
        test.setOnClickListener(v -> testArduino());

    }


    private void onDataReceive(String data){
        runOnUiThread(() -> output.append(data + "\n"));
    }

    private void testArduino() {
        getPreferencesManager().setArduinoTesting(true);
        Toast.makeText(this, "دستگاه تا چند لحظه دیگر خاموش می شود.", Toast.LENGTH_LONG).show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                portReader.write("T:10:10:10:10:10:2019:10:10:10:11#");

            }
        }, 5_000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        portReader.close();
    }
}

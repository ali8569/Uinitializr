package ir.markazandroid.uinitializr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ir.markazandroid.uinitializr.hardware.PortReader;

public class ArduinoTester extends AppCompatActivity {

    private TextView output;
    private Button send;
    private PortReader portReader;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino_tester);
        input=findViewById(R.id.input);
        output=findViewById(R.id.output);
        send=findViewById(R.id.send);

        output.setMovementMethod(new ScrollingMovementMethod());

        portReader = new PortReader(this);
        portReader.setListener(this::onDataReceive);
        portReader.start();

        send.setOnClickListener(v -> portReader.write(input.getText().toString()));

    }


    private void onDataReceive(String data){
        runOnUiThread(()->output.append(data));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        portReader.close();
    }
}

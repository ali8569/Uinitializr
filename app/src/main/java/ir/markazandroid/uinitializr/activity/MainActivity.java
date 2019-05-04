package ir.markazandroid.uinitializr.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Button;

import ir.markazandroid.uinitializr.R;
import ir.markazandroid.uinitializr.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setUser(getUser());

        start = findViewById(R.id.start);

        start.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ArduinoTester.class);
            startActivity(intent);
            finish();
        });
    }
}

package ir.markazandroid.uinitializr.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ir.markazandroid.uinitializr.R;
import ir.markazandroid.uinitializr.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setUser(getUser());

    }
}

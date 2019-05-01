package ir.markazandroid.uinitializr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import ir.markazandroid.uinitializr.R;
import ir.markazandroid.uinitializr.UInitializrApplication;
import ir.markazandroid.uinitializr.fragment.LoginFragment;
import ir.markazandroid.uinitializr.network.OnResultLoaded;
import ir.markazandroid.uinitializr.object.User;
import ir.markazandroid.uinitializr.signal.Signal;
import ir.markazandroid.uinitializr.util.Utils;

public class LoginActivity extends BaseActivity implements
        LoginFragment.LoginSuccessListener {

    private ImageView logo;
    private RelativeLayout parentLayout;
    private ProgressBar progressBar;
    private Button retry;
    private boolean isStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        logo = findViewById(R.id.logo);
        parentLayout = findViewById(R.id.parent_layout);
        progressBar = findViewById(R.id.progressbar);
        retry = findViewById(R.id.retry);

        retry.setOnClickListener((v) -> checkIfLogin());

        isStart = getIntent().getAction() != null;

        if (!isStart) {
            animationEnd();
        } else {
            logo.postDelayed(this::checkIfLogin, 2000);
        }

    }


    private void loadRegisterLoginPage() {
        int height = logo.getTop();
        final int margin = getMargin();
        int newHeight = height - margin;
        Log.e("height", height + "  " + margin + "  " + newHeight);
        TranslateAnimation animation = new TranslateAnimation
                (Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -newHeight);
        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setFillBefore(false);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        logo.startAnimation(animation);
    }

    private int getMargin() {
        return dpToPx(32);
    }

    private void animationEnd() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) logo.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutParams.setMargins(dpToPx(32), getMargin(), dpToPx(32), 0);
        getSupportFragmentManager().beginTransaction().add(R.id.container, new LoginFragment()).commit();
        View container = findViewById(R.id.container);
        Utils.fade(container, progressBar, 500);
    }

    private void checkIfLogin() {
        retry.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        getNetworkManager().getUser(new OnResultLoaded<User>() {
            @Override
            public void loaded(final User result) {
                runOnUiThread(() -> {
                    if (result == null)
                        parentLayout.post(LoginActivity.this::loadRegisterLoginPage);
                    else {
                        onLoginSuccess(result);
                    }
                });
            }

            @Override
            public void failed(Exception e) {
                runOnUiThread(() -> Utils.fade(retry, progressBar, 300));
                e.printStackTrace();
            }
        }, false);
    }


    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * ((float) displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public void onLoginSuccess(User user) {
        ((UInitializrApplication) getApplication()).setUser(user);
        getSignalManager().sendMainSignal(new Signal("login", Signal.SIGNAL_LOGIN, user));
        if (isStart)
            goToMainActivity();
        else
            finish();
    }
}

package ir.markazandroid.uinitializr.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import ir.markazandroid.uinitializr.UInitializrApplication;
import ir.markazandroid.uinitializr.signal.Signal;
import ir.markazandroid.uinitializr.signal.SignalManager;
import ir.markazandroid.uinitializr.signal.SignalReceiver;


/**
 * Coded by Ali on 11/08/2017.
 */

public class ButtonHandler implements SignalReceiver {
    boolean onParent = false;
    private View button;
    private ProgressBar progressBar;
    private ViewGroup parent;
    private Context context;
    private Handler handler;
    private SignalManager signalManager;

    public ButtonHandler(Context context, View button, int color, boolean onParent) {
        this.button = button;
        this.context = context;
        this.onParent = onParent;
        parent = (ViewGroup) button.getParent();
        progressBar = new ProgressBar(context);
        progressBar.setLayoutParams(button.getLayoutParams());
        if (color > 0) {
            Drawable drawable = progressBar.getIndeterminateDrawable();
            drawable.mutate();
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            progressBar.setIndeterminateDrawable(drawable);
        }
        handler = new Handler(context.getMainLooper());
        signalManager = ((UInitializrApplication) context.getApplicationContext()).getSignalManager();
    }

    public void click() {
        parent.removeView(button);
        parent.addView(progressBar);
        if (onParent) parent.setClickable(false);
        signalManager.addReceiver(this);
    }

    @Override
    public boolean onSignal(Signal signal) {
        if (signal.getType() == Signal.SIGNAL_RESPONSE) {
            parent.removeView(progressBar);
            parent.addView(button);
            if (onParent) parent.setClickable(true);
            signalManager.removeReceiver(this);
            return true;
        }
        return false;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        signalManager.removeReceiver(this);
    }

    public void dispose() {
        signalManager.removeReceiver(this);
    }
}

package ir.markazandroid.uinitializr.fragment.abstracts;

import android.view.View;
import android.view.ViewGroup;

/**
 * Coded by Ali on 30/07/2017.
 */

public interface DynamicToolbar {
    void updateToolbar(View view);

    ViewGroup getContainer();
}

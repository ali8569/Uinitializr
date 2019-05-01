package ir.markazandroid.uinitializr.fragment.abstracts;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import ir.markazandroid.uinitializr.UInitializrApplication;
import ir.markazandroid.uinitializr.network.NetworkManager;
import ir.markazandroid.uinitializr.signal.Signal;
import ir.markazandroid.uinitializr.signal.SignalManager;
import ir.markazandroid.uinitializr.util.PreferencesManager;

/**
 * base fragment abstract class for extending by fragments that need to use
 * {@link NetworkManager} to perform network operations
 * and {@link SignalManager} to send signals
 * <p>
 * Coded by Ali on 06/11/2017.
 */

public abstract class BaseNetworkFragment extends Fragment {
    private NetworkManager networkManager;

    protected NetworkManager getNetworkManager() {
        if (networkManager == null) {
            networkManager = new NetworkManager.Builder()
                    .from(getActivity())
                    .tag(toString())
                    .build();
        }
        return networkManager;
    }

    protected SignalManager getSignalManager() {
        return ((UInitializrApplication) getActivity().getApplication()).getSignalManager();
    }

    protected PreferencesManager getPreferencesManager() {
        return ((UInitializrApplication) getActivity().getApplication()).getPreferencesManager();
    }


    /**
     * send view destroyed signal to cancel request callbacks in order to
     * to avoid {@link NullPointerException}
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getSignalManager().sendSignal(new Signal(toString(), Signal.SIGNAL_VIEW_DESTROYED));
    }

    public void runOnUiThread(Runnable r) {
        getActivity().runOnUiThread(r);
    }

    public void makeShortToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void makeLongToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

}

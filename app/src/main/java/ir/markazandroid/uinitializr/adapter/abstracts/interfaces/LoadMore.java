package ir.markazandroid.uinitializr.adapter.abstracts.interfaces;

/**
 * Coded by Ali on 30/08/2017.
 */

public interface LoadMore {
    int ITEMS = 0x00000000;
    int SPINNER = 0x10000000;

    void setIsLoading(boolean isLoading);

    boolean isLoading();
}

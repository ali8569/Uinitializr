package ir.markazandroid.uinitializr.adapter.abstracts.interfaces;

import android.support.v7.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

/**
 * Coded by Ali on 10/17/2018.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(RecyclerView.ViewHolder fromPosition, RecyclerView.ViewHolder toPosition);

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    List getData();

    default void moveItem(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(getData(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(getData(), i, i - 1);
            }
        }
    }
}

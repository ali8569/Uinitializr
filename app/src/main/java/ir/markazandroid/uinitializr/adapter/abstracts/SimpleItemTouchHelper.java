package ir.markazandroid.uinitializr.adapter.abstracts;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import ir.markazandroid.uinitializr.adapter.abstracts.interfaces.ItemTouchHelperAdapter;

/**
 * Coded by Ali on 10/17/2018.
 */
public abstract class SimpleItemTouchHelper<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V>
        implements ItemTouchHelperAdapter {


    @Override
    public void onItemDismiss(int position) {
        getData().remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder from, RecyclerView.ViewHolder to) {
        int fromPosition = from.getAdapterPosition();
        int toPosition = to.getAdapterPosition();
        moveItem(fromPosition, toPosition);
        changeItemMoved(from, to, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyListeners(fromPosition, toPosition);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        moveItem(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyItemChanged(fromPosition);
        notifyItemChanged(toPosition);
        notifyListeners(fromPosition, toPosition);
    }

    protected void changeItemMoved(RecyclerView.ViewHolder from, RecyclerView.ViewHolder to, int fromPosition, int toPosition) {
    }

    protected void notifyListeners(int fromPosition, int toPosition) {
    }

    public static void makeRecyclerViewTouchable(RecyclerView recyclerView, ItemTouchHelperAdapter adapter) {
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

}

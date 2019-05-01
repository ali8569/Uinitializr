package ir.markazandroid.uinitializr.adapter.abstracts;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import ir.markazandroid.uinitializr.adapter.abstracts.interfaces.LoadMore;
import ir.markazandroid.uinitializr.adapter.abstracts.interfaces.OnLoadMoreListener;

/**
 * Coded by Ali on 30/08/2017.
 */

public class ScrollListener extends RecyclerView.OnScrollListener {

    private OnLoadMoreListener onLoadMoreListener;

    public ScrollListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        LoadMore adapter = (LoadMore) recyclerView.getAdapter();
        int totalItemCount = recyclerView.getAdapter().getItemCount();
        int lastVisibleItem = manager.findLastVisibleItemPosition();
        if (!adapter.isLoading() && totalItemCount <= (lastVisibleItem + 1)) {
            // End has been reached
            // Do something
            Log.e(totalItemCount + "", lastVisibleItem + "");
            adapter.setIsLoading(true);
            onLoadMoreListener.onLoadMore(adapter);

        }
    }

}

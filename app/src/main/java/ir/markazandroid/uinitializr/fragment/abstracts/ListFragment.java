package ir.markazandroid.uinitializr.fragment.abstracts;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.markazandroid.uinitializr.R;
import ir.markazandroid.uinitializr.adapter.abstracts.ScrollListener;
import ir.markazandroid.uinitializr.adapter.abstracts.interfaces.LoadMore;
import ir.markazandroid.uinitializr.adapter.abstracts.interfaces.OnLoadMoreListener;


/**
 * class that extends {@link LoadingFragment} to use
 * {@link RecyclerView} as content
 */
public abstract class ListFragment extends LoadingFragment implements OnLoadMoreListener {


    protected RecyclerView list;
    protected int limit;
    protected int offset;
    protected ScrollListener scrollListener;
    protected LoadMore loadMoreAdapter;
    protected View emptyList;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        scrollListener = new ScrollListener(this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected View initParentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View main;
        if (getListManualLayoutId() != 0) {
            main = inflater.inflate(getListManualLayoutId(), container, false);
            ViewGroup listContainer = main.findViewById(getListContainerId());
            View listView = inflater.inflate(R.layout.fragment_list, listContainer, false);
            listContainer.addView(listView);
        } else
            main = inflater.inflate(R.layout.fragment_list, container, false);

        emptyList = main.findViewById(R.id.empty);
        list = main.findViewById(getContentViewId());
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        return main;
    }

    @Override
    public void onLoadMore(LoadMore adapter) {
        loadMoreAdapter = adapter;
        offset += limit;
        loadMoreData();
    }

    public void enableEndless() {
        list.removeOnScrollListener(scrollListener);
        list.addOnScrollListener(scrollListener);
    }

    public void disableEndless() {
        list.removeOnScrollListener(scrollListener);
    }


    @Override
    protected void loadingFailed() {
        super.loadingFailed();
        endLoadingMore();
        emptyList.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void endProgress() {
        super.endProgress();
        endLoadingMore();
        emptyList.setVisibility(View.INVISIBLE);
    }

    protected void endLoadingMore() {
        if (loadMoreAdapter != null) {
            loadMoreAdapter.setIsLoading(false);
        }
    }

    @Override
    protected int getContentViewId() {
        return R.id.list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        list.removeOnScrollListener(scrollListener);
        scrollListener = null;
        loadMoreAdapter = null;
    }

    protected void emptyList() {
        loadingFailed();
        retry.setVisibility(View.INVISIBLE);
        emptyList.setVisibility(View.VISIBLE);
    }

    /**
     * for putting this view in parent view
     *
     * @return
     */
    protected int getListContainerId() {
        return 0;
    }

    /**
     * for putting this view in parent view
     *
     * @return
     */
    protected int getListManualLayoutId() {
        return 0;
    }


    protected void loadMoreData() {
    }
}

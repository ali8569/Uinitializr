package ir.markazandroid.uinitializr.fragment.abstracts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import ir.markazandroid.uinitializr.R;
import ir.markazandroid.uinitializr.util.Utils;

/**
 * base abstract LoadingFragment class for fragments that need to load content and
 * implement user friendly interface to handle loading results
 * <p>
 * Coded by Ali on 14/12/2017.
 */
public abstract class LoadingFragment extends BaseNetworkFragment {

    protected ProgressBar progressBar;
    protected SwipeRefreshLayout refreshLayout;
    protected View content;
    protected DynamicToolbar dynamicToolbar;
    protected View toolbarItems;
    protected View retry;

    protected boolean isLoading = false;
    protected boolean isRefreshing = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof DynamicToolbar)
            dynamicToolbar = (DynamicToolbar) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = initParentView(inflater, container, savedInstanceState);
        content = view.findViewById(getContentViewId());
        progressBar = view.findViewById(getProgressBarViewId());
        refreshLayout = view.findViewById(getRefreshLayoutViewId());
        retry = view.findViewById(getRetryViewId());

        toolbarItems = initToolbar(inflater);
        if (dynamicToolbar != null) dynamicToolbar.updateToolbar(toolbarItems);
        onViewInitialized(view);
        return view;
    }


    /**
     * called when the view initialization is finished
     * it is called in the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * you can override it to do your stuff
     * it has empty implementation
     *
     * @param viewToDisplay the main view to display
     *                      ( view object that {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} returns)
     */
    protected void onViewInitialized(View viewToDisplay) {

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry();
            }
        });

        if (refreshLayout != null)
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    isRefreshing = true;
                    refresh();
                }
            });
        init();
        initToolbarViews();
    }

    /**
     * called in {@link #onActivityCreated(Bundle)}
     */
    protected void init() {
        startProgress();
        setData();
    }

    /**
     * must be called in {@link #setData()} if the content loading was successful
     */
    protected void endProgress() {
        retry.setVisibility(View.INVISIBLE);
        Utils.fade(content, progressBar, 300);
        if (refreshLayout != null)
            refreshLayout.setRefreshing(false);
        isRefreshing = false;
        isLoading = false;
    }

    /**
     * called in {@link #init()} before calling {@link #setData()}
     * you should not call it manually anywhere
     */
    protected void startProgress() {
        isLoading = true;
        retry.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        content.setVisibility(View.INVISIBLE);
    }

    /**
     * must be called in {@link #setData()} if the content loading failed
     * if user was refreshing, the content wont get disappeared
     * if content was getting loaded and not refreshed retry will appear
     */
    protected void loadingFailed() {
        if (!isRefreshing) {
            content.setVisibility(View.INVISIBLE);
            retry.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.INVISIBLE);
        if (refreshLayout != null)
            refreshLayout.setRefreshing(false);
        isRefreshing = false;
        isLoading = false;
    }

    protected void retry() {
        init();
    }

    protected void refresh() {
        setData();
    }

    /**
     * it simply calls {@link SwipeRefreshLayout#setEnabled(boolean)}
     *
     * @param isEnabled false to disable the Swipe To Refresh gesture
     */

    protected void setSwipeToRefreshEnabled(boolean isEnabled) {
        if (refreshLayout != null)
            refreshLayout.setEnabled(isEnabled);
    }

    protected void initToolbarViews() {

    }

    protected View initToolbar(LayoutInflater inflater) {
        return null;
    }

    /**
     * default is {@link R.id#content}
     *
     * @return the id of content view to use in {@link View#findViewById(int)}
     * that is called in method {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * to the returned {@link View} of method {@link #initParentView(LayoutInflater, ViewGroup, Bundle)}
     */
    protected int getContentViewId() {
        return 0;
    }

    /**
     * default is {@link R.id#progressbar}
     *
     * @return the id of content view to use in {@link View#findViewById(int)}
     * that is called in method {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * to the returned {@link View} of method {@link #initParentView(LayoutInflater, ViewGroup, Bundle)}
     */
    protected int getProgressBarViewId() {
        return R.id.progressbar;
    }

    /**
     * default is {@link R.id#retry}
     *
     * @return the id of content view to use in {@link View#findViewById(int)}
     * that is called in method {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * to the returned {@link View} of method {@link #initParentView(LayoutInflater, ViewGroup, Bundle)}
     */
    protected int getRetryViewId() {
        return R.id.retry;
    }

    /**
     * default is {@link R.id#refresh_layout}
     *
     * @return the id of content view to use in {@link View#findViewById(int)}
     * that is called in method {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * to the returned {@link View} of method {@link #initParentView(LayoutInflater, ViewGroup, Bundle)}
     */
    protected int getRefreshLayoutViewId() {
        return R.id.refresh_layout;
    }

    /**
     * implements must retrieve data and load it into view in this method
     * after loading one of methods {@link #endProgress()} or {@link #loadingFailed()}
     * must be called
     */
    protected abstract void setData();

    /**
     * this method must init the view of fragment
     *
     * @param inflater           passed from {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @param container          passed from {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @param savedInstanceState passed from {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @return the main view to display in the fragment
     */
    protected abstract View initParentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onDestroy() {
        super.onDestroy();
        dynamicToolbar = null;
    }

}

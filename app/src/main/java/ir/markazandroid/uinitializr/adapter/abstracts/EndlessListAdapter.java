package ir.markazandroid.uinitializr.adapter.abstracts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import ir.markazandroid.uinitializr.adapter.abstracts.interfaces.LoadMore;

/**
 * Coded by Ali on 26/01/2018.
 */

abstract class EndlessListAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> implements LoadMore {

    protected boolean isLoading = false;
    protected Context context;

    public EndlessListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
        if (isLoading) {
            notifyItemInserted(getCount());
        } else notifyDataSetChanged();
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SPINNER) {
            View spinner = new ProgressBar(context);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            spinner.setLayoutParams(params);
            return initViewHolder(spinner);
        }
        return createView(parent, viewType);
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        if (getItemViewType(position) != SPINNER)
            bindView(holder, position);

    }

    @Override
    public int getItemCount() {
        if (isLoading) return getCount() + 1;
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoading && position >= getCount()) return SPINNER;
        else return getViewType(position);
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    protected int getViewType(int position) {
        return ITEMS;
    }

    abstract T createView(ViewGroup parent, int viewType);

    abstract void bindView(T holder, int position);

    abstract int getCount();

    abstract T initViewHolder(View view);
}

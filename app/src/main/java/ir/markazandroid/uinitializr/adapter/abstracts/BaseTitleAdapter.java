package ir.markazandroid.uinitializr.adapter.abstracts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Coded by Ali on 5/11/2018.
 */
public abstract class BaseTitleAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    private static final int TITLE = 1;
    private int titleResId;

    protected Context context;

    public BaseTitleAdapter(Context context, int titleResId) {
        this.titleResId = titleResId;
        this.context = context;
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TITLE) {
            return initViewHolder(LayoutInflater.from(context).inflate(titleResId, parent, false));
        }
        return createView(parent, viewType);
    }


    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {
        if (getItemViewType(position) != TITLE)
            bindView(holder, position - 1);
        else bindTitle(holder);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TITLE;
        else return getViewType(position);
    }

    @Override
    public int getItemCount() {
        return getCount() + 1;
    }


    protected int getViewType(int position) {
        return super.getItemViewType(position);
    }

    protected abstract T createView(ViewGroup parent, int viewType);

    protected abstract void bindTitle(T holder);

    protected abstract void bindView(T holder, int position);

    protected abstract int getCount();

    protected abstract T initViewHolder(View view);


}

package ru.p3tr0vich.calibration.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.p3tr0vich.calibration.models.BaseRecord;

public abstract class BaseAdapter<T extends BaseRecord> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_FOOTER = 2;

    private static final long HEADER_ID = Long.MAX_VALUE;
    private static final long FOOTER_ID = Long.MIN_VALUE;

    public static final int HEADER_POSITION = 0;

    private final List<T> mRecords;

    @LayoutRes
    private int mHeaderLayout;
    @LayoutRes
    private int mFooterLayout;

    public BaseAdapter(@LayoutRes int headerLayout, @LayoutRes int footerLayout) {
        super();
        setHasStableIds(true);

        mHeaderLayout = headerLayout;
        mFooterLayout = footerLayout;

        mRecords = new ArrayList<>();
        if (hasHeader()) mRecords.add(null);
        if (hasFooter()) mRecords.add(null);
    }

    public int getFooterType() {
        return TYPE_FOOTER;
    }

    public boolean hasHeader() {
        return mHeaderLayout > 0;
    }

    private boolean hasFooter() {
        return mFooterLayout > 0;
    }

    public void swapRecords(@Nullable List<T> records) {
        mRecords.clear();

        if (hasHeader()) mRecords.add(null);

        if (records != null)
            mRecords.addAll(records);

        if (hasFooter()) mRecords.add(null);

        notifyDataSetChanged();
    }

    public int findPositionById(long id) {
        for (int i = (hasHeader() ? 1 : 0), count = mRecords.size() - (hasFooter() ? 1 : 0); i < count; i++)
            if (mRecords.get(i).getId() == id) return i;

        return -1;
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    public List<T> getRecords() {
        return mRecords;
    }

    @Override
    public long getItemId(int position) {
        if (hasHeader() && position == HEADER_POSITION) return HEADER_ID;
        if (hasFooter() && position == mRecords.size() - 1) return FOOTER_ID;

        return getRecords().get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader() && position == HEADER_POSITION) return TYPE_HEADER;
        if (hasFooter() && position == mRecords.size() - 1) return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    @NonNull
    public abstract ItemViewHolder getItemViewHolder(ViewGroup parent);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                return getItemViewHolder(parent);
            case TYPE_HEADER:
                return new HeaderOrFooterViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(mHeaderLayout, parent, false));
            case TYPE_FOOTER:
                return new HeaderOrFooterViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(mFooterLayout, parent, false));
            default:
                throw new IllegalArgumentException("onCreateViewHolder: wrong viewType == " + viewType);
        }
    }

    public static class ItemViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
        private final T mBinding;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        T getBinding() {
            return mBinding;
        }
    }

    private static class HeaderOrFooterViewHolder extends RecyclerView.ViewHolder {
        HeaderOrFooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
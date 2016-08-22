package ru.p3tr0vich.calibration.adapters;

import android.support.annotation.IntDef;
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

    private final View.OnClickListener mOnClickListener;

    @HeaderFooter
    private int mShowHeader;
    @HeaderFooter
    private int mShowFooter;

    @IntDef({HF_HIDE, HF_SHOW})
    public @interface HeaderFooter {
    }

    private static final int HF_HIDE = 0;
    private static final int HF_SHOW = 1;

    public BaseAdapter(View.OnClickListener onClickListener,
                       boolean showHeader, boolean showFooter) {
        super();
        setHasStableIds(true);

        setShowHeader(showHeader);
        setShowFooter(showFooter);

        mRecords = new ArrayList<>();
        if (isShowHeader()) mRecords.add(null);
        if (isShowFooter()) mRecords.add(null);

        mOnClickListener = onClickListener;
    }

    public int getFooterType() {
        return TYPE_FOOTER;
    }

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public boolean isShowHeader() {
        return mShowHeader == HF_SHOW;
    }

    private void setShowHeader(boolean showHeader) {
        mShowHeader = showHeader ? HF_SHOW : HF_HIDE;
    }

    public boolean isShowFooter() {
        return mShowFooter == HF_SHOW;
    }

    private void setShowFooter(boolean showFooter) {
        mShowFooter = showFooter ? HF_SHOW : HF_HIDE;
    }

    public void swapRecords(@Nullable List<T> records) {
        mRecords.clear();

        if (isShowHeader()) mRecords.add(null);

        if (records != null)
            mRecords.addAll(records);

        if (isShowFooter()) mRecords.add(null);

        notifyDataSetChanged();
    }

    public int findPositionById(long id) {
        for (int i = mShowHeader, count = mRecords.size() - mShowFooter; i < count; i++)
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
        if (isShowHeader() && position == HEADER_POSITION) return HEADER_ID;
        if (isShowFooter() && position == mRecords.size() - 1) return FOOTER_ID;

        return getRecords().get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowHeader() && position == HEADER_POSITION) return TYPE_HEADER;
        if (isShowFooter() && position == getRecords().size() - 1) return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    @NonNull
    public abstract RecyclerView.ViewHolder getItemViewHolder(ViewGroup parent);

    @LayoutRes
    public abstract int getHeaderLayout();

    @LayoutRes
    public abstract int getFooterLayout();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                return getItemViewHolder(parent);
            case TYPE_HEADER:
                return new HeaderViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(getHeaderLayout(), parent, false));
            case TYPE_FOOTER:
                return new HeaderViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(getFooterLayout(), parent, false));
            default:
                throw new IllegalArgumentException("onCreateViewHolder: wrong viewType == " + viewType);
        }
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
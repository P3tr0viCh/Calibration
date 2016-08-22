package ru.p3tr0vich.calibration.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.p3tr0vich.calibration.R;
import ru.p3tr0vich.calibration.databinding.ListItemScaleBinding;
import ru.p3tr0vich.calibration.models.ScaleRecord;

public class ScalesAdapter extends BaseAdapter<ScaleRecord> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_FOOTER = 2;

    public ScalesAdapter(View.OnClickListener onClickListener, boolean showHeader, boolean showFooter) {
        super(onClickListener, showHeader, showFooter);
    }

    @Override
    public int getFooterType() {
        return TYPE_FOOTER;
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowHeader() && position == HEADER_POSITION) return TYPE_HEADER;
        if (isShowFooter() && position == getRecords().size() - 1) return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                return new ScaleItemViewHolder(
                        ListItemScaleBinding.inflate(
                                LayoutInflater.from(parent.getContext()), parent, false).getRoot());
            case TYPE_HEADER:
                return new HeaderViewHolder(
                        LayoutInflater.from(parent.getContext()).
                                inflate(R.layout.partial_scales_recycler_view_header, parent, false));
            case TYPE_FOOTER:
                return new HeaderViewHolder(
                        LayoutInflater.from(parent.getContext()).
                                inflate(R.layout.partial_scales_recycler_view_footer, parent, false));
            default:
                throw new IllegalArgumentException("onCreateViewHolder: wrong viewType == " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ScaleItemViewHolder) {
            final ScaleRecord record = getRecords().get(position);

            final ListItemScaleBinding binding = ((ScaleItemViewHolder) holder).getBinding();

            binding.setScaleRecord(record);

            binding.btnMenu.setTag(record.getId());
            binding.btnMenu.setOnClickListener(getOnClickListener());
        }
    }

    private static class ScaleItemViewHolder extends RecyclerView.ViewHolder {

        private final ListItemScaleBinding mBinding;

        ScaleItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        ListItemScaleBinding getBinding() {
            return mBinding;
        }
    }
}
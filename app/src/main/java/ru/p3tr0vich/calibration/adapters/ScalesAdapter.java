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

    public ScalesAdapter(View.OnClickListener onClickListener, boolean showHeader, boolean showFooter) {
        super(onClickListener, showHeader, showFooter);
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder getItemViewHolder(ViewGroup parent) {
        return new ScaleItemViewHolder(
                ListItemScaleBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false).getRoot());
    }

    @Override
    public int getHeaderLayout() {
        return R.layout.partial_scales_recycler_view_header;
    }

    @Override
    public int getFooterLayout() {
        return R.layout.partial_scales_recycler_view_footer;
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
package ru.p3tr0vich.calibration.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.p3tr0vich.calibration.presenters.PopupMenuPresenter;
import ru.p3tr0vich.calibration.R;
import ru.p3tr0vich.calibration.databinding.ListItemScaleBinding;
import ru.p3tr0vich.calibration.models.ScaleRecord;

public class ScalesAdapter extends BaseAdapter<ScaleRecord> {

    private PopupMenuPresenter.OnMenuItemClickListener<ScaleRecord> mOnMenuItemClickListener;

    public ScalesAdapter(@NonNull PopupMenuPresenter.OnMenuItemClickListener<ScaleRecord> onMenuItemClickListener,
                         @LayoutRes int headerLayout, @LayoutRes int footerLayout) {
        super(headerLayout, footerLayout);
        mOnMenuItemClickListener = onMenuItemClickListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ScaleItemViewHolder) {
            final ScaleRecord record = getRecords().get(position);

            ListItemScaleBinding binding = ((ScaleItemViewHolder) holder).getBinding();

            binding.setScaleRecord(record);
            binding.setPopupMenuPresenter(new PopupMenuPresenter<>(R.menu.menu_scales, mOnMenuItemClickListener));
        }
    }

    @NonNull
    @Override
    public ItemViewHolder getItemViewHolder(ViewGroup parent) {
        return new ScaleItemViewHolder(
                ListItemScaleBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false).getRoot());
    }

    private static class ScaleItemViewHolder extends ItemViewHolder<ListItemScaleBinding> {
        ScaleItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
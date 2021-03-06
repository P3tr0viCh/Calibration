package ru.p3tr0vich.calibration;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

abstract class OnRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private final int mScrollThreshold;

    private int mOffset = 0;

    public abstract void onScrollUp();

    public abstract void onScrollDown();

    OnRecyclerViewScrollListener(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition() == 0)
                onScrollDown();
            else if (((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findLastCompletelyVisibleItemPosition() ==
                    (recyclerView.getAdapter().getItemCount() - 1))
                onScrollUp();
            else if (Math.abs(mOffset) > mScrollThreshold)
                if (mOffset > 0)
                    onScrollUp();
                else
                    onScrollDown();
            mOffset = 0;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        mOffset += dy;
    }
}
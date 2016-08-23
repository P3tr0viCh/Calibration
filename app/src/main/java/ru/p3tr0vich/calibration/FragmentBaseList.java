package ru.p3tr0vich.calibration;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import ru.p3tr0vich.calibration.adapters.BaseAdapter;
import ru.p3tr0vich.calibration.models.BaseRecord;
import ru.p3tr0vich.calibration.utils.Utils;
import ru.p3tr0vich.calibration.utils.UtilsLog;

public abstract class FragmentBaseList<T extends BaseRecord> extends FragmentBase {

    private static final String TAG = "FragmentBaseList";

    private static final boolean LOG_ENABLED = true;

    private RecyclerView mRecyclerView;

    private BaseAdapter<T> mRecyclerViewAdapter;

    private ProgressWheel mProgressWheel;
    private TextView mTextNoRecords;

    private FloatingActionButton mFloatingActionButton;

    private final Handler mHandler = new Handler();

    private long mIdForScroll = -1;

    private Snackbar mSnackbar = null;
    private final Snackbar.Callback mSnackBarCallback = new Snackbar.Callback() {
        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            // Workaround for bug

            if (event == DISMISS_EVENT_SWIPE)
                setFabVisibleTranslate(true);
        }
    };

    private static class AnimationDuration {
        final int startDelayFab;
        final int translateFabShow;
        final int translateFabHide;
        final int delayedTimeShowNoRecords;
        final int delayedTimeShowProgressWheel;

        AnimationDuration(@NonNull Context context) {
            startDelayFab = context.getResources().getInteger(R.integer.animation_start_delay_fab);
            translateFabShow = context.getResources().getInteger(R.integer.animation_duration_fab_show);
            translateFabHide = context.getResources().getInteger(R.integer.animation_duration_fab_hide);
            delayedTimeShowNoRecords = context.getResources().getInteger(R.integer.delayed_time_show_no_records);
            delayedTimeShowProgressWheel = context.getResources().getInteger(R.integer.delayed_time_show_progress_wheel);
        }
    }

    private AnimationDuration mAnimationDuration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAnimationDuration = new AnimationDuration(getContext());

        if (LOG_ENABLED) UtilsLog.d(TAG, "onCreate", "savedInstanceState " +
                (savedInstanceState == null ? "=" : "!") + "= null");
    }

    @LayoutRes
    public abstract int getFragmentLayout();

    public abstract void onFloatingActionButtonClick();

    @NonNull
    public abstract BaseAdapter<T> createRecyclerViewAdapter(boolean isPhone);

    public void onRecyclerViewScrollUp() {
        setFabVisibleTranslate(false);
    }

    public void onRecyclerViewScrollDown() {
        setFabVisibleTranslate(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (LOG_ENABLED) UtilsLog.d(TAG, "onCreateView");

        final boolean isPhone = Utils.isPhone();

        View view = inflater.inflate(getFragmentLayout(), container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecorationFueling itemDecoration = new DividerItemDecorationFueling(getContext());
        if (!isPhone) itemDecoration.setFooterType(mRecyclerViewAdapter.getFooterType());
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerViewAdapter = createRecyclerViewAdapter(isPhone);

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        if (isPhone)
            mRecyclerView.addOnScrollListener(new OnRecyclerViewScrollListener(
                    getResources().getDimensionPixelOffset(R.dimen.recycler_view_scroll_threshold)) {

                @Override
                public void onScrollUp() {
                    onRecyclerViewScrollUp();
                }

                @Override
                public void onScrollDown() {
                    if (mSnackbar == null || !mSnackbar.isShown())
                        onRecyclerViewScrollDown();
                }
            });

        mProgressWheel = (ProgressWheel) view.findViewById(R.id.progress_wheel);
        mProgressWheel.setVisibility(View.GONE);

        mTextNoRecords = (TextView) view.findViewById(R.id.text_no_records);
        mTextNoRecords.setVisibility(View.GONE);

        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFloatingActionButtonClick();
            }
        });
        mFloatingActionButton.setScaleX(0.0f);
        mFloatingActionButton.setScaleY(0.0f);

        return view;
    }

    private final Runnable mRunnableShowNoRecords = new Runnable() {
        @Override
        public void run() {
            Utils.setViewVisibleAnimate(mTextNoRecords, true);
        }
    };

    private final Runnable mRunnableShowProgressWheelFueling = new Runnable() {
        @Override
        public void run() {
            Utils.setViewVisibleAnimate(mProgressWheel, true);
        }
    };

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnableShowNoRecords);
        mHandler.removeCallbacks(mRunnableShowProgressWheelFueling);

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        setFabVisibleScale(true);
    }

    @Override
    public void onPause() {
        setFabVisibleScale(false);
        super.onPause();
    }

    public void setFabVisibleScale(boolean visible) {
        final float value = visible ? 1.0f : 0.0f;
        mFloatingActionButton.animate()
                .setStartDelay(mAnimationDuration.startDelayFab).scaleX(value).scaleY(value);
    }

    private void setFabVisibleTranslate(final boolean visible) {
        final float value = visible ? 0.0f : mFloatingActionButton.getHeight();
        mFloatingActionButton.animate().translationY(value);

//        if (mFloatingActionButton.getVisibility() == View.VISIBLE && visible) return;
//
//        ValueAnimator valueAnimator = ValueAnimator.ofInt(
//                (int) mFloatingActionButton.getTranslationY(),
//                visible ? 0 : mFloatingActionButton.getHeight());
//        valueAnimator
//                .setDuration(visible ?
//                        mAnimationDuration.translateFabShow:
//                        mAnimationDuration.translateFabHide)
//                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    int translationY;
//
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        translationY = (int) animation.getAnimatedValue();
//
//                        mFloatingActionButton.setTranslationY(translationY);
//                    }
//                });
//        valueAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                if (!visible) mFloatingActionButton.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationStart(Animator animation) {
//                if (visible) mFloatingActionButton.setVisibility(View.VISIBLE);
//            }
//        });
//
//        valueAnimator.start();
    }

    public void setLoading(boolean loading) {
        mHandler.removeCallbacks(mRunnableShowProgressWheelFueling);
        if (loading)
            mHandler.postDelayed(mRunnableShowProgressWheelFueling, mAnimationDuration.delayedTimeShowProgressWheel);
        else
            Utils.setViewVisibleAnimate(mProgressWheel, false);
    }

    private void setTextNoRecordsVisible(boolean visible) {
        mHandler.removeCallbacks(mRunnableShowNoRecords);
        if (visible)
            mHandler.postDelayed(mRunnableShowNoRecords, mAnimationDuration.delayedTimeShowNoRecords);
        else
            mTextNoRecords.setVisibility(View.GONE);
    }

    private LinearLayoutManager getRecyclerViewLayoutManager() {
        return (LinearLayoutManager) mRecyclerView.getLayoutManager();
    }

    private boolean isItemVisible(int position) {
        final int firstVisibleItem = getRecyclerViewLayoutManager()
                .findFirstCompletelyVisibleItemPosition();
        final int lastVisibleItem = getRecyclerViewLayoutManager()
                .findLastCompletelyVisibleItemPosition();

        return firstVisibleItem != RecyclerView.NO_POSITION &&
                lastVisibleItem != RecyclerView.NO_POSITION &&
                position > firstVisibleItem && position < lastVisibleItem;
    }

    private void scrollToPosition(int position) {
        if (position < 0) return;

        if (mRecyclerViewAdapter.isShowHeader() && position == BaseAdapter.HEADER_POSITION + 1)
            position = BaseAdapter.HEADER_POSITION;

        if (isItemVisible(position)) return;

        getRecyclerViewLayoutManager().scrollToPositionWithOffset(position, 0);
    }

    public void setIdForScroll(long idForScroll) {
        mIdForScroll = idForScroll;
    }

    public void swapRecords(@Nullable List<T> records) {
        setTextNoRecordsVisible(records == null || records.isEmpty());

        mRecyclerViewAdapter.swapRecords(records);

        if (mIdForScroll != -1) {
            scrollToPosition(mRecyclerViewAdapter.findPositionById(mIdForScroll));
            mIdForScroll = -1;
        }
    }
}
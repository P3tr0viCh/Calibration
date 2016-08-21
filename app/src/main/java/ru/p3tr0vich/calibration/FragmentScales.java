package ru.p3tr0vich.calibration;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;

import ru.p3tr0vich.calibration.adapters.BaseAdapter;
import ru.p3tr0vich.calibration.adapters.ScalesAdapter;
import ru.p3tr0vich.calibration.helpers.ContentProviderHelper;
import ru.p3tr0vich.calibration.helpers.DatabaseHelper;
import ru.p3tr0vich.calibration.models.ScaleRecord;
import ru.p3tr0vich.calibration.utils.Utils;
import ru.p3tr0vich.calibration.utils.UtilsLog;

public class FragmentScales extends FragmentBaseList<ScaleRecord> implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "FragmentScales";

    private static final boolean LOG_ENABLED = true;

    private static final int SCALES_CURSOR_LOADER_ID = 0;

    @Override
    public int getTitleId() {
        return R.string.title_scales;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_scales;
    }

    @NonNull
    @Override
    public BaseAdapter createRecyclerViewAdapter(boolean isPhone) {
        return new ScalesAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toast(TAG + ": btn");
            }
        }, isPhone, !isPhone);
    }

    @Override
    public void onFloatingActionButtonClick() {
        ActivityDialog.start(getActivity(), ActivityDialog.DIALOG_SCALE_CHANGE, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (LOG_ENABLED) UtilsLog.d(TAG, "onActivityCreated", "savedInstanceState " +
                (savedInstanceState == null ? "=" : "!") + "= null");

        getLoaderManager().initLoader(SCALES_CURSOR_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case SCALES_CURSOR_LOADER_ID:
                return new ScalesCursorLoader(getContext());
            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (LOG_ENABLED) UtilsLog.d(TAG, "onLoadFinished");

        switch (loader.getId()) {
            case SCALES_CURSOR_LOADER_ID:
                swapRecords(DatabaseHelper.getScaleRecords(data));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (LOG_ENABLED) UtilsLog.d(TAG, "onLoaderReset");

        switch (loader.getId()) {
            case SCALES_CURSOR_LOADER_ID:
                swapRecords(null);
        }
    }

    private static class ScalesCursorLoader extends CursorLoader {

        ScalesCursorLoader(Context context) {
            super(context);
        }

        @Override
        public Cursor loadInBackground() {
            if (LOG_ENABLED) UtilsLog.d(TAG, "ScalesCursorLoader", "loadInBackground");

            BroadcastReceiverLoading.send(getContext(), true);

            try {
                return ContentProviderHelper.getScales(getContext());
            } finally {
                BroadcastReceiverLoading.send(getContext(), false);
            }
        }
    }
}
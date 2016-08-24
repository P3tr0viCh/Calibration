package ru.p3tr0vich.calibration;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.MenuItem;

import ru.p3tr0vich.calibration.adapters.BaseAdapter;
import ru.p3tr0vich.calibration.adapters.ScalesAdapter;
import ru.p3tr0vich.calibration.helpers.ContentProviderHelper;
import ru.p3tr0vich.calibration.helpers.DatabaseHelper;
import ru.p3tr0vich.calibration.models.ScaleRecord;
import ru.p3tr0vich.calibration.observers.DatabaseObserver;
import ru.p3tr0vich.calibration.presenters.PopupMenuPresenter;
import ru.p3tr0vich.calibration.utils.Utils;
import ru.p3tr0vich.calibration.utils.UtilsLog;

import static ru.p3tr0vich.calibration.helpers.ContentProviderHelper.DATABASE_SCALES_ITEM;

public class FragmentScales extends FragmentBaseList<ScaleRecord> implements
        PopupMenuPresenter.OnMenuItemClickListener<ScaleRecord>,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "FragmentScales";

    private static final boolean LOG_ENABLED = true;

    private static final int SCALES_CURSOR_LOADER_ID = 0;

    DatabaseObserverScales mDatabaseObserverScales;

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
    public BaseAdapter<ScaleRecord> createRecyclerViewAdapter(boolean isPhone) {
        return new ScalesAdapter(this,
                isPhone ? R.layout.partial_scales_recycler_view_header : 0,
                isPhone ? 0 : R.layout.partial_scales_recycler_view_footer);
    }

    @Override
    public void onFloatingActionButtonClick() {
        ActivityDialog.start(getActivity(), ActivityDialog.DIALOG_SCALE_CHANGE, null);
    }

    private boolean deleteRecord(@NonNull ScaleRecord record) {
        if (ContentProviderHelper.deleteRecord(getContext(), record))
            return true;
        else {
            Utils.toast(R.string.message_error_delete_record);
            return false;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item, @Nullable ScaleRecord record) {
        if (record == null) return false;

        switch (item.getItemId()) {
            case R.id.action_fueling_update:
                ActivityDialog.start(getActivity(), ActivityDialog.DIALOG_SCALE_CHANGE, record.putToBundle());

                return true;
            case R.id.action_fueling_delete:
                deleteRecord(record);

//                mDeletedFuelingRecord = fuelingRecord;
//
//                if (markRecordAsDeleted(fuelingRecord)) {
//                    mSnackbar = Snackbar
//                            .make(mLayoutMain, R.string.message_record_deleted,
//                                    Snackbar.LENGTH_LONG)
//                            .setAction(R.string.dialog_btn_cancel, mUndoClickListener)
//                            .setCallback(mSnackBarCallback);
//                    mSnackbar.show();
//                }

                return true;
            default:
                return false;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (LOG_ENABLED) UtilsLog.d(TAG, "onActivityCreated", "savedInstanceState " +
                (savedInstanceState == null ? "=" : "!") + "= null");

        getLoaderManager().initLoader(SCALES_CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseObserverScales = new DatabaseObserverScales();
        mDatabaseObserverScales.register(getContext());
    }

    @Override
    public void onDestroy() {
        mDatabaseObserverScales.unregister(getContext());
        super.onDestroy();
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

    private void updateList(long id) {
        if (id != -1) {
            setIdForScroll(id);
            getLoaderManager().getLoader(SCALES_CURSOR_LOADER_ID).forceLoad();
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

    private class DatabaseObserverScales extends DatabaseObserver {

        @Override
        public void onChange(@NonNull Uri uri, int uriMatch) {
            if (LOG_ENABLED)
                UtilsLog.d(TAG, "DatabaseObserverScales onChange", "uriMatch == " + uriMatch);

            switch (uriMatch) {
                case DATABASE_SCALES_ITEM:
                    updateList(ContentUris.parseId(uri));
            }
        }
    }
}
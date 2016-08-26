package ru.p3tr0vich.calibration.factories;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.support.v4.content.CursorLoader;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ru.p3tr0vich.calibration.BroadcastReceiverLoading;
import ru.p3tr0vich.calibration.helpers.ContentProviderHelper;
import ru.p3tr0vich.calibration.utils.UtilsLog;

public class CursorLoaderFactory {

    private static final String TAG = "CursorLoaderFactory";

    private static final boolean LOG_ENABLED = true;

    public interface Ids {

        @Retention(RetentionPolicy.SOURCE)
        @IntDef({SCALES})
        @interface Id {
        }

        int SCALES = 0;
    }

    private CursorLoaderFactory() {
    }

    public static CursorLoader getCursorLoader(Context context, @Ids.Id int id) {
        switch (id) {
            case Ids.SCALES:
                return new ScalesCursorLoader(context);
            default:
                throw new IllegalArgumentException("Cursor loader bad ID");
        }
    }

    private static class BaseCursorLoader extends CursorLoader {

        public BaseCursorLoader(Context context, Uri uri, String[] projection, String selection,
                                String[] selectionArgs, String sortOrder) {
            super(context, uri, projection, selection, selectionArgs, sortOrder);
        }

        @Override
        public Cursor loadInBackground() {
            if (LOG_ENABLED) UtilsLog.d(TAG, "BaseCursorLoader", "loadInBackground");

            BroadcastReceiverLoading.send(getContext(), getId(), true);

            try {
                return super.loadInBackground();
            } finally {
                BroadcastReceiverLoading.send(getContext(), getId(), false);
            }
        }
    }

    private static class ScalesCursorLoader extends BaseCursorLoader {

        ScalesCursorLoader(Context context) {
            super(context, ContentProviderHelper.URI_DATABASE_SCALES, null, null, null, null);
        }
    }
}
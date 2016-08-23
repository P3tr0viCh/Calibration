package ru.p3tr0vich.calibration.observers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import ru.p3tr0vich.calibration.helpers.ContentProviderHelper;
import ru.p3tr0vich.calibration.utils.UtilsLog;

/**
 * Наблюдатель за изменениями в базе данных.
 */
public abstract class DatabaseObserver extends ContentObserverBase {

    private static final String TAG = "DatabaseObserver";

    public void register(@NonNull Context context) {
        register(context, ContentProviderHelper.URI_DATABASE, true);
    }

    @Override
    public final void onChange(boolean selfChange, Uri changeUri) {
        UtilsLog.d(TAG, "onChange", "changeUri == " + changeUri);

        if (changeUri != null)
            onChange(changeUri, ContentProviderHelper.uriMatch(changeUri));
    }

    public abstract void onChange(@NonNull Uri uri, int uriMatch);
}
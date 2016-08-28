package ru.p3tr0vich.calibration.helpers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.p3tr0vich.calibration.BuildConfig;
import ru.p3tr0vich.calibration.models.BaseRecord;
import ru.p3tr0vich.calibration.models.ScaleRecord;
import ru.p3tr0vich.calibration.utils.UtilsLog;

@SuppressWarnings("TryFinallyCanBeTryWithResources")
// Try-with-resources requires API level 19 (current min is 17)
public class ContentProviderHelper extends ContentProvider {

    private static final String TAG = "ContentProviderHelper";

    private static class BaseUri {

        private static final String SCHEME = ContentResolver.SCHEME_CONTENT;
        private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";

        private BaseUri() {
        }

        public static Uri getUri(String path) {
            return new Uri.Builder()
                    .scheme(SCHEME)
                    .authority(AUTHORITY)
                    .path(path)
                    .build();
        }
    }

    private static class UriPath {
        private static final String DATABASE = "database";
        private static final String DATABASE_SCALES = DATABASE + "/scales";
        private static final String DATABASE_SCALES_ITEM = DATABASE_SCALES + "/#";

        private static final String PREFERENCES = "preferences";
        private static final String PREFERENCES_ITEM = PREFERENCES + "/*";
    }

    public static final Uri URI_DATABASE = BaseUri.getUri(UriPath.DATABASE);
    public static final Uri URI_DATABASE_SCALES = BaseUri.getUri(UriPath.DATABASE_SCALES);

    public static final Uri URI_PREFERENCES = BaseUri.getUri(UriPath.PREFERENCES);

    public static final int DATABASE = 10;
    public static final int DATABASE_SCALES = 11;
    public static final int DATABASE_SCALES_ITEM = 12;

    private static final int PREFERENCES = 20;
    private static final int PREFERENCES_ITEM = 21;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(BaseUri.AUTHORITY, UriPath.DATABASE, DATABASE);
        sURIMatcher.addURI(BaseUri.AUTHORITY, UriPath.DATABASE_SCALES, DATABASE_SCALES);
        sURIMatcher.addURI(BaseUri.AUTHORITY, UriPath.DATABASE_SCALES_ITEM, DATABASE_SCALES_ITEM);

        sURIMatcher.addURI(BaseUri.AUTHORITY, UriPath.PREFERENCES, PREFERENCES);
        sURIMatcher.addURI(BaseUri.AUTHORITY, UriPath.PREFERENCES_ITEM, PREFERENCES_ITEM);
    }

    private static final String CURSOR_DIR_BASE_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
            "/vnd." + BaseUri.AUTHORITY + ".";
    private static final String CURSOR_ITEM_BASE_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
            "/vnd." + BaseUri.AUTHORITY + ".";

    private static final String CURSOR_DIR_BASE_TYPE_DATABASE =
            CURSOR_DIR_BASE_TYPE + UriPath.DATABASE_SCALES;
    private static final String CURSOR_ITEM_BASE_TYPE_DATABASE =
            CURSOR_ITEM_BASE_TYPE + UriPath.DATABASE_SCALES;

    private static final String CURSOR_DIR_BASE_TYPE_PREFERENCES =
            CURSOR_DIR_BASE_TYPE + UriPath.PREFERENCES;
    private static final String CURSOR_ITEM_BASE_TYPE_PREFERENCES =
            CURSOR_ITEM_BASE_TYPE + UriPath.PREFERENCES;

    private DatabaseHelper mDatabaseHelper;
    private PreferencesHelper mPreferencesHelper;

    @Override
    public boolean onCreate() {
        assert getContext() != null;

        mDatabaseHelper = new DatabaseHelper(getContext());
        mPreferencesHelper = PreferencesHelper.getInstance(getContext());

        return true;
    }

    public static int uriMatch(@NonNull Uri uri) {
        return sURIMatcher.match(uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case DATABASE:
            case DATABASE_SCALES:
                return CURSOR_DIR_BASE_TYPE_DATABASE;
            case DATABASE_SCALES_ITEM:
                return CURSOR_ITEM_BASE_TYPE_DATABASE;

            case PREFERENCES:
                return CURSOR_DIR_BASE_TYPE_PREFERENCES;
            case PREFERENCES_ITEM:
                return CURSOR_ITEM_BASE_TYPE_PREFERENCES;
            default:
                UtilsLog.d(TAG, "getType", "sURIMatcher.match() == default, uri == " + uri);
                return null;
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        try {
            switch (sURIMatcher.match(uri)) {
                case DATABASE_SCALES:
                    return mDatabaseHelper.getScales();
                case DATABASE_SCALES_ITEM:
                    return mDatabaseHelper.getScale(ContentUris.parseId(uri));

                case PREFERENCES:
                    return null;// mPreferencesHelper.getPreferences();
                case PREFERENCES_ITEM:
                    return null;// mPreferencesHelper.getPreference(uri.getLastPathSegment());
                default:
                    UtilsLog.d(TAG, "query", "sURIMatcher.match() == default, uri == " + uri);
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            UtilsLog.d(TAG, "query", "exception == " + e.toString());
            return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        try {
            switch (sURIMatcher.match(uri)) {
                case DATABASE_SCALES:
                    return ContentUris.withAppendedId(URI_DATABASE_SCALES,
                            mDatabaseHelper.insert(DatabaseHelper.TableScales.NAME, values));
                default:
                    UtilsLog.d(TAG, "insert", "sURIMatcher.match() == default, uri == " + uri);
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            UtilsLog.d(TAG, "insert", "exception == " + e.toString());
            return null;
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        try {
            switch (sURIMatcher.match(uri)) {
                case DATABASE_SCALES:
                    return mDatabaseHelper.update(DatabaseHelper.TableScales.NAME, values, selection);
                case DATABASE_SCALES_ITEM:
                    return mDatabaseHelper.update(DatabaseHelper.TableScales.NAME, values, ContentUris.parseId(uri));

                case PREFERENCES:
                    return -1;// mPreferencesHelper.setPreferences(values, null);
                case PREFERENCES_ITEM:
                    return -1; // mPreferencesHelper.setPreferences(values, uri.getLastPathSegment());
                default:
                    UtilsLog.d(TAG, "update", "sURIMatcher.match() == default, uri == " + uri);
                    return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            UtilsLog.d(TAG, "update", "exception == " + e.toString());
            return -1;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        try {
            switch (sURIMatcher.match(uri)) {
                case DATABASE_SCALES:
                    return mDatabaseHelper.delete(DatabaseHelper.TableScales.NAME, selection);
                case DATABASE_SCALES_ITEM:
                    return mDatabaseHelper.delete(DatabaseHelper.TableScales.NAME, ContentUris.parseId(uri));
                default:
                    UtilsLog.d(TAG, "delete", "sURIMatcher.match() == default, uri == " + uri);
                    return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            UtilsLog.d(TAG, "delete", "exception == " + e.toString());
            return -1;
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        switch (sURIMatcher.match(uri)) {
            case DATABASE_SCALES:
                int numValues = values.length;

                SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

                try {
                    db.beginTransaction();
                    try {
                        for (ContentValues value : values)
                            mDatabaseHelper.insert(db, DatabaseHelper.TableScales.NAME, value);

                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                } finally {
                    db.close();
                }

                return numValues;
            default:
                UtilsLog.d(TAG, "bulkInsert", "sURIMatcher.match() == default, uri == " + uri);
                return -1;
        }
    }

//    public static Cursor getAll(@NonNull Context context, @NonNull DatabaseHelper.Filter filter) {
//        return context.getContentResolver().query(URI_DATABASE, null,
//                filter.getSelection(), null, null, null);
//    }

    @Nullable
    public static ScaleRecord getScale(@NonNull Context context, long id) {
        final Cursor cursor = context.getContentResolver().query(
                ContentUris.withAppendedId(URI_DATABASE_SCALES, id), null, null, null, null, null);

        if (cursor != null)
            try {
                if (cursor.moveToFirst())
                    return new ScaleRecord(cursor);
            } finally {
                cursor.close();
            }

        return null;
    }

//    @NonNull
//    public static List<FuelingRecord> getAllRecordsList(@NonNull Context context) {
//        List<FuelingRecord> fuelingRecords = new ArrayList<>();
//
//        final Cursor cursor = context.getContentResolver().query(URI_DATABASE, null,
//                DatabaseHelper.Where.RECORD_NOT_DELETED, null, null, null);
//
//        if (cursor != null)
//            try {
//                if (cursor.moveToFirst()) do
//                    fuelingRecords.add(DatabaseHelper.getFuelingRecord(cursor));
//                while (cursor.moveToNext());
//            } finally {
//                cursor.close();
//            }
//
//        return fuelingRecords;
//    }

//    public static void swapRecords(@NonNull Context context, @NonNull List<FuelingRecord> fuelingRecordList) {
//        UtilsLog.d(TAG, "swapRecords", "records count == " + fuelingRecordList.size());
//
//        context.getContentResolver().delete(URI_DATABASE, null, null); // delete all records
//
//        int size = fuelingRecordList.size();
//        if (size == 0) return;
//
//        FuelingRecord fuelingRecord;
//
//        ContentValues[] values = new ContentValues[size];
//
//        for (int i = 0; i < size; i++) {
//            fuelingRecord = fuelingRecordList.get(i);
//
//            values[i] = DatabaseHelper.getValues(
//                    fuelingRecord.getDateTime(),
//                    fuelingRecord.getDateTime(),
//                    fuelingRecord.getCost(),
//                    fuelingRecord.getVolume(),
//                    fuelingRecord.getTotal());
//        }
//
//        context.getContentResolver().bulkInsert(URI_DATABASE, values);
//
//        notifyChangeAfterUser(context, -1);
//    }

    private static Uri getUriForRecord(@NonNull BaseRecord record) {
        if (record instanceof ScaleRecord)
            return URI_DATABASE_SCALES;
        else
            throw new IllegalArgumentException("Wrong class -- " + record.getClass().getName());
    }

    public static boolean insertRecord(@NonNull Context context, @NonNull BaseRecord record) {
        Uri uri = getUriForRecord(record);

        final Uri result = context.getContentResolver().insert(uri, record.getContentValues());

        if (result == null) return false;

        context.getContentResolver().notifyChange(result, null, false);

        return true;
    }

    public static boolean updateRecord(@NonNull Context context, @NonNull BaseRecord record) {
        Uri uri = getUriForRecord(record);

        uri = ContentUris.withAppendedId(uri, record.getId());

        int result = context.getContentResolver().update(uri, record.getContentValues(), null, null);

        if (result == -1) return false;

        context.getContentResolver().notifyChange(uri, null, false);

        return true;
    }

    public static boolean deleteRecord(@NonNull Context context, @NonNull BaseRecord record) {
        Uri uri = getUriForRecord(record);

        uri = ContentUris.withAppendedId(uri, record.getId());

        int result = context.getContentResolver().delete(uri, null, null);

        if (result == -1) return false;

        context.getContentResolver().notifyChange(uri, null, false);

        return true;
    }
}
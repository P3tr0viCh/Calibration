package ru.p3tr0vich.calibration.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ru.p3tr0vich.calibration.models.DatabaseModel;
import ru.p3tr0vich.calibration.models.ScaleRecord;
import ru.p3tr0vich.calibration.utils.UtilsLog;

@SuppressWarnings("TryFinallyCanBeTryWithResources")
// Try-with-resources requires API level 19 (current min is 17)
public class DatabaseHelper extends SQLiteOpenHelper implements DatabaseModel {

    private static final String TAG = "DatabaseHelper";

    private static final boolean LOG_ENABLED = false;

    private static final boolean QUERY_WAIT_ENABLED = false;

//    public static class Filter {
//
//        @Retention(RetentionPolicy.SOURCE)
//        @IntDef({MODE_ALL, MODE_CURRENT_YEAR, MODE_YEAR, MODE_DATES, MODE_TWO_LAST_RECORDS})
//        public @interface Mode {
//        }
//
//        public static final int MODE_ALL = 0;
//        public static final int MODE_CURRENT_YEAR = 1;
//        public static final int MODE_YEAR = 2;
//        public static final int MODE_DATES = 3;
//        public static final int MODE_TWO_LAST_RECORDS = 4;
//
//        public long dateFrom;
//        public long dateTo;
//        public int year;
//        @Mode
//        public int mode;
//
//        public Filter() {
//            mode = MODE_ALL;
//        }
//
//        public Filter(int year) {
//            this.year = year;
//            mode = MODE_YEAR;
//        }
//
//        @NonNull
//        public String getSelection() {
//            if (mode == MODE_ALL)
//                return Where.RECORD_NOT_DELETED;
//            else if (mode == MODE_TWO_LAST_RECORDS) {
//                final Calendar calendar = Calendar.getInstance();
//
//                return TableFueling.DATETIME +
//                        String.format(Locale.US, Where.LESS_OR_EQUAL,
//                                UtilsDate.utcToLocal(calendar.getTimeInMillis())) +
//                        AND + Where.RECORD_NOT_DELETED;
//            } else {
//                final Calendar calendarFrom = Calendar.getInstance();
//                final Calendar calendarTo = Calendar.getInstance();
//
//                if (mode == MODE_DATES) {
//                    calendarFrom.setTimeInMillis(dateFrom);
//                    calendarTo.setTimeInMillis(dateTo);
//                } else {
//                    final int year = mode == MODE_YEAR ? this.year : UtilsDate.getCurrentYear();
//
//                    calendarFrom.set(year, Calendar.JANUARY, 1);
//                    calendarTo.set(year, Calendar.DECEMBER, 31);
//                }
//
//                UtilsDate.setStartOfDay(calendarFrom);
//                UtilsDate.setEndOfDay(calendarTo);
//
//                return TableFueling.DATETIME +
//                        String.format(Locale.US, Where.BETWEEN,
//                                UtilsDate.utcToLocal(calendarFrom.getTimeInMillis()),
//                                UtilsDate.utcToLocal(calendarTo.getTimeInMillis())) +
//                        AND + Where.RECORD_NOT_DELETED;
//            }
//        }
//    }

    public DatabaseHelper(@NonNull Context context) {
        super(context, Database.NAME, null, Database.VERSION);
    }

    @SuppressWarnings("SameParameterValue")
    public static boolean getBoolean(@NonNull Cursor cursor, int columnIndex) {
        return cursor.getInt(columnIndex) == Statement.TRUE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (LOG_ENABLED)
            UtilsLog.d(TAG, "onCreate", "sql == " + Database.CREATE_STATEMENT);
        db.execSQL(Database.CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (LOG_ENABLED)
            UtilsLog.d(TAG, "onUpgrade");
    }

    @NonNull
    public static ScaleRecord getScaleRecord(@NonNull Cursor cursor) {
        //noinspection WrongConstant
        return new ScaleRecord(
                cursor.getLong(TableScales.Columns._ID_INDEX),
                cursor.getString(TableScales.Columns.NAME_INDEX),
                cursor.getString(TableScales.Columns.TYPE_INDEX),
                cursor.getInt(TableScales.Columns.CLASS_STATIC_INDEX),
                cursor.getInt(TableScales.Columns.CLASS_DYNAMIC_INDEX));
    }

    @Nullable
    public static List<ScaleRecord> getScaleRecords(@Nullable Cursor cursor) {
        if (cursor == null) return null;

        List<ScaleRecord> records = new ArrayList<>();

        if (cursor.moveToFirst())
            do
                records.add(getScaleRecord(cursor));
            while (cursor.moveToNext());

        return records;
    }

    public Cursor getScales() {
        return query(TableScales.NAME, TableScales.Columns.ALL, null, null,
                TableScales.Columns.NAME + ", " + TableScales.Columns._ID);
    }

    public Cursor getScale(long id) {
        return query(TableScales.NAME, TableScales.Columns.ALL, TableScales.Columns._ID + Statement.EQUAL + id, null, null);
    }

    private Cursor query(String table, String[] columns, String selection, String groupBy, String orderBy, String limit) {
        if (LOG_ENABLED)
            UtilsLog.d(TAG, "query", "columns == " + Arrays.toString(columns) +
                    ", selection == " + selection + ", groupBy == " + groupBy +
                    ", orderBy == " + orderBy + ", limit == " + limit);

        if (QUERY_WAIT_ENABLED)
            for (int i = 0, waitSeconds = 3; i < waitSeconds; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                UtilsLog.d(TAG, "query", "wait... " + (waitSeconds - i));
            }

        return getReadableDatabase().query(table, columns, selection,
                null, groupBy, null, orderBy, limit);
    }

    private Cursor query(String table, String[] columns, String selection, String groupBy, String orderBy) {
        return query(table, columns, selection, groupBy, orderBy, null);
    }

    public long insert(@NonNull SQLiteDatabase db, @NonNull String table, @NonNull ContentValues values) {
        return db.insertOrThrow(table, null, values);
    }

    public long insert(@NonNull String table, @NonNull ContentValues values) {
        return insert(getWritableDatabase(), table, values);
    }

    private int update(@NonNull SQLiteDatabase db, @NonNull String table, @NonNull ContentValues values, @NonNull String whereClause) {
        return db.update(table, values, whereClause, null);
    }

    public int update(@NonNull String table, @NonNull ContentValues values, @NonNull String whereClause) {
        return update(getWritableDatabase(), table, values, whereClause);
    }

    public int update(@NonNull String table, @NonNull ContentValues values, long id) {
        return update(table, values, BaseColumns._ID + Statement.EQUAL + id);
    }

    private int delete(@NonNull SQLiteDatabase db, @NonNull String table, @Nullable String whereClause) {
        return db.delete(table, whereClause, null);
    }

    public int delete(@NonNull String table, @Nullable String whereClause) {
        return delete(getWritableDatabase(), table, whereClause);
    }

    public int delete(@NonNull String table, long id) {
        return delete(table, BaseColumns._ID + Statement.EQUAL + id);
    }
}
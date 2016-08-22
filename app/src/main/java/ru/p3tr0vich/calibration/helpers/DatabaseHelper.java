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

import ru.p3tr0vich.calibration.models.ScaleRecord;
import ru.p3tr0vich.calibration.utils.UtilsLog;

@SuppressWarnings("TryFinallyCanBeTryWithResources")
// Try-with-resources requires API level 19 (current min is 17)
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final boolean LOG_ENABLED = false;

    public static class TableScales {

        private TableScales() {
        }

        public static final String NAME = "scales";

        public static final class Columns implements BaseColumns {
            public static final String NAME = "name";
            public static final String TYPE = "type";
            public static final String CLASS_STATIC = "class_static";
            public static final String CLASS_DYNAMIC = "class_dynamic";

            private static final String[] ALL = new String[]{
                    _ID, NAME, TYPE, CLASS_STATIC, CLASS_DYNAMIC
            };

            private static final int _ID_INDEX = 0;
            private static final int NAME_INDEX = 1;
            private static final int TYPE_INDEX = 2;
            private static final int CLASS_STATIC_INDEX = 3;
            private static final int CLASS_DYNAMIC_INDEX = 4;
        }

        private static final String CREATE_STATEMENT = "CREATE TABLE " + NAME + "(" +
                Columns._ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE, " +
                Columns.NAME + " TEXT, " +
                Columns.TYPE + " TEXT, " +
                Columns.CLASS_STATIC + " INTEGER DEFAULT -1, " +
                Columns.CLASS_DYNAMIC + " INTEGER DEFAULT -1" +
                ");";
    }

    private static class Database {
        private static final int VERSION = 1;

        private static final String NAME = "calibration.db";

        private static final String CREATE_STATEMENT = TableScales.CREATE_STATEMENT;

        private Database() {
        }
    }

    private static final String AND = " AND ";
    private static final String AS = " AS ";
    private static final String DESC = " DESC";

    private static final String EQUAL = "=";
    private static final int TRUE = 1;
    private static final int FALSE = 0;

    public static class Where {
        private static final String BETWEEN = " BETWEEN %1$d AND %2$d";
        private static final String LESS_OR_EQUAL = " <= %d";

        private Where() {
        }
    }

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
        return cursor.getInt(columnIndex) == TRUE;
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
    public static ContentValues getScaleValues(long id,
                                               String name,
                                               String type,
                                               int classStatic,
                                               int classDynamic) {
        ContentValues values = new ContentValues();

        values.put(TableScales.Columns._ID, id);
        values.put(TableScales.Columns.NAME, name);
        values.put(TableScales.Columns.TYPE, type);
        values.put(TableScales.Columns.CLASS_STATIC, classStatic);
        values.put(TableScales.Columns.CLASS_DYNAMIC, classDynamic);

        return values;
    }

    @NonNull
    public static ScaleRecord getScaleRecord(@NonNull Cursor cursor) {
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
        return query(TableScales.NAME, TableScales.Columns.ALL, null, null, TableScales.Columns.NAME + DESC);
    }

    public Cursor getScale(long id) {
        return query(TableScales.NAME, TableScales.Columns.ALL, BaseColumns._ID + EQUAL + id, null, null);
    }

//    public Cursor getAll(String selection) {
//        return query(TableFueling.COLUMNS, selection, null, TableFueling.DATETIME + DESC);
//    }

//    public Cursor getRecord(long id) {
//        return query(TableFueling.COLUMNS, TableFueling._ID + EQUAL + id, null, null);
//    }

//    public int deleteMarkedAsDeleted() {
//        return delete(TableFueling.DELETED + EQUAL + TRUE);
//    }

//    public int updateChanged() {
//        ContentValues values = new ContentValues();
//        values.put(TableFueling.CHANGED, FALSE);
//
//        return update(values, TableFueling.CHANGED + EQUAL + TRUE);
//    }

    private Cursor query(String table, String[] columns, String selection, String groupBy, String orderBy, String limit) {
        if (LOG_ENABLED)
            UtilsLog.d(TAG, "query", "columns == " + Arrays.toString(columns) +
                    ", selection == " + selection + ", groupBy == " + groupBy +
                    ", orderBy == " + orderBy + ", limit == " + limit);

//        for (int i = 0, waitSeconds = 3; i < waitSeconds; i++) {
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            UtilsLog.d(TAG, "query", "wait... " + (waitSeconds - i));
//        }

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

    public int update(@NonNull SQLiteDatabase db, @NonNull String table, @NonNull ContentValues values, @NonNull String whereClause) {
        return db.update(table, values, whereClause, null);
    }

    public int update(@NonNull String table, @NonNull ContentValues values, @NonNull String whereClause) {
        return update(getWritableDatabase(), table, values, whereClause);
    }

    public int update(@NonNull String table, @NonNull ContentValues values, long id) {
        return update(table, values, BaseColumns._ID + EQUAL + id);
    }

    private int delete(@NonNull SQLiteDatabase db, @NonNull String table, @Nullable String whereClause) {
        return db.delete(table, whereClause, null);
    }

    public int delete(@NonNull String table, @Nullable String whereClause) {
        return delete(getWritableDatabase(), table, whereClause);
    }

    public int delete(@NonNull String table, long id) {
        return delete(table, BaseColumns._ID + EQUAL + id);
    }
}
package ru.p3tr0vich.calibration.models;

import android.provider.BaseColumns;

public interface DatabaseModel {

    interface Database {
        int VERSION = 1;

        String NAME = "calibration.db";

        String CREATE_STATEMENT = TableScales.CREATE_STATEMENT;
    }

    interface TableScales {
        String NAME = "scales";

        interface Columns extends BaseColumns {
            String NAME = "name";
            String TYPE = "type";
            String CLASS_STATIC = "class_static";
            String CLASS_DYNAMIC = "class_dynamic";

            String[] ALL = new String[]{
                    _ID, NAME, TYPE, CLASS_STATIC, CLASS_DYNAMIC
            };
            int _ID_INDEX = 0;
            int NAME_INDEX = 1;
            int TYPE_INDEX = 2;
            int CLASS_STATIC_INDEX = 3;
            int CLASS_DYNAMIC_INDEX = 4;
        }

        String CREATE_STATEMENT = "CREATE TABLE " + NAME + "(" +
                Columns._ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE, " +
                Columns.NAME + " TEXT, " +
                Columns.TYPE + " TEXT, " +
                Columns.CLASS_STATIC + " INTEGER DEFAULT 0, " +
                Columns.CLASS_DYNAMIC + " INTEGER DEFAULT 0" +
                ");";
    }

    interface Statement {
        String AND = " AND ";
        String AS = " AS ";
        String DESC = " DESC";

        String EQUAL = "=";
        int TRUE = 1;
        int FALSE = 0;
    }

    interface Where {
        String BETWEEN = " BETWEEN %1$d AND %2$d";
        String LESS_OR_EQUAL = " <= %d";
    }
}
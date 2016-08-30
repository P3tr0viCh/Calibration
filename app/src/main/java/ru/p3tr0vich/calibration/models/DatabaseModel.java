package ru.p3tr0vich.calibration.models;

import android.provider.BaseColumns;

/**
 * Структура базы данных.
 *
 * @see TableScales
 * @see TableCalibrations
 * @see TableDetails
 * @see TableControlWeights
 */
public interface DatabaseModel {

    interface Database {
        int VERSION = 4;

        String NAME = "calibration.db";

        String CREATE_STATEMENT = TableScales.CREATE_STATEMENT + "; " +
                TableCalibrations.CREATE_STATEMENT + "; " +
                TableDetails.CREATE_STATEMENT + "; " +
                TableControlWeights.CREATE_STATEMENT;
    }

    /**
     * Список весовых.<br>
     * Поле _ID содержит уникальный номер весовой. Задаётся вручную.
     *
     * @see Columns
     * @see ScaleRecord
     */
    interface TableScales {
        String NAME = "scales";

        /**
         * Поля таблицы.
         *
         * @see Columns#NAME
         * @see Columns#TYPE
         * @see Columns#CLASS_STATIC
         * @see Columns#CLASS_DYNAMIC
         */
        interface Columns extends BaseColumns {
            /**
             * Название весовой.
             */
            String NAME = "name";
            /**
             * Тип весов.
             */
            String TYPE = "type";
            /**
             * Класс точности в статике.
             */
            String CLASS_STATIC = "class_static";
            /**
             * Класс точности в динамике.
             */
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

    /**
     * Список калибровок.<br>
     * Поле _ID содержит уникальный номер. Задаётся автоматически.
     *
     * @see Columns
     * @see CalibrationRecord
     */
    interface TableCalibrations {
        String NAME = "calibrations";

        /**
         * Поля таблицы.
         *
         * @see Columns#DATE_TIME
         * @see Columns#SCALE_ID
         */
        interface Columns extends BaseColumns {
            /**
             * Дата и время начала калибровки.<br>
             * // TODO: Изначально устанавливается время добавления записи. Затем меняется на время добавления первого вагона.
             */
            String DATE_TIME = "date_time";
            /**
             * Сведения о весовой.<br>
             * Поле содержит ссылку на _ID в {@link TableScales таблице весовых}.
             */
            String SCALE_ID = "scale_id";

            String[] ALL = new String[]{
                    _ID, DATE_TIME, SCALE_ID
            };
//            int _ID_INDEX = 0;
//            int SCALE_ID_INDEX = 1;
//            int DATE_TIME_INDEX = 2;
        }

        String CREATE_STATEMENT = "CREATE TABLE " + NAME + "(" +
                Columns._ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE AUTOINCREMENT, " +
                Columns.DATE_TIME + " TEXT, " +

                Columns.SCALE_ID + " INTEGER REFERENCES " +
                TableScales.NAME + "(" + TableScales.Columns._ID + ") ON DELETE RESTRICT, " +
                ");";
    }

    /**
     * Список провешенных вагонов.<br>
     * Поле _ID содержит уникальный номер. Задаётся автоматически.
     *
     * @see Columns
     */
    interface TableDetails {
        String NAME = "calibrations_details";

        interface Columns extends BaseColumns {
            /**
             * Идентификатор калибровки.<br>
             * Поле содержит ссылку на _ID в {@link TableCalibrations таблице калибровок}.
             */
            String CALIBRATIONS_ID = "calibrations_id";
            /**
             * Дата и время провески.
             */
            String DATE_TIME = "date_time";
            /**
             * Порядковый номер вагона в составе.
             */
            String SERIAL_NUMBER = "serial_number";
            /**
             * Вес вагона.
             */
            String WEIGHT = "weight";

            String[] ALL = new String[]{
                    _ID, CALIBRATIONS_ID, DATE_TIME, SERIAL_NUMBER, WEIGHT
            };
//            int _ID_INDEX = 0;
//            int SCALE_ID_INDEX = 1;
//            int DATE_TIME_INDEX = 2;
        }

        String CREATE_STATEMENT = "CREATE TABLE " + NAME + "(" +
                Columns._ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE AUTOINCREMENT, " +

                Columns.CALIBRATIONS_ID + " INTEGER REFERENCES " +
                TableCalibrations.NAME + "(" + TableCalibrations.Columns._ID + ") ON DELETE RESTRICT, " +

                Columns.DATE_TIME + " TEXT, " +
                Columns.SERIAL_NUMBER + " INTEGER, " +
                Columns.WEIGHT + " INTEGER, " +
                ");";

    }

    /**
     * Список контрольных вагонов.<br>
     * Поле _ID содержит уникальный номер. Задаётся автоматически.
     *
     * @see Columns
     */
    interface TableControlWeights {
        String NAME = "control_weights";

        interface Columns extends BaseColumns {
            /**
             * Идентификатор калибровки.<br>
             * Поле содержит ссылку на _ID в {@link TableCalibrations таблице калибровок}.
             */
            String CALIBRATIONS_ID = "calibrations_id";
            /**
             * Порядковый номер вагона в составе.
             */
            String SERIAL_NUMBER = "serial_number";
            /**
             * Номер вагона.
             */
            String NUMBER = "number";
            /**
             * Контрольный вес.
             */
            String WEIGHT = "weight";

            String[] ALL = new String[]{
                    _ID, CALIBRATIONS_ID, SERIAL_NUMBER, NUMBER, WEIGHT
            };
//            int _ID_INDEX = 0;
//            int SCALE_ID_INDEX = 1;
//            int DATE_TIME_INDEX = 2;
        }

        String CREATE_STATEMENT = "CREATE TABLE " + NAME + "(" +
                Columns._ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE AUTOINCREMENT, " +

                Columns.CALIBRATIONS_ID + " INTEGER REFERENCES " +
                TableCalibrations.NAME + "(" + TableCalibrations.Columns._ID + ") ON DELETE RESTRICT, " +

                Columns.SERIAL_NUMBER + " INTEGER, " +
                Columns.NUMBER + " TEXT, " +
                Columns.WEIGHT + " INTEGER, " +
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
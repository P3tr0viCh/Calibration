package ru.p3tr0vich.calibration.utils;

import android.text.TextUtils;
import android.text.format.DateUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

import ru.p3tr0vich.calibration.ApplicationCalibration;

public class UtilsFormat {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    public static String dateTimeToString(long date, boolean withYear, boolean abbrevMonth) {
        int flags = DateUtils.FORMAT_SHOW_DATE;
        flags |= withYear ? DateUtils.FORMAT_SHOW_YEAR : DateUtils.FORMAT_NO_YEAR;
        if (abbrevMonth) flags |= DateUtils.FORMAT_ABBREV_MONTH;

//        flags |= DateUtils.FORMAT_SHOW_TIME;

        return DateUtils.formatDateTime(ApplicationCalibration.getContext(), date, flags);
    }

    public static String dateToString(long date, boolean withYear) {
        return dateTimeToString(date, withYear, false);
    }

    private static long stringToLong(String value) {
        if (TextUtils.isEmpty(value)) return 0;
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private static float stringToFloat(String value) {
        if (TextUtils.isEmpty(value)) return 0;
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String longToString(long value, boolean showZero) {
        final String strValue = String.valueOf(value);
        return showZero ? strValue : value == 0 ? "" : strValue;
    }

    public static String floatToString(float value, boolean showZero) {
        final String strValue = DECIMAL_FORMAT.format(value).replace(',', '.');
        return showZero ? strValue : value == 0 ? "" : strValue;
    }

    public static String floatToString(float value) {
        return floatToString(value, true);
    }

    public static long editTextToLong(EditText edit) {
        return stringToLong(edit.getText().toString());
    }

    public static float editTextToFloat(EditText edit) {
        return stringToFloat(edit.getText().toString());
    }

    public static void longToEditText(EditText edit, long value, boolean showZero) {
        edit.setText(longToString(value, showZero));
    }

    public static void floatToEditText(EditText edit, float value, boolean showZero) {
        edit.setText(floatToString(value, showZero));
    }

    public static void floatToTextView(TextView text, float value, boolean showZero) {
        text.setText(floatToString(value, showZero));
    }
}
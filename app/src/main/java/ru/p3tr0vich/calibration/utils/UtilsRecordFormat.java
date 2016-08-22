package ru.p3tr0vich.calibration.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import ru.p3tr0vich.calibration.R;
import ru.p3tr0vich.calibration.models.ScaleRecord;

public class UtilsRecordFormat {

    public static String getScaleNameAndId(Context context, ScaleRecord record) {
        String name = record.getName();
        if (TextUtils.isEmpty(name)) name = context.getString(R.string.item_scale_name_empty);
        return context.getString(R.string.item_scale_name, name, record.getId());
    }

    public static String getScaleType(Context context, ScaleRecord record) {
        String type = record.getType();
        if (TextUtils.isEmpty(type)) type = context.getString(R.string.item_scale_type_empty);
        return type;
    }

    public static String getScaleClassStatic(Context context, ScaleRecord record) {
        int classStatic = record.getClassStatic();
        switch (classStatic) {
            case ScaleRecord.CLASS_STATIC_HIGH:
                return context.getString(R.string.text_class_static_high);
            case ScaleRecord.CLASS_STATIC_MEDIUM:
                return context.getString(R.string.text_class_static_medium);
            case ScaleRecord.CLASS_STATIC_LOW:
                return context.getString(R.string.text_class_static_low);
            case ScaleRecord.CLASS_STATIC_NONE:
                return "";
            default:
                return context.getString(R.string.text_class_static_unknown, classStatic);
        }
    }

    public static String getScaleClassDynamic(Context context, ScaleRecord record) {
        int classDynamic = record.getClassDynamic();
        switch (classDynamic) {
            case ScaleRecord.CLASS_DYNAMIC_0_DOT_5:
                return context.getString(R.string.text_class_dynamic_0_dot_5);
            case ScaleRecord.CLASS_DYNAMIC_1:
                return context.getString(R.string.text_class_dynamic_1);
            case ScaleRecord.CLASS_DYNAMIC_2:
                return context.getString(R.string.text_class_dynamic_2);
            case ScaleRecord.CLASS_DYNAMIC_NONE:
                return "";
            default:
                return context.getString(R.string.text_class_static_unknown, classDynamic);
        }
    }
}
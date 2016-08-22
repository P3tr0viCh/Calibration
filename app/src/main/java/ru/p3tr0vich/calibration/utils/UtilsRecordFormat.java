package ru.p3tr0vich.calibration.utils;

import android.support.annotation.NonNull;

import ru.p3tr0vich.calibration.ApplicationCalibration;
import ru.p3tr0vich.calibration.R;
import ru.p3tr0vich.calibration.models.ScaleRecord;

public class UtilsRecordFormat {

    public static String getScaleName(@NonNull ScaleRecord record) {
        return String.format(ApplicationCalibration.getContext().getString(R.string.text_scale_name),
                record.getName(), record.getId());
    }
}
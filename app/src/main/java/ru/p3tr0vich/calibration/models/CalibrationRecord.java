package ru.p3tr0vich.calibration.models;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;

public class CalibrationRecord extends BaseRecord {

    public static final String NAME = CalibrationRecord.class.getName();

    @NonNull
    @Override
    public Bundle putToBundle(@NonNull Bundle bundle) {
        return bundle;
    }

    @NonNull
    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        return values;
    }
}
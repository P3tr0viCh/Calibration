package ru.p3tr0vich.calibration.models;

import android.content.ContentValues;
import android.os.Bundle;

public interface BaseRecord {
    long getId();

    Bundle getBundle();
    ContentValues getContentValues();
}
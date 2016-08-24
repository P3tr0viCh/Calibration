package ru.p3tr0vich.calibration.models;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;

public interface DatabaseRecord {
    long getId();

    void setId(long id);

    @NonNull
    Bundle putToBundle(@NonNull Bundle bundle);

    @NonNull
    Bundle putToBundle();

    @NonNull
    ContentValues getContentValues();
}
package ru.p3tr0vich.calibration.models;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.NonNull;

public abstract class BaseRecord implements DatabaseRecord {

    /**
     * Ключевое поле.
     */
    private long mId;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    @NonNull
    public abstract Bundle putToBundle(@NonNull Bundle bundle);

    @NonNull
    public abstract Bundle putToBundle();

    @NonNull
    public abstract ContentValues getContentValues();

    @Override
    public String toString() {
        return getContentValues().toString();
    }
}
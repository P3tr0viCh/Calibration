package ru.p3tr0vich.calibration.models;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.p3tr0vich.calibration.helpers.DatabaseHelper;

public class ScaleRecord implements BaseRecord, Parcelable {

    public static final String NAME = "SCALE_RECORD";

    private long mId;
    private String mName;
    private String mType;
    private int mClassStatic;
    private int mClassDynamic;

    public ScaleRecord(long id, String name, String type, int classStatic, int classDynamic) {
        setId(id);
        setName(name);
        setType(type);
        setClassStatic(classStatic);
        setClassDynamic(classDynamic);
    }

    public ScaleRecord() {
        this(0, null, null, 0, 0);
    }

    private ScaleRecord(@Nullable ScaleRecord record) {
        this();

        if (record != null) {
            mId = record.mId;
            mName = record.mName;
            mType = record.mType;
            mClassStatic = record.mClassStatic;
            mClassDynamic = record.mClassDynamic;
        }
    }

    public ScaleRecord(@NonNull Bundle bundle) {
        this((ScaleRecord) bundle.getParcelable(NAME));
    }

    @Override
    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public int getClassStatic() {
        return mClassStatic;
    }

    public void setClassStatic(int classStatic) {
        mClassStatic = classStatic;
    }

    public int getClassDynamic() {
        return mClassDynamic;
    }

    public void setClassDynamic(int classDynamic) {
        mClassDynamic = classDynamic;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    private Bundle getBundle(@NonNull Bundle bundle) {
        bundle.putParcelable(NAME, this);
        return bundle;
    }

    @Override
    public Bundle getBundle() {
        return getBundle(new Bundle());
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.TableScales._ID, mId);
        values.put(DatabaseHelper.TableScales.NAME, mName);
        values.put(DatabaseHelper.TableScales.TYPE, mType);
        values.put(DatabaseHelper.TableScales.CLASS_STATIC, mClassStatic);
        values.put(DatabaseHelper.TableScales.CLASS_DYNAMIC, mClassDynamic);

        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mName);
        dest.writeString(mType);
        dest.writeInt(mClassStatic);
        dest.writeInt(mClassDynamic);
    }

    public static final Parcelable.Creator<ScaleRecord> CREATOR = new Parcelable.Creator<ScaleRecord>() {

        @Override
        public ScaleRecord createFromParcel(Parcel in) {
            return new ScaleRecord(in.readLong(), in.readString(), in.readString(), in.readInt(), in.readInt());
        }

        @Override
        public ScaleRecord[] newArray(int size) {
            return new ScaleRecord[size];
        }
    };
}
package ru.p3tr0vich.calibration.models;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ru.p3tr0vich.calibration.helpers.DatabaseHelper;

public class ScaleRecord implements BaseRecord, Parcelable {

    public static final String NAME = "SCALE_RECORD";

    /**
     * Номер весовой. Ключевое поле.
     */
    private long mId;

    /**
     * Название весовой.
     */
    private String mName;

    /**
     * Тип весов.
     */
    private String mType;

    /**
     * Класс точности весов в статике.
     *
     * @see ClassStatic
     */
    @ClassStatic
    private int mClassStatic;

    /**
     * Класс точности весов в динамике.
     *
     * @see ClassDynamic
     */
    @ClassDynamic
    private int mClassDynamic;

    /**
     * Класс точности весов в статике.
     *
     * @see #CLASS_STATIC_NONE
     * @see #CLASS_STATIC_HIGH
     * @see #CLASS_STATIC_MEDIUM
     * @see #CLASS_STATIC_LOW
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CLASS_STATIC_NONE,
            CLASS_STATIC_HIGH,
            CLASS_STATIC_MEDIUM,
            CLASS_STATIC_LOW})
    public @interface ClassStatic {
    }

    /**
     * Весы не работают в статике.
     */
    public static final int CLASS_STATIC_NONE = 0;
    /**
     * Высокий класс точности в статике.
     */
    public static final int CLASS_STATIC_HIGH = 1;
    /**
     * Средний класс точности в статике.
     */
    public static final int CLASS_STATIC_MEDIUM = 2;
    /**
     * Низкий класс точности в статике.
     */
    public static final int CLASS_STATIC_LOW = 3;
    /**
     * Класс точности весов в динамике.
     *
     * @see #CLASS_DYNAMIC_NONE
     * @see #CLASS_DYNAMIC_0_DOT_5
     * @see #CLASS_DYNAMIC_1
     * @see #CLASS_DYNAMIC_2
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CLASS_DYNAMIC_NONE,
            CLASS_DYNAMIC_0_DOT_5,
            CLASS_DYNAMIC_1,
            CLASS_DYNAMIC_2})
    public @interface ClassDynamic {
    }

    /**
     * Весы не работают в динамике.
     */
    public static final int CLASS_DYNAMIC_NONE = 0;
    /**
     * Класс точности в динамике 0.5.
     */
    public static final int CLASS_DYNAMIC_0_DOT_5 = 1;
    /**
     * Класс точности в динамике 1.
     */
    public static final int CLASS_DYNAMIC_1 = 2;
    /**
     * Класс точности в динамике 2.
     */
    public static final int CLASS_DYNAMIC_2 = 3;

    public ScaleRecord(long id, String name, String type, @ClassStatic int classStatic,
                       @ClassDynamic int classDynamic) {
        setId(id);
        setName(name);
        setType(type);
        setClassStatic(classStatic);
        setClassDynamic(classDynamic);
    }

    public ScaleRecord() {
        this(0, null, null, CLASS_STATIC_NONE, CLASS_DYNAMIC_NONE);
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

    @ClassStatic
    public int getClassStatic() {
        return mClassStatic;
    }

    public void setClassStatic(@ClassStatic int classStatic) {
        mClassStatic = classStatic;
    }

    @ClassDynamic
    public int getClassDynamic() {
        return mClassDynamic;
    }

    public void setClassDynamic(@ClassDynamic int classDynamic) {
        mClassDynamic = classDynamic;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @NonNull
    public Bundle putToBundle(@NonNull Bundle bundle) {
        bundle.putParcelable(NAME, this);
        return bundle;
    }

    @NonNull
    public Bundle putToBundle() {
        return putToBundle(new Bundle());
    }

    @NonNull
    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.TableScales.Columns._ID, mId);
        values.put(DatabaseHelper.TableScales.Columns.NAME, mName);
        values.put(DatabaseHelper.TableScales.Columns.TYPE, mType);
        values.put(DatabaseHelper.TableScales.Columns.CLASS_STATIC, mClassStatic);
        values.put(DatabaseHelper.TableScales.Columns.CLASS_DYNAMIC, mClassDynamic);

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

    private ScaleRecord(Parcel in) {
        //noinspection WrongConstant
        this(in.readLong(), in.readString(), in.readString(), in.readInt(), in.readInt());
    }

    public static final Parcelable.Creator<ScaleRecord> CREATOR = new Parcelable.Creator<ScaleRecord>() {

        @Override
        public ScaleRecord createFromParcel(Parcel in) {
            return new ScaleRecord(in);
        }

        @Override
        public ScaleRecord[] newArray(int size) {
            return new ScaleRecord[size];
        }
    };
}
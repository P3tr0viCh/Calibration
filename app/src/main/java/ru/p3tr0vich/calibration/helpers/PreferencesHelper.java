package ru.p3tr0vich.calibration.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ru.p3tr0vich.calibration.utils.UtilsLog;

public class PreferencesHelper implements SharedPreferences.OnSharedPreferenceChangeListener {

    @SuppressLint("StaticFieldLeak")
    private static PreferencesHelper instance;

    private static final String TAG = "PreferencesHelper";

    private static final boolean LOG_ENABLED = false;

    private final Context mContext; // == ApplicationContext
    private final SharedPreferences mSharedPreferences;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PREFERENCE_TYPE_STRING, PREFERENCE_TYPE_INT, PREFERENCE_TYPE_LONG})
    public @interface PreferenceType {
    }

    public static final int PREFERENCE_TYPE_STRING = 0;
    public static final int PREFERENCE_TYPE_INT = 1;
    public static final int PREFERENCE_TYPE_LONG = 2;

    public static class Keys {
        @Retention(RetentionPolicy.SOURCE)
        @IntDef({UNKNOWN})
        public @interface KeyAsInt {
        }

        public static final int UNKNOWN = -1;

//        public final String price;
//        public static final int PRICE = R.string.pref_key_price;

        private Keys(@NonNull Context context) {
//            price = context.getString(R.string.pref_key_price);
        }

        @KeyAsInt
        public int getAsInt(@Nullable String key) {
//            if (price.equals(key)) return PRICE;
//            else if (defaultCost.equals(key)) return DEFAULT_COST;
//            else
            return UNKNOWN;
        }

        private boolean isSyncKey(@Nullable String key) {
            switch (getAsInt(key)) {
//                case SYNC_ENABLED:
                default:
                    return false;

                case UNKNOWN:
                    return true;
            }
        }
    }

    public final Keys keys;

    private PreferencesHelper(@NonNull Context context) {
        mContext = context.getApplicationContext();

        keys = new Keys(context);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

//        mSharedPreferences.edit()
//                .remove("last sync")
//                .remove("full sync")
//                .apply();

        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public static synchronized PreferencesHelper getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new PreferencesHelper(context);
        }

        return instance;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (LOG_ENABLED) UtilsLog.d(TAG, "onSharedPreferenceChanged", "key == " + key);
    }

    @NonNull
    private String getString(String key, @NonNull String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    @NonNull
    public String getString(String key) {
        return getString(key, "");
    }
}
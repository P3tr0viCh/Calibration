package ru.p3tr0vich.calibration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;
import android.text.TextUtils;

import ru.p3tr0vich.calibration.utils.UtilsLog;

public class FragmentPreferences extends FragmentPreferencesBase implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    private static final String TAG = "FragmentPreferences";

    private static final boolean LOG_ENABLED = false;

    public static final String KEY_PREFERENCE_SCREEN = "KEY_PREFERENCE_SCREEN";

    private OnPreferenceScreenChangeListener mOnPreferenceScreenChangeListener;
    private OnPreferenceClickListener mOnPreferenceClickListener;

    private boolean mIsInRoot;
    private PreferenceScreen mRootPreferenceScreen;

    public interface OnPreferenceClickListener {
    }

    public interface OnPreferenceScreenChangeListener {
        void onPreferenceScreenChanged(@NonNull CharSequence title, boolean isInRoot);
    }

    @NonNull
    @Override
    public String getTitle() {
        return mIsInRoot ? getString(R.string.title_prefs) : (String) getPreferenceScreen().getTitle();
    }

    public boolean isInRoot() {
        return mIsInRoot;
    }

    @SuppressLint("SwitchIntDef")
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (LOG_ENABLED) UtilsLog.d(TAG, "onSharedPreferenceChanged", "key == " + key);

        updatePreference(key);
    }

    @SuppressLint("SwitchIntDef")
    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preferencesHelper.keys.getAsInt(preference.getKey())) {
            default:
                return false;
        }
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        if (LOG_ENABLED) UtilsLog.d(TAG, "onCreatePreferences");

        addPreferencesFromResource(R.xml.preferences);
        mRootPreferenceScreen = getPreferenceScreen();

        String keyPreferenceScreen = null;

        if (bundle == null) {
            Bundle arguments = getArguments();
            if (arguments != null)
                keyPreferenceScreen = arguments.getString(KEY_PREFERENCE_SCREEN);
        } else
            keyPreferenceScreen = bundle.getString(KEY_PREFERENCE_SCREEN);

        if (TextUtils.isEmpty(keyPreferenceScreen))
            mIsInRoot = true;
        else
            navigateToScreen((PreferenceScreen) findPreference(keyPreferenceScreen));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_PREFERENCE_SCREEN, mIsInRoot ? null : getPreferenceScreen().getKey());
    }

    @Override
    public void onNavigateToScreen(PreferenceScreen preferenceScreen) {
        navigateToScreen(preferenceScreen);

        super.onNavigateToScreen(preferenceScreen);
    }

    private void navigateToScreen(@Nullable PreferenceScreen preferenceScreen) {
        mIsInRoot = preferenceScreen == null || preferenceScreen.equals(mRootPreferenceScreen);

        setPreferenceScreen(mIsInRoot ? mRootPreferenceScreen : preferenceScreen);

        mOnPreferenceScreenChangeListener.onPreferenceScreenChanged(getTitle(), mIsInRoot);
    }

    public boolean goToRootScreen() {
        if (mIsInRoot) return false;

        navigateToScreen(null);

        return true;
    }

    @Override
    public boolean onBackPressed() {
        return goToRootScreen();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnPreferenceScreenChangeListener = (OnPreferenceScreenChangeListener) context;
            mOnPreferenceClickListener = (OnPreferenceClickListener) context;
        } catch (ClassCastException e) {
            throw new ImplementException(context, new Class[]{
                    OnPreferenceScreenChangeListener.class,
                    OnPreferenceClickListener.class});
        }
    }

    @Override
    public void onStart() {
        if (LOG_ENABLED) UtilsLog.d(TAG, "onStart");

        super.onStart();

        init(mRootPreferenceScreen);

        mRootPreferenceScreen.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        if (LOG_ENABLED) UtilsLog.d(TAG, "onStop");

        mRootPreferenceScreen.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        super.onStop();
    }

    private String getValue(@Nullable String value, @NonNull String empty) {
        return TextUtils.isEmpty(value) ? empty : value;
    }

    private String getValue(@Nullable String value, @StringRes int emptyId) {
        return getValue(value, getString(emptyId));
    }

    private void updatePreference(String key) {
        updatePreference(mRootPreferenceScreen.findPreference(key));
    }

    @SuppressLint("SwitchIntDef")
    private void updatePreference(@Nullable Preference preference) {
        if (LOG_ENABLED) UtilsLog.d(TAG, "updatePreference", "preference == " + preference);

        if (preference == null) return;

        String key = preference.getKey();
        if (key == null) return;

        String title = null;
        String summary = null;

        int intKey = preferencesHelper.keys.getAsInt(key);

        switch (intKey) {
             default:
                if (preference instanceof EditTextPreference) {
                    String text = preferencesHelper.getString(key);

                    ((EditTextPreference) preference).setText(text);

                    text = getValue(text, "0");

                    summary = preference.getSummary().toString();

                    int i = summary.lastIndexOf(" (");
                    if (i != -1) summary = summary.substring(0, i);
                    summary += " (" + text + ")";
                }
        }

        if (LOG_ENABLED)
            UtilsLog.d(TAG, "updatePreference", "title == " + title + ", summary == " + summary);

        if (title != null) preference.setTitle(title);
        if (summary != null) preference.setSummary(summary);
    }

    private void init(@NonNull Preference preference) {
        updatePreference(preference);
        if (preference instanceof PreferenceScreen || preference instanceof PreferenceGroup) {
            for (int i = 0, count = ((PreferenceGroup) preference).getPreferenceCount(); i < count; i++)
                init(((PreferenceGroup) preference).getPreference(i));
        }
    }
}
package ru.p3tr0vich.calibration.factories;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ru.p3tr0vich.calibration.ApplicationCalibration;
import ru.p3tr0vich.calibration.FragmentAbout;
import ru.p3tr0vich.calibration.FragmentActivityDialogScaleChange;
import ru.p3tr0vich.calibration.FragmentBase;
import ru.p3tr0vich.calibration.FragmentCalibrations;
import ru.p3tr0vich.calibration.FragmentPreferences;
import ru.p3tr0vich.calibration.FragmentScales;
import ru.p3tr0vich.calibration.R;

public class FragmentFactory {

    public static final class IDS {

        @Retention(RetentionPolicy.SOURCE)
        @IntDef({BAD_ID,
                CALIBRATIONS,
                SCALES,
                PREFERENCES,
                ABOUT,
                SCALE_CHANGE})
        public @interface Id {
        }

        public static final int BAD_ID = -1;
        public static final int CALIBRATIONS = 10;
        public static final int SCALES = 11;
        public static final int PREFERENCES = 12;
        public static final int ABOUT = 13;

        public static final int SCALE_CHANGE = 20;

        private IDS() {
        }
    }

    public static final class TAGS {
        public static final String CALIBRATIONS = FragmentCalibrations.class.getSimpleName();
        public static final String SCALES = FragmentScales.class.getSimpleName();
        public static final String PREFERENCES = FragmentPreferences.class.getSimpleName();
        public static final String ABOUT = FragmentAbout.class.getSimpleName();
        public static final String SCALE_CHANGE = FragmentActivityDialogScaleChange.class.getSimpleName();

        private TAGS() {
        }
    }

    public static final class MAIN_FRAGMENT {
        public static final int ID = IDS.SCALES;
        public static final String TAG = TAGS.SCALES;
    }

    private FragmentFactory() {
    }

    @NonNull
    public static Fragment getFragmentNewInstance(@IDS.Id int fragmentId, @Nullable Bundle args) {
        Fragment fragment;

        switch (fragmentId) {
            case IDS.CALIBRATIONS:
                fragment = new FragmentCalibrations();
                break;
            case IDS.SCALES:
                fragment = new FragmentScales();
                break;
            case IDS.PREFERENCES:
                fragment = new FragmentPreferences();
                break;
            case IDS.ABOUT:
                fragment = new FragmentAbout();
                break;
            case IDS.SCALE_CHANGE:
                fragment = new FragmentActivityDialogScaleChange();
                break;
            case IDS.BAD_ID:
            default:
                throw new IllegalArgumentException(ApplicationCalibration.getContext().getString(R.string.exception_fragment_bad_id));
        }

        return FragmentBase.newInstance(fragmentId, fragment, args);
    }

    @NonNull
    public static Fragment getFragmentNewInstance(@IDS.Id int fragmentId) {
        return getFragmentNewInstance(fragmentId, null);
    }

    @IDS.Id
    public static int intToFragmentId(int id) {
        return id;
    }

    @NonNull
    public static String fragmentIdToTag(@IDS.Id int id) {
        switch (id) {
            case IDS.CALIBRATIONS:
                return TAGS.CALIBRATIONS;
            case IDS.SCALES:
                return TAGS.SCALES;
            case IDS.PREFERENCES:
                return TAGS.PREFERENCES;
            case IDS.ABOUT:
                return TAGS.ABOUT;
            case IDS.SCALE_CHANGE:
                return TAGS.SCALE_CHANGE;
            case IDS.BAD_ID:
            default:
                throw new IllegalArgumentException(ApplicationCalibration.getContext().getString(R.string.exception_fragment_bad_id));
        }
    }
}
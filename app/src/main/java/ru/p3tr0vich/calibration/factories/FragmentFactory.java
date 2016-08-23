package ru.p3tr0vich.calibration.factories;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import ru.p3tr0vich.calibration.FragmentAbout;
import ru.p3tr0vich.calibration.FragmentActivityDialogScaleChange;
import ru.p3tr0vich.calibration.FragmentBase;
import ru.p3tr0vich.calibration.FragmentCalibrations;
import ru.p3tr0vich.calibration.FragmentPreferences;
import ru.p3tr0vich.calibration.FragmentScales;

public class FragmentFactory {

    public interface Ids {

        @Retention(RetentionPolicy.SOURCE)
        @IntDef({BAD_ID,
                CALIBRATIONS,
                SCALES,
                PREFERENCES,
                ABOUT,
                SCALE_CHANGE})
        @interface Id {
        }

        int BAD_ID = -1;
        int CALIBRATIONS = 10;
        int SCALES = 11;
        int PREFERENCES = 12;
        int ABOUT = 13;

        int SCALE_CHANGE = 20;
    }

    private interface Tags {
        String CALIBRATIONS = FragmentCalibrations.class.getSimpleName();
        String SCALES = FragmentScales.class.getSimpleName();
        String PREFERENCES = FragmentPreferences.class.getSimpleName();
        String ABOUT = FragmentAbout.class.getSimpleName();
        String SCALE_CHANGE = FragmentActivityDialogScaleChange.class.getSimpleName();
    }

    public interface MainFragment{
        int ID = Ids.SCALES;
        String TAG = Tags.SCALES;
    }

    private FragmentFactory() {
    }

    @NonNull
    public static Fragment getFragmentNewInstance(@Ids.Id int fragmentId, @Nullable Bundle args) {
        Fragment fragment;

        switch (fragmentId) {
            case Ids.CALIBRATIONS:
                fragment = new FragmentCalibrations();
                break;
            case Ids.SCALES:
                fragment = new FragmentScales();
                break;
            case Ids.PREFERENCES:
                fragment = new FragmentPreferences();
                break;
            case Ids.ABOUT:
                fragment = new FragmentAbout();
                break;
            case Ids.SCALE_CHANGE:
                fragment = new FragmentActivityDialogScaleChange();
                break;
            case Ids.BAD_ID:
            default:
                throw new IllegalArgumentException("Fragment bad ID");
        }

        return FragmentBase.newInstance(fragmentId, fragment, args);
    }

    @NonNull
    public static Fragment getFragmentNewInstance(@Ids.Id int fragmentId) {
        return getFragmentNewInstance(fragmentId, null);
    }

    @Ids.Id
    public static int intToFragmentId(int id) {
        return id;
    }

    @NonNull
    public static String fragmentIdToTag(@Ids.Id int id) {
        switch (id) {
            case Ids.CALIBRATIONS:
                return Tags.CALIBRATIONS;
            case Ids.SCALES:
                return Tags.SCALES;
            case Ids.PREFERENCES:
                return Tags.PREFERENCES;
            case Ids.ABOUT:
                return Tags.ABOUT;
            case Ids.SCALE_CHANGE:
                return Tags.SCALE_CHANGE;
            case Ids.BAD_ID:
            default:
                throw new IllegalArgumentException("Fragment bad ID");
        }
    }
}
package ru.p3tr0vich.calibration.helpers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import ru.p3tr0vich.calibration.FragmentCalibrations;
import ru.p3tr0vich.calibration.FragmentInterface;
import ru.p3tr0vich.calibration.FragmentPreferences;
import ru.p3tr0vich.calibration.R;
import ru.p3tr0vich.calibration.factories.FragmentFactory;

public class FragmentHelper {

    private final FragmentActivity mFragmentActivity;

    public FragmentHelper(@NonNull FragmentActivity fragmentActivity) {
        mFragmentActivity = fragmentActivity;
    }

    @NonNull
    public FragmentInterface getCurrentFragment() {
        return (FragmentInterface) mFragmentActivity.getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);

    }

    @Nullable
    public Fragment getFragment(@NonNull String fragmentTag) {
        return mFragmentActivity.getSupportFragmentManager().findFragmentByTag(fragmentTag);
    }

    @Nullable
    public FragmentCalibrations getFragmentCalibrations() {
        return (FragmentCalibrations) getFragment(FragmentFactory.TAGS.CALIBRATIONS);
    }

    @Nullable
    public FragmentPreferences getFragmentPreferences() {
        return (FragmentPreferences) getFragment(FragmentFactory.TAGS.PREFERENCES);
    }

    public void addMainFragment() {
        mFragmentActivity.getSupportFragmentManager().beginTransaction()
                .add(R.id.content_frame,
                        FragmentFactory.getFragmentNewInstance(FragmentFactory.MAIN_FRAGMENT.ID),
                        FragmentFactory.MAIN_FRAGMENT.TAG)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .commit();
    }

    public void replaceFragment(@FragmentFactory.IDS.Id int fragmentId) {
        mFragmentActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame,
                        FragmentFactory.getFragmentNewInstance(fragmentId),
                        FragmentFactory.fragmentIdToTag(fragmentId))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }
}
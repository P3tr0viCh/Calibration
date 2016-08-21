package ru.p3tr0vich.calibration;

import android.support.annotation.NonNull;

import ru.p3tr0vich.calibration.adapters.BaseAdapter;
import ru.p3tr0vich.calibration.utils.Utils;

public class FragmentCalibrations extends FragmentBaseList {

    private static final String TAG = "FragmentCalibrations";

    private static final boolean LOG_ENABLED = true;

    @Override
    public int getTitleId() {
        return R.string.title_calibrations;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_calibrations;
    }

    @Override
    public void onFloatingActionButtonClick() {
        Utils.toast(TAG);
    }

    @NonNull
    @Override
    public BaseAdapter createRecyclerViewAdapter(boolean isPhone) {
        return null;
    }
}
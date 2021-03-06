package ru.p3tr0vich.calibration;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;

import ru.p3tr0vich.calibration.factories.FragmentFactory;
import ru.p3tr0vich.calibration.helpers.PreferencesHelper;

import static ru.p3tr0vich.calibration.factories.FragmentFactory.Ids.BAD_ID;

public abstract class FragmentPreferencesBase extends PreferenceFragmentCompat
        implements FragmentInterface {

    private static final String KEY_ID = "FRAGMENT_BASE_KEY_ID";

    @FragmentFactory.Ids.Id
    private int mFragmentId = BAD_ID;

    private OnFragmentChangeListener mOnFragmentChangeListener;

    protected PreferencesHelper preferencesHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        preferencesHelper = PreferencesHelper.getInstance(getContext());

        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            mFragmentId = FragmentFactory.intToFragmentId(getArguments().getInt(KEY_ID, BAD_ID));

        if (mFragmentId == BAD_ID)
            throw new IllegalArgumentException("Fragment must have ID");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDivider(null);

        getListView().addItemDecoration(new DividerItemDecorationPreferences(getContext()));
    }

    @Override
    public final int getFragmentId() {
        return mFragmentId;
    }

    @Override
    public int getTitleId() {
        return -1;
    }

    @Override
    public int getSubtitleId() {
        return -1;
    }

    @Nullable
    @Override
    public String getSubtitle() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnFragmentChangeListener = (OnFragmentChangeListener) context;
        } catch (ClassCastException e) {
            throw new ImplementException(context, OnFragmentChangeListener.class);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mOnFragmentChangeListener.onFragmentChange(this);
    }
}
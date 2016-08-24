package ru.p3tr0vich.calibration.presenters;

import android.content.Context;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.Field;

import ru.p3tr0vich.calibration.models.BaseRecord;

public class PopupMenuPresenter<T extends BaseRecord> {

    @MenuRes
    private int mMenuRes;

    private OnMenuItemClickListener<T> mOnMenuItemClickListener;

    public interface OnMenuItemClickListener<T> {
        boolean onMenuItemClick(MenuItem item, @Nullable T record);
    }

    public PopupMenuPresenter(@MenuRes int menuRes, @NonNull OnMenuItemClickListener<T> onMenuItemClickListener) {
        mMenuRes = menuRes;
        mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void onClick(Context context, View view, @Nullable T record) {
        doPopup(context, view, record);
    }

    private void doPopup(Context context, View view, @Nullable final T record) {
        int menuRes = mMenuRes;

        if (menuRes <= 0) return;

        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(menuRes);

        Object menuHelper = null;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popupMenu);
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", boolean.class).invoke(menuHelper, true);
        } catch (Exception e) {
            //
        }

        popupMenu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return mOnMenuItemClickListener.onMenuItemClick(item, record);
                    }
                }
        );

        popupMenu.show();

        try {
            if (menuHelper == null) return;

            Field fListPopup = menuHelper.getClass().getDeclaredField("mPopup");
            fListPopup.setAccessible(true);
            Object listPopup = fListPopup.get(menuHelper);
            Class<?> listPopupClass = listPopup.getClass();

            // Magic number
            listPopupClass.getDeclaredMethod("setVerticalOffset", int.class).invoke(listPopup, -view.getHeight() - 8);

            listPopupClass.getDeclaredMethod("show").invoke(listPopup);
        } catch (Exception e) {
            //
        }
    }

}
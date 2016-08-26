package ru.p3tr0vich.calibration;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

public abstract class BroadcastReceiverLoading extends BroadcastReceiverLocalBase {

    private static final String ACTION = BuildConfig.APPLICATION_ID + ".ACTION_LOADING";
    private static final String EXTRA_LOADER_ID = BuildConfig.APPLICATION_ID + ".EXTRA_LOADER_ID";
    private static final String EXTRA_LOADING = BuildConfig.APPLICATION_ID + ".EXTRA_LOADING";

    @Override
    protected final String getAction() {
        return ACTION;
    }

    public static void send(@NonNull Context context, int loaderId, boolean loading) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION)
                .putExtra(EXTRA_LOADER_ID, loaderId)
                .putExtra(EXTRA_LOADING, loading));
    }

    @Override
    public final void onReceive(Context context, Intent intent) {
        onReceive(intent.getIntExtra(EXTRA_LOADER_ID, -1), intent.getBooleanExtra(EXTRA_LOADING, false));
    }

    public abstract void onReceive(int loaderId, boolean loading);
}
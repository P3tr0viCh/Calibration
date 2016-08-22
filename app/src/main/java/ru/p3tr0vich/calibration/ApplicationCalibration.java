package ru.p3tr0vich.calibration;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import ru.p3tr0vich.calibration.utils.UtilsLog;

public class ApplicationCalibration extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

        UtilsLog.d("ApplicationCalibration", "**********************************************************");
        UtilsLog.d("ApplicationCalibration", "onCreate");

//        ContentObserverService.start(this);
    }

    @Override
    public void onTerminate() {
        UtilsLog.d("ApplicationCalibration", "onTerminate");
        super.onTerminate();
    }

    @NonNull
    public static Context getContext() {
        if (sContext == null) throw new AssertionError("Application context is null");
        return sContext;
    }
}
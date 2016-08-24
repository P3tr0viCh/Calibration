-dontnote com.google.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

-dontnote android.net.http.**
-dontnote org.apache.http.**
-dontnote org.apache.commons.codec.**

-keepclassmembers class android.support.v7.widget.ListPopupWindow {
    public void setVerticalOffset(int);
    public void show();
}

-keepclassmembers class android.support.v7.view.menu.MenuPopupHelper {
    private android.support.v7.widget.ListPopupWindow mPopup;
    public void setForceShowIcon(boolean);
}

-keepclassmembers class android.support.v7.widget.PopupMenu {
    private android.support.v7.view.menu.MenuPopupHelper mPopup;
}

-dontnote android.support.v4.app.**
-dontnote android.support.v4.view.**
-dontnote android.support.v4.text.**
-dontnote android.support.v4.widget.**
-dontnote android.support.v7.view.**
-dontnote android.support.v7.widget.**
-dontnote android.support.design.widget.**

-dontnote com.wdullaer.materialdatetimepicker.**

-keep class com.github.mikephil.charting.animation.ChartAnimator { *; }

-dontnote com.github.mikephil.charting.charts.**
-dontnote com.github.mikephil.charting.animation.**

-keep class com.yandex.disk.rest.** { *; }

-keepattributes Signature
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepattributes *Annotation*

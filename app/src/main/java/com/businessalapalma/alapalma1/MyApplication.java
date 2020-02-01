package com.businessalapalma.alapalma1;

import android.app.Application;
import com.amplitude.api.Amplitude;
import com.onesignal.OneSignal;

public class MyApplication extends Application {

    private static MyApplication nInstance;

    public  MyApplication () {
        nInstance=this;
    }

    @Override
    public  void onCreate (){
        super.onCreate();
        nInstance=this;
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    public static synchronized MyApplication getInstance(){
        return nInstance;
    }
}

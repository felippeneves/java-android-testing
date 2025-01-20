package com.felippeneves.tutorial_android_application.common;

import android.app.Application;

import com.felippeneves.tutorial_android_application.common.di.CompositionRoot;

public class CustomApplication extends Application {

    private CompositionRoot mCompositionRoot;

    @Override
    public void onCreate() {
        super.onCreate();
        mCompositionRoot = new CompositionRoot();
    }

    public CompositionRoot getCompositionRoot() {
        return mCompositionRoot;
    }
}

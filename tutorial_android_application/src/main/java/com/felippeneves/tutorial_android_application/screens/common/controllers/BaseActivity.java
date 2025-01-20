package com.felippeneves.tutorial_android_application.screens.common.controllers;

import androidx.appcompat.app.AppCompatActivity;

import com.felippeneves.tutorial_android_application.common.CustomApplication;
import com.felippeneves.tutorial_android_application.common.di.ControllerCompositionRoot;

public class BaseActivity extends AppCompatActivity {

    private ControllerCompositionRoot mControllerCompositionRoot;

    protected ControllerCompositionRoot getCompositionRoot() {
        if (mControllerCompositionRoot == null) {
            mControllerCompositionRoot = new ControllerCompositionRoot(
                    ((CustomApplication) getApplication()).getCompositionRoot(),
                    this
            );
        }
        return mControllerCompositionRoot;
    }

}

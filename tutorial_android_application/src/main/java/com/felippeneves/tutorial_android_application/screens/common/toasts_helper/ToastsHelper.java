package com.felippeneves.tutorial_android_application.screens.common.toasts_helper;

import android.content.Context;
import android.widget.Toast;

import com.felippeneves.tutorial_android_application.R;

public class ToastsHelper {

    private final Context mContext;

    public ToastsHelper(Context context) {
        mContext = context;
    }

    public void showUseCaseError() {
        Toast.makeText(mContext, R.string.error_network_call_failed, Toast.LENGTH_SHORT).show();
    }
}

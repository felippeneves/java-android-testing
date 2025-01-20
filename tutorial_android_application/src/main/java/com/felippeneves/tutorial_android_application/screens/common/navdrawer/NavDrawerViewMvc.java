package com.felippeneves.tutorial_android_application.screens.common.navdrawer;

import android.widget.FrameLayout;

import com.felippeneves.tutorial_android_application.screens.common.views.ObservableViewMvc;

public interface NavDrawerViewMvc extends ObservableViewMvc<NavDrawerViewMvc.Listener> {

    interface Listener {

        void onQuestionsListClicked();
    }

    FrameLayout getFragmentFrame();

    boolean isDrawerOpen();

    void openDrawer();

    void closeDrawer();
}

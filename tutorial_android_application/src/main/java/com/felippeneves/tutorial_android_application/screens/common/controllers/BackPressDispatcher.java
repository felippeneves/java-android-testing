package com.felippeneves.tutorial_android_application.screens.common.controllers;

public interface BackPressDispatcher {
    void registerListener(BackPressedListener listener);

    void unregisterListener(BackPressedListener listener);
}

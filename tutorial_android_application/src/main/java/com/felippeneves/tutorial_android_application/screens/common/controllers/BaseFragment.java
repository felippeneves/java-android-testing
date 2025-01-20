package com.felippeneves.tutorial_android_application.screens.common.controllers;

import androidx.fragment.app.Fragment;

import com.felippeneves.tutorial_android_application.common.CustomApplication;
import com.felippeneves.tutorial_android_application.common.di.ControllerCompositionRoot;

public class BaseFragment extends Fragment {

    private ControllerCompositionRoot mControllerCompositionRoot;

    protected ControllerCompositionRoot getCompositionRoot() {
        if (mControllerCompositionRoot == null) {
            mControllerCompositionRoot = new ControllerCompositionRoot(
                    ((CustomApplication) requireActivity().getApplication()).getCompositionRoot(),
                    requireActivity()
            );
        }
        return mControllerCompositionRoot;
    }
}

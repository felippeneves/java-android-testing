package com.felippeneves.tutorial_android_application.screens.common.main;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.felippeneves.tutorial_android_application.R;
import com.felippeneves.tutorial_android_application.screens.common.controllers.BackPressDispatcher;
import com.felippeneves.tutorial_android_application.screens.common.controllers.BackPressedListener;
import com.felippeneves.tutorial_android_application.screens.common.controllers.BaseActivity;
import com.felippeneves.tutorial_android_application.screens.common.fragment_frame_helper.FragmentFrameWrapper;
import com.felippeneves.tutorial_android_application.screens.common.navdrawer.NavDrawerHelper;
import com.felippeneves.tutorial_android_application.screens.common.navdrawer.NavDrawerViewMvc;
import com.felippeneves.tutorial_android_application.screens.common.screens_navigator.ScreensNavigator;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends BaseActivity implements
        BackPressDispatcher,
        FragmentFrameWrapper,
        NavDrawerViewMvc.Listener,
        NavDrawerHelper {

    private final Set<BackPressedListener> mBackPressedListeners = new HashSet<>();
    private ScreensNavigator mScreensNavigator;

    private NavDrawerViewMvc mViewMvc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScreensNavigator = getCompositionRoot().getScreensNavigator();
        mViewMvc = getCompositionRoot().getViewMvcFactory().getNavDrawerViewMvc(null);
        setContentView(mViewMvc.getRootView());

        if (savedInstanceState == null) {
            mScreensNavigator.toQuestionsList();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewMvc.registerListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewMvc.unregisterListener(this);
    }

    @Override
    public void onQuestionsListClicked() {
        mScreensNavigator.toQuestionsList();
    }

    @Override
    public void registerListener(BackPressedListener listener) {
        mBackPressedListeners.add(listener);
    }

    @Override
    public void unregisterListener(BackPressedListener listener) {
        mBackPressedListeners.remove(listener);
    }

    @Override
    public void onBackPressed() {
        boolean isBackPressConsumedByAnyListener = false;
        for (BackPressedListener listener : mBackPressedListeners) {
            if (listener.onBackPressed()) {
                isBackPressConsumedByAnyListener = true;
            }
        }
        if (!isBackPressConsumedByAnyListener) {
            super.onBackPressed();
        }
    }

    @Override
    public FrameLayout getFragmentFrame() {
        return mViewMvc.getFragmentFrame();
    }

    @Override
    public void openDrawer() {
        mViewMvc.openDrawer();
    }

    @Override
    public void closeDrawer() {
        mViewMvc.closeDrawer();
    }

    @Override
    public boolean isDrawerOpen() {
        return mViewMvc.isDrawerOpen();
    }
}
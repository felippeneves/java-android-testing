package com.felippeneves.tutorial_android_application.common.di;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.felippeneves.tutorial_android_application.common.time.TimeProvider;
import com.felippeneves.tutorial_android_application.networking.StackoverflowApi;
import com.felippeneves.tutorial_android_application.networking.questions.FetchLastActiveQuestionsEndpoint;
import com.felippeneves.tutorial_android_application.questions.FetchLastActiveQuestionsUseCase;
import com.felippeneves.tutorial_android_application.questions.FetchQuestionDetailsUseCase;
import com.felippeneves.tutorial_android_application.screens.common.ViewMvcFactory;
import com.felippeneves.tutorial_android_application.screens.common.controllers.BackPressDispatcher;
import com.felippeneves.tutorial_android_application.screens.common.fragment_frame_helper.FragmentFrameHelper;
import com.felippeneves.tutorial_android_application.screens.common.fragment_frame_helper.FragmentFrameWrapper;
import com.felippeneves.tutorial_android_application.screens.common.navdrawer.NavDrawerHelper;
import com.felippeneves.tutorial_android_application.screens.common.screens_navigator.ScreensNavigator;
import com.felippeneves.tutorial_android_application.screens.common.toasts_helper.ToastsHelper;
import com.felippeneves.tutorial_android_application.screens.question_details.QuestionDetailsController;
import com.felippeneves.tutorial_android_application.screens.questions_list.QuestionsListController;

public class ControllerCompositionRoot {

    private final CompositionRoot mCompositionRoot;
    private final FragmentActivity mActivity;

    public ControllerCompositionRoot(CompositionRoot compositionRoot, FragmentActivity activity) {
        mCompositionRoot = compositionRoot;
        mActivity = activity;
    }

    private FragmentActivity getActivity() {
        return mActivity;
    }

    private Context getContext() {
        return mActivity;
    }

    private FragmentManager getFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    private StackoverflowApi getStackoverflowApi() {
        return mCompositionRoot.getStackoverflowApi();
    }

    private FetchLastActiveQuestionsEndpoint getFetchLastActiveQuestionsEndpoint() {
        return new FetchLastActiveQuestionsEndpoint(getStackoverflowApi());
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(getContext());
    }

    public ViewMvcFactory getViewMvcFactory() {
        return new ViewMvcFactory(getLayoutInflater(), getNavDrawerHelper());
    }

    private NavDrawerHelper getNavDrawerHelper() {
        return (NavDrawerHelper) getActivity();
    }

    public FetchQuestionDetailsUseCase getFetchQuestionDetailsUseCase() {
        return mCompositionRoot.getFetchQuestionDetailsUseCase();
    }

    public FetchLastActiveQuestionsUseCase getFetchLastActiveQuestionsUseCase() {
        return new FetchLastActiveQuestionsUseCase(getFetchLastActiveQuestionsEndpoint());
    }

    public TimeProvider getTimeProvider() {
        return mCompositionRoot.getTimeProvider();
    }

    public QuestionsListController getQuestionsListController() {
        return new QuestionsListController(
                getFetchLastActiveQuestionsUseCase(),
                getScreensNavigator(),
                getToastsHelper(),
                getTimeProvider());
    }

    public ToastsHelper getToastsHelper() {
        return new ToastsHelper(getContext());
    }

    public ScreensNavigator getScreensNavigator() {
        return new ScreensNavigator(getFragmentFrameHelper());
    }

    private FragmentFrameHelper getFragmentFrameHelper() {
        return new FragmentFrameHelper(getActivity(), getFragmentFrameWrapper(), getFragmentManager());
    }

    private FragmentFrameWrapper getFragmentFrameWrapper() {
        return (FragmentFrameWrapper) getActivity();
    }

    public BackPressDispatcher getBackPressDispatcher() {
        return (BackPressDispatcher) getActivity();
    }

    public QuestionDetailsController getQuestionDetailsController() {
        return new QuestionDetailsController(getFetchQuestionDetailsUseCase(), getScreensNavigator(), getToastsHelper());
    }
}

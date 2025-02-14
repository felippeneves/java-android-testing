package com.felippeneves.tutorial_android_application.screens.common.screens_navigator;

import com.felippeneves.tutorial_android_application.screens.common.fragment_frame_helper.FragmentFrameHelper;
import com.felippeneves.tutorial_android_application.screens.question_details.QuestionDetailsFragment;
import com.felippeneves.tutorial_android_application.screens.questions_list.QuestionsListFragment;

public class ScreensNavigator {

    private FragmentFrameHelper mFragmentFrameHelper;

    public ScreensNavigator(FragmentFrameHelper fragmentFrameHelper) {
        mFragmentFrameHelper = fragmentFrameHelper;
    }

    public void toQuestionDetails(String questionId) {
        mFragmentFrameHelper.replaceFragment(QuestionDetailsFragment.newInstance(questionId));
    }

    public void toQuestionsList() {
        mFragmentFrameHelper.replaceFragmentAndClearBackstack(QuestionsListFragment.newInstance());
    }

    public void navigateUp() {
        mFragmentFrameHelper.navigateUp();
    }
}

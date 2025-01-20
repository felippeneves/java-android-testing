package com.felippeneves.tutorial_android_application.screens.question_details;

import com.felippeneves.tutorial_android_application.questions.FetchQuestionDetailsUseCase;
import com.felippeneves.tutorial_android_application.questions.QuestionDetails;
import com.felippeneves.tutorial_android_application.screens.common.screens_navigator.ScreensNavigator;
import com.felippeneves.tutorial_android_application.screens.common.toasts_helper.ToastsHelper;

public class QuestionDetailsController implements QuestionDetailsViewMvc.Listener, FetchQuestionDetailsUseCase.Listener {

    private final FetchQuestionDetailsUseCase mFetchQuestionDetailsUseCase;
    private final ScreensNavigator mScreensNavigator;
    private final ToastsHelper mToastsHelper;

    private String mQuestionId;
    private QuestionDetailsViewMvc mViewMvc;

    public QuestionDetailsController(FetchQuestionDetailsUseCase fetchQuestionDetailsUseCase,
                                     ScreensNavigator screensNavigator,
                                     ToastsHelper toastsHelper) {
        mFetchQuestionDetailsUseCase = fetchQuestionDetailsUseCase;
        mScreensNavigator = screensNavigator;
        mToastsHelper = toastsHelper;
    }

    public void bindQuestionId(String questionId) {
        mQuestionId = questionId;
    }

    public void bindView(QuestionDetailsViewMvc viewMvc) {
        mViewMvc = viewMvc;
    }

    public void onStart() {
        mViewMvc.registerListener(this);
        mFetchQuestionDetailsUseCase.registerListener(this);

        mViewMvc.showProgressIndication();
        mFetchQuestionDetailsUseCase.fetchQuestionDetailsAndNotify(mQuestionId);
    }

    public void onStop() {
        mViewMvc.unregisterListener(this);
        mFetchQuestionDetailsUseCase.unregisterListener(this);
    }

    @Override
    public void onQuestionDetailsFetched(QuestionDetails questionDetails) {
        mViewMvc.bindQuestion(questionDetails);
        mViewMvc.hideProgressIndication();
    }

    @Override
    public void onQuestionDetailsFetchFailed() {
        mViewMvc.hideProgressIndication();
        mToastsHelper.showUseCaseError();
    }

    @Override
    public void onNavigateUpClicked() {
        mScreensNavigator.navigateUp();
    }
}

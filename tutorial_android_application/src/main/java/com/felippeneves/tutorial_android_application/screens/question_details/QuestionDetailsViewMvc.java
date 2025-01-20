package com.felippeneves.tutorial_android_application.screens.question_details;

import com.felippeneves.tutorial_android_application.questions.QuestionDetails;
import com.felippeneves.tutorial_android_application.screens.common.views.ObservableViewMvc;

public interface QuestionDetailsViewMvc extends ObservableViewMvc<QuestionDetailsViewMvc.Listener> {

    interface Listener {
        void onNavigateUpClicked();
    }

    void bindQuestion(QuestionDetails question);

    void showProgressIndication();

    void hideProgressIndication();
}

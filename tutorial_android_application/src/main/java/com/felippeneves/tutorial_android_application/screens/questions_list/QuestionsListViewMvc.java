package com.felippeneves.tutorial_android_application.screens.questions_list;

import com.felippeneves.tutorial_android_application.questions.Question;
import com.felippeneves.tutorial_android_application.screens.common.views.ObservableViewMvc;

import java.util.List;

public interface QuestionsListViewMvc extends ObservableViewMvc<QuestionsListViewMvc.Listener> {

    interface Listener {
        void onQuestionClicked(Question question);
    }

    void bindQuestions(List<Question> questions);

    void showProgressIndication();

    void hideProgressIndication();
}

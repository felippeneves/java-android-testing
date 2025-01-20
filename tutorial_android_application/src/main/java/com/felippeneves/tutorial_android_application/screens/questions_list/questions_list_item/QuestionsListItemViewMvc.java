package com.felippeneves.tutorial_android_application.screens.questions_list.questions_list_item;

import com.felippeneves.tutorial_android_application.questions.Question;
import com.felippeneves.tutorial_android_application.screens.common.views.ObservableViewMvc;

public interface QuestionsListItemViewMvc extends ObservableViewMvc<QuestionsListItemViewMvc.Listener> {

    interface Listener {
        void onQuestionClicked(Question question);
    }

    void bindQuestion(Question question);
}

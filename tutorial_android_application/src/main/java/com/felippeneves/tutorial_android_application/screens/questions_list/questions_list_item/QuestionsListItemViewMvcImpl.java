package com.felippeneves.tutorial_android_application.screens.questions_list.questions_list_item;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.felippeneves.tutorial_android_application.R;
import com.felippeneves.tutorial_android_application.questions.Question;
import com.felippeneves.tutorial_android_application.screens.common.views.BaseObservableViewMvc;

public class QuestionsListItemViewMvcImpl extends BaseObservableViewMvc<QuestionsListItemViewMvc.Listener>
        implements QuestionsListItemViewMvc {

    private final TextView mTxtTitle;

    private Question mQuestion;

    public QuestionsListItemViewMvcImpl(LayoutInflater inflater, @Nullable ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.layout_question_list_item, parent, false));

        mTxtTitle = findViewById(R.id.txt_title);
        getRootView().setOnClickListener(view -> {
            for (Listener listener : getListeners()) {
                listener.onQuestionClicked(mQuestion);
            }
        });
    }

    @Override
    public void bindQuestion(Question question) {
        mQuestion = question;
        mTxtTitle.setText(question.getTitle());
    }
}

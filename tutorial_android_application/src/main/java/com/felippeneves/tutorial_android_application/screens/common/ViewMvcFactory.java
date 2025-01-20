package com.felippeneves.tutorial_android_application.screens.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.felippeneves.tutorial_android_application.screens.common.navdrawer.NavDrawerHelper;
import com.felippeneves.tutorial_android_application.screens.common.navdrawer.NavDrawerViewMvc;
import com.felippeneves.tutorial_android_application.screens.common.navdrawer.NavDrawerViewMvcImpl;
import com.felippeneves.tutorial_android_application.screens.common.toolbar.ToolbarViewMvc;
import com.felippeneves.tutorial_android_application.screens.question_details.QuestionDetailsViewMvc;
import com.felippeneves.tutorial_android_application.screens.question_details.QuestionDetailsViewMvcImpl;
import com.felippeneves.tutorial_android_application.screens.questions_list.QuestionsListViewMvc;
import com.felippeneves.tutorial_android_application.screens.questions_list.QuestionsListViewMvcImpl;
import com.felippeneves.tutorial_android_application.screens.questions_list.questions_list_item.QuestionsListItemViewMvc;
import com.felippeneves.tutorial_android_application.screens.questions_list.questions_list_item.QuestionsListItemViewMvcImpl;

public class ViewMvcFactory {

    private final LayoutInflater mLayoutInflater;
    private final NavDrawerHelper mNavDrawerHelper;

    public ViewMvcFactory(LayoutInflater layoutInflater, NavDrawerHelper navDrawerHelper) {
        mLayoutInflater = layoutInflater;
        mNavDrawerHelper = navDrawerHelper;
    }

    public QuestionsListViewMvc getQuestionsListViewMvc(@Nullable ViewGroup parent) {
        return new QuestionsListViewMvcImpl(mLayoutInflater, parent, mNavDrawerHelper, this);
    }

    public QuestionsListItemViewMvc getQuestionsListItemViewMvc(@Nullable ViewGroup parent) {
        return new QuestionsListItemViewMvcImpl(mLayoutInflater, parent);
    }

    public QuestionDetailsViewMvc getQuestionDetailsViewMvc(@Nullable ViewGroup parent) {
        return new QuestionDetailsViewMvcImpl(mLayoutInflater, parent, this);
    }

    public ToolbarViewMvc getToolbarViewMvc(@Nullable ViewGroup parent) {
        return new ToolbarViewMvc(mLayoutInflater, parent);
    }

    public NavDrawerViewMvc getNavDrawerViewMvc(@Nullable ViewGroup parent) {
        return new NavDrawerViewMvcImpl(mLayoutInflater, parent);
    }
}

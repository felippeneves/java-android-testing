package com.felippeneves.tutorial_android_application.data;

import com.felippeneves.tutorial_android_application.questions.QuestionDetails;

public class QuestionDetailsTestData {

    public static final String TITLE_1 = "title1";
    public static final String ID_1 = "id1";
    public static final String BODY_1 = "body1";

    public static final String TITLE_2 = "title2";
    public static final String ID_2 = "id2";
    public static final String BODY_2 = "body2";

    public static QuestionDetails getQuestionDetails1() {
        return new QuestionDetails(ID_1, TITLE_1, BODY_1);
    }

    public static QuestionDetails getQuestionDetails2() {
        return new QuestionDetails(TITLE_2, ID_2, BODY_2);
    }
}

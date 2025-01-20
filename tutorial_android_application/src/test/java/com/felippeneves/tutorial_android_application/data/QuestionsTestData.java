package com.felippeneves.tutorial_android_application.data;

import com.felippeneves.tutorial_android_application.networking.questions.QuestionSchema;
import com.felippeneves.tutorial_android_application.questions.Question;

import java.util.LinkedList;
import java.util.List;

public class QuestionsTestData {

    public static final String TITLE_1 = "title1";
    public static final String ID_1 = "id1";
    public static final String BODY_1 = "body1";

    public static final String TITLE_2 = "title2";
    public static final String ID_2 = "id2";
    public static final String BODY_2 = "body2";

    public static Question getQuestion() {
        return new Question(ID_1, TITLE_1);
    }

    public static List<Question> getQuestionList() {
        List<Question> questions = new LinkedList<>();
        questions.add(new Question(ID_1, TITLE_1));
        questions.add(new Question(ID_2, TITLE_2));
        return questions;
    }

    public static List<QuestionSchema> getQuestionSchemaList() {
        List<QuestionSchema> questionSchemaList = new LinkedList<>();
        questionSchemaList.add(new QuestionSchema(TITLE_1, ID_1, BODY_1));
        questionSchemaList.add(new QuestionSchema(TITLE_2, ID_2, BODY_2));
        return questionSchemaList;
    }
}

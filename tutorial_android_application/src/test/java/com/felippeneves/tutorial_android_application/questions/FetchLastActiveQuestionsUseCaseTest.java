package com.felippeneves.tutorial_android_application.questions;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import com.felippeneves.tutorial_android_application.data.QuestionsTestData;
import com.felippeneves.tutorial_android_application.networking.questions.FetchLastActiveQuestionsEndpoint;
import com.felippeneves.tutorial_android_application.networking.questions.QuestionSchema;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class FetchLastActiveQuestionsUseCaseTest {

    //region constants

    private static final List<Question> QUESTION_LIST = QuestionsTestData.getQuestionList();

    //endregion constants

    // region helper fields

    private EndpointTd mEndpointTd;
    @Mock
    private FetchLastActiveQuestionsUseCase.Listener mListener1;
    @Mock
    private FetchLastActiveQuestionsUseCase.Listener mListener2;

    @Captor
    private ArgumentCaptor<List<Question>> mQuestionListCaptor;

    // endregion helper fields

    // region SUT

    FetchLastActiveQuestionsUseCase SUT;

    // endregion SUT

    // region setup

    @Before
    public void setUp() {
        mEndpointTd = new EndpointTd();
        SUT = new FetchLastActiveQuestionsUseCase(mEndpointTd);
    }

    // endregion setup

    // region test methods

    // success - listeners notified of success with correct data

    @Test
    public void fetchLastActiveQuestionsAndNotify_success_listenersNotifiedWithCorrectData() {
        // Arrange
        success();
        SUT.registerListener(mListener1);
        SUT.registerListener(mListener2);
        // Act
        SUT.fetchLastActiveQuestionsAndNotify();
        // Assert
        verify(mListener1).onLastActiveQuestionsFetched(mQuestionListCaptor.capture());
        verify(mListener2).onLastActiveQuestionsFetched(mQuestionListCaptor.capture());
        List<List<Question>> questionList = mQuestionListCaptor.getAllValues();
        assertEquals(QUESTION_LIST, questionList.get(0));
        assertEquals(QUESTION_LIST, questionList.get(1));
    }

    // failure - listeners notified of failure

    @Test
    public void fetchLastActiveQuestionsAndNotify_failure_listenerNotifiedOfFailure() {
        // Arrange
        failure();
        SUT.registerListener(mListener1);
        SUT.registerListener(mListener2);
        // Act
        SUT.fetchLastActiveQuestionsAndNotify();
        // Assert
        verify(mListener1).onLastActiveQuestionsFetchFailed();
        verify(mListener2).onLastActiveQuestionsFetchFailed();
    }


    // endregion test methods

    // region helper methods

    private void success() {
        // currently no-op
    }

    private void failure() {
        mEndpointTd.mFailure = true;
    }

    // endregion helper methods

    // region helper classes

    private static class EndpointTd extends FetchLastActiveQuestionsEndpoint {

        public boolean mFailure;

        public EndpointTd() {
            super(null);
        }

        @Override
        public void fetchLastActiveQuestions(final Listener listener) {
            if (!mFailure) {
                listener.onQuestionsFetched(QuestionsTestData.getQuestionSchemaList());
            } else {
                listener.onQuestionsFetchFailed();
            }
        }
    }

    // endregion helper classes
}

package com.felippeneves.tutorial_android_application.questions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import androidx.annotation.NonNull;

import com.felippeneves.tutorial_android_application.common.time.TimeProvider;
import com.felippeneves.tutorial_android_application.data.QuestionDetailsTestData;
import com.felippeneves.tutorial_android_application.networking.questions.FetchQuestionDetailsEndpoint;
import com.felippeneves.tutorial_android_application.networking.questions.QuestionSchema;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class FetchQuestionDetailsUseCaseTest {

    //region constants

    private static final long CACHE_TIMEOUT = 60000;
    private static final QuestionDetails QUESTION_DETAILS_1 = QuestionDetailsTestData.getQuestionDetails1();
    private static final String QUESTION_ID_1 = QUESTION_DETAILS_1.getId();
    private static final QuestionDetails QUESTION_DETAILS_2 = QuestionDetailsTestData.getQuestionDetails2();
    private static final String QUESTION_ID_2 = QUESTION_DETAILS_2.getId();

    //endregion constants

    // region helper fields

    @Mock
    FetchQuestionDetailsEndpoint mFetchQuestionDetailsEndpointMock;

    @Mock
    TimeProvider mTimeProviderMock;

    private ListenerTd mListener1;
    private ListenerTd mListener2;

    private int mEndpointCallsCount;

    // endregion helper fields

    // region SUT

    FetchQuestionDetailsUseCase SUT;

    // endregion SUT

    // region setup

    @Before
    public void setUp() {
        SUT = new FetchQuestionDetailsUseCase(mFetchQuestionDetailsEndpointMock, mTimeProviderMock);

        mListener1 = new ListenerTd();
        mListener2 = new ListenerTd();
        SUT.registerListener(mListener1);
        SUT.registerListener(mListener2);
    }

    // endregion setup

    // region test methods

    @Test
    public void fetchQuestionDetailsAndNotify_success_listenersNotifiedWithCorrectData() {
        // Arrange
        success();
        // Act
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_1);
        // Assert
        mListener1.assertSuccessfulCalls(1);
        assertEquals(QUESTION_DETAILS_1, mListener1.getLastData());
        mListener2.assertSuccessfulCalls(1);
        assertEquals(QUESTION_DETAILS_1, mListener1.getLastData());
    }

    @Test
    public void fetchQuestionDetailsAndNotify_failure_listenersNotifiedOfFailure() {
        // Arrange
        failure();
        // Act
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_1);
        // Assert
        mListener1.assertOneFailingCall();
        mListener2.assertOneFailingCall();
    }

    @Test
    public void fetchQuestionDetailsAndNotify_secondTimeImmediatelyAfterSuccess_listenersNotifiedWithDataFromCache() {
        // Arrange
        success();
        // Act
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_1);
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_1);
        // Assert
        mListener1.assertSuccessfulCalls(2);
        assertEquals(QUESTION_DETAILS_1, mListener1.getLastData());
        mListener2.assertSuccessfulCalls(2);
        assertEquals(QUESTION_DETAILS_1, mListener2.getLastData());
        assertEquals(1, mEndpointCallsCount);
    }

    @Test
    public void fetchQuestionDetailsAndNotify_secondTimeRightBeforeTimeoutAfterSuccess_listenersNotifiedWithDataFromCache() {
        // Arrange
        success();
        when(mTimeProviderMock.getCurrentTimestamp()).thenReturn(0l);
        // Act
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_1);
        when(mTimeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT - 1);
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_1);
        // Assert
        mListener1.assertSuccessfulCalls(2);
        assertEquals(QUESTION_DETAILS_1, mListener1.getLastData());
        mListener2.assertSuccessfulCalls(2);
        assertEquals(QUESTION_DETAILS_1, mListener2.getLastData());
        assertEquals(1, mEndpointCallsCount);
    }

    @Test
    public void fetchQuestionDetailsAndNotify_secondTimeRightAfterTimeoutAfterSuccess_listenersNotifiedWithDataFromEndpoint() {
        // Arrange
        success();
        when(mTimeProviderMock.getCurrentTimestamp()).thenReturn(0l);
        // Act
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_1);
        when(mTimeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT);
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_1);
        // Assert
        mListener1.assertSuccessfulCalls(2);
        assertEquals(QUESTION_DETAILS_1, mListener1.getLastData());
        mListener2.assertSuccessfulCalls(2);
        assertEquals(QUESTION_DETAILS_1, mListener2.getLastData());
        assertEquals(2, mEndpointCallsCount);
    }

    @Test
    public void fetchQuestionDetailsAndNotify_secondTimeWithDifferentIdAfterSuccess_listenersNotifiedWithDataFromEndpoint() {
        // Arrange
        success();
        // Act
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_1);
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_2);
        // Assert
        mListener1.assertSuccessfulCalls(2);
        assertEquals(QUESTION_DETAILS_2, mListener1.getLastData());
        mListener2.assertSuccessfulCalls(2);
        assertEquals(QUESTION_DETAILS_2, mListener1.getLastData());
        assertEquals(2, mEndpointCallsCount);
    }

    @Test
    public void fetchQuestionDetailsAndNotify_afterTwoDifferentQuestionsAtDifferentTimesFirstQuestionRightBeforeTimeout_listenersNotifiedWithDataFromCache() {
        // Arrange
        success();
        when(mTimeProviderMock.getCurrentTimestamp()).thenReturn(0l);
        // Act
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_1);
        when(mTimeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT / 2);
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_2);
        when(mTimeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT - 1);
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_1);
        // Assert
        mListener1.assertSuccessfulCalls(3);
        assertEquals(QUESTION_DETAILS_1, mListener1.getLastData());
        mListener2.assertSuccessfulCalls(3);
        assertEquals(QUESTION_DETAILS_1, mListener2.getLastData());
        assertEquals(2, mEndpointCallsCount);
    }

    @Test
    public void fetchQuestionDetailsAndNotify_afterTwoDifferentQuestionsAtDifferentTimesSecondQuestionRightBeforeTimeout_listenersNotifiedWithDataFromCache() throws Exception {
        // Arrange
        success();
        when(mTimeProviderMock.getCurrentTimestamp()).thenReturn(0l);
        // Act
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_1);
        when(mTimeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT / 2);
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_2);
        when(mTimeProviderMock.getCurrentTimestamp()).thenReturn(CACHE_TIMEOUT + (CACHE_TIMEOUT / 2) - 1);
        SUT.fetchQuestionDetailsAndNotify(QUESTION_ID_2);
        // Assert
        mListener1.assertSuccessfulCalls(3);
        assertEquals(QUESTION_DETAILS_2, mListener1.getLastData());
        mListener2.assertSuccessfulCalls(3);
        assertEquals(QUESTION_DETAILS_2, mListener2.getLastData());
        assertEquals(2, mEndpointCallsCount);
    }

    // endregion test methods

    // region helper methods

    private void success() {
        doAnswer(invocation -> {
            mEndpointCallsCount++;

            Object[] args = invocation.getArguments();
            String questionId = (String) args[0];
            FetchQuestionDetailsEndpoint.Listener listener = (FetchQuestionDetailsEndpoint.Listener) args[1];

            QuestionSchema response = getQuestionSchema(questionId);
            listener.onQuestionDetailsFetched(response);
            return null;
        }).when(mFetchQuestionDetailsEndpointMock).fetchQuestionDetails(
                any(String.class),
                any(FetchQuestionDetailsEndpoint.Listener.class)
        );
    }

    private void failure() {
        doAnswer(invocation -> {
            mEndpointCallsCount++;

            Object[] args = invocation.getArguments();
            FetchQuestionDetailsEndpoint.Listener listener = (FetchQuestionDetailsEndpoint.Listener) args[1];

            listener.onQuestionDetailsFetchFailed();
            return null;
        }).when(mFetchQuestionDetailsEndpointMock).fetchQuestionDetails(
                any(String.class),
                any(FetchQuestionDetailsEndpoint.Listener.class)
        );
    }

    @NonNull
    private static QuestionSchema getQuestionSchema(String questionId) {
        QuestionSchema response;
        if (questionId.equals(QUESTION_ID_1)) {
            response = new QuestionSchema(QUESTION_DETAILS_1.getTitle(), QUESTION_DETAILS_1.getId(), QUESTION_DETAILS_1.getBody());
        } else if (questionId.equals(QUESTION_ID_2)) {
            response = new QuestionSchema(QUESTION_DETAILS_2.getTitle(), QUESTION_DETAILS_2.getId(), QUESTION_DETAILS_2.getBody());
        } else {
            throw new RuntimeException("unhandled question id: " + questionId);
        }
        return response;
    }

    // endregion helper methods

    // region helper classes

    private static class ListenerTd implements FetchQuestionDetailsUseCase.Listener {
        private int mCallCount;
        private int mSuccessCount;
        private QuestionDetails mData;

        @Override
        public void onQuestionDetailsFetched(QuestionDetails questionDetails) {
            mCallCount++;
            mSuccessCount++;
            mData = questionDetails;
        }

        @Override
        public void onQuestionDetailsFetchFailed() {
            mCallCount++;
        }

        public void assertSuccessfulCalls(int count) {
            if (mCallCount != count || mCallCount != mSuccessCount) {
                throw new RuntimeException(count + " successful call(s) assertion failed; calls: " + mCallCount + "; successes: " + mSuccessCount);
            }
        }

        public void assertOneFailingCall() {
            if (mCallCount != 1 || mSuccessCount > 0) {
                throw new RuntimeException("one failing call assertion failed");
            }
        }

        public QuestionDetails getLastData() {
            return mData;
        }
    }

    // endregion helper classes
}

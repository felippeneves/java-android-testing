package com.felippeneves.tutorial_android_application.screens.questions_list;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.felippeneves.tutorial_android_application.common.time.TimeProvider;
import com.felippeneves.tutorial_android_application.data.QuestionsTestData;
import com.felippeneves.tutorial_android_application.questions.FetchLastActiveQuestionsUseCase;
import com.felippeneves.tutorial_android_application.questions.Question;
import com.felippeneves.tutorial_android_application.screens.common.screens_navigator.ScreensNavigator;
import com.felippeneves.tutorial_android_application.screens.common.toasts_helper.ToastsHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class QuestionsListControllerTest {

    //region constants

    private static final List<Question> QUESTION_LIST = QuestionsTestData.getQuestionList();
    private static final Question QUESTION = QuestionsTestData.getQuestion();

    //endregion constants

    // region helper fields

    private UseCaseTd mUseCaseTd;
    @Mock
    ScreensNavigator mScreensNavigatorMock;
    @Mock
    ToastsHelper mToastsHelperMock;
    @Mock
    QuestionsListViewMvc mQuestionsListViewMvcMock;
    @Mock
    TimeProvider mTimeProviderMock;

    // endregion helper fields

    // region SUT

    QuestionsListController SUT;

    // endregion SUT

    // region setup

    @Before
    public void setUp() {
        mUseCaseTd = new UseCaseTd();
        SUT = new QuestionsListController(mUseCaseTd, mScreensNavigatorMock, mToastsHelperMock, mTimeProviderMock);
        SUT.bindView(mQuestionsListViewMvcMock);
    }

    // endregion setup

    // region test methods

    @Test
    public void onStart_progressIndicationShown() {
        // Arrange
        success();
        // Act
        SUT.onStart();
        // Assert
        verify(mQuestionsListViewMvcMock).showProgressIndication();
    }

    @Test
    public void onStart_successfulResponse_progressIndicationHidden() {
        // Arrange
        success();
        // Act
        SUT.onStart();
        // Assert
        verify(mQuestionsListViewMvcMock).hideProgressIndication();
    }

    @Test
    public void onStart_failure_progressIndicationHidden() {
        // Arrange
        failure();
        // Act
        SUT.onStart();
        // Assert
        verify(mQuestionsListViewMvcMock).hideProgressIndication();
    }

    @Test
    public void onStart_successfulResponse_questionsBoundToView() {
        // Arrange
        success();
        // Act
        SUT.onStart();
        // Assert
        verify(mQuestionsListViewMvcMock).bindQuestions(QUESTION_LIST);
    }

    @Test
    public void onStart_secondTimeAfterSuccessfulResponse_questionsBoundToTheViewFromCache() {
        // Arrange
        success();
        // Act
        SUT.onStart();
        SUT.onStart();
        // Assert
        verify(mQuestionsListViewMvcMock, times(2)).bindQuestions(QUESTION_LIST);
        assertEquals(1, mUseCaseTd.getCallCount());
    }

    @Test
    public void onStart_failure_errorToastShow() {
        // Arrange
        failure();
        // Act
        SUT.onStart();
        // Assert
        verify(mToastsHelperMock).showUseCaseError();
    }

    @Test
    public void onStart_failure_questionsNotBoundToView() {
        // Arrange
        failure();
        // Act
        SUT.onStart();
        // Assert
        verify(mQuestionsListViewMvcMock, never()).bindQuestions(anyList());
    }

    @Test
    public void onStart_listenersRegistered() {
        // Arrange
        // Act
        SUT.onStart();
        // Assert
        verify(mQuestionsListViewMvcMock).registerListener(SUT);
        mUseCaseTd.verifyListenerRegistered(SUT);
    }

    @Test
    public void onStop_listenersUnregistered() {
        // Arrange
        // Act
        SUT.onStop();
        // Assert
        verify(mQuestionsListViewMvcMock).unregisterListener(SUT);
        mUseCaseTd.verifyListenerNotRegistered(SUT);
    }

    @Test
    public void onQuestionClicked_navigateToQuestionDetailsScreen() {
        // Arrange
        // Act
        SUT.onQuestionClicked(QUESTION);
        // Assert
        verify(mScreensNavigatorMock).toQuestionDetails(QUESTION.getId());
    }

    @Test
    public void onStart_secondTimeAfterCachingTimeout_questionsBoundToViewFromUseCase() throws Exception {
        // Arrange
        emptyQuestionsListOnFirstCall();
        when(mTimeProviderMock.getCurrentTimestamp()).thenReturn(0l);
        // Act
        SUT.onStart();
        SUT.onStop();
        when(mTimeProviderMock.getCurrentTimestamp()).thenReturn(10000l);
        SUT.onStart();
        // Assert
        verify(mQuestionsListViewMvcMock).bindQuestions(QUESTION_LIST);
    }

    @Test
    public void onStart_secondTimeRightBeforeCachingTimeout_questionsBoundToViewFromCache() throws Exception {
        // Arrange
        when(mTimeProviderMock.getCurrentTimestamp()).thenReturn(0l);
        // Act
        SUT.onStart();
        SUT.onStop();
        when(mTimeProviderMock.getCurrentTimestamp()).thenReturn(9999l);
        SUT.onStart();
        // Assert
        verify(mQuestionsListViewMvcMock, times(2)).bindQuestions(QUESTION_LIST);
        assertEquals(1, mUseCaseTd.getCallCount());
    }

    // endregion test methods

    // region helper methods

    private void success() {
        // currently no-op
    }

    private void failure() {
        mUseCaseTd.mFailure = true;
    }

    private void emptyQuestionsListOnFirstCall() {
        mUseCaseTd.mEmptyListOnFirstCall = true;
    }

    // endregion helper methods

    // region helper classes

    private static class UseCaseTd extends FetchLastActiveQuestionsUseCase {

        public boolean mEmptyListOnFirstCall;
        private boolean mFailure;
        private int mCallCount;

        public UseCaseTd() {
            super(null);
        }

        @Override
        public void fetchLastActiveQuestionsAndNotify() {
            mCallCount++;
            for (FetchLastActiveQuestionsUseCase.Listener listener : getListeners()) {
                if (mFailure) {
                    listener.onLastActiveQuestionsFetchFailed();
                } else {
                    if (mEmptyListOnFirstCall && mCallCount == 1) {
                        listener.onLastActiveQuestionsFetched(new LinkedList<>());
                    } else {
                        listener.onLastActiveQuestionsFetched(QUESTION_LIST);
                    }
                }
            }
        }

        public void verifyListenerRegistered(QuestionsListController candidate) {
            for (FetchLastActiveQuestionsUseCase.Listener listener : getListeners()) {
                if (listener == candidate) {
                    return;
                }
            }
            throw new RuntimeException("listener not registered");
        }

        public void verifyListenerNotRegistered(QuestionsListController candidate) {
            for (FetchLastActiveQuestionsUseCase.Listener listener : getListeners()) {
                if (listener == candidate) {
                    throw new RuntimeException("listener not registered");
                }
            }
        }

        public int getCallCount() {
            return mCallCount;
        }
    }

    // endregion helper classes
}

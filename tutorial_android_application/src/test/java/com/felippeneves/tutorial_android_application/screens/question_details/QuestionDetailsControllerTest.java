package com.felippeneves.tutorial_android_application.screens.question_details;

import static org.mockito.Mockito.verify;

import com.felippeneves.tutorial_android_application.data.QuestionDetailsTestData;
import com.felippeneves.tutorial_android_application.questions.FetchQuestionDetailsUseCase;
import com.felippeneves.tutorial_android_application.questions.QuestionDetails;
import com.felippeneves.tutorial_android_application.screens.common.screens_navigator.ScreensNavigator;
import com.felippeneves.tutorial_android_application.screens.common.toasts_helper.ToastsHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QuestionDetailsControllerTest {

    //region constants

    private static final QuestionDetails QUESTION_DETAILS = QuestionDetailsTestData.getQuestionDetails1();
    private static final String QUESTION_ID = QUESTION_DETAILS.getId();

    //endregion constants

    // region helper fields

    private UseCaseTd mUseCaseTd;

    @Mock
    private ScreensNavigator mScreensNavigatorMock;

    @Mock
    private ToastsHelper mToastsHelperMock;

    @Mock
    QuestionDetailsViewMvc mQuestionDetailsViewMvcMock;

    // endregion helper fields

    // region SUT

    QuestionDetailsController SUT;

    // endregion SUT

    // region setup

    @Before
    public void setUp() {
        mUseCaseTd = new UseCaseTd();
        SUT = new QuestionDetailsController(mUseCaseTd, mScreensNavigatorMock, mToastsHelperMock);
        SUT.bindView(mQuestionDetailsViewMvcMock);
        SUT.bindQuestionId(QUESTION_ID);
    }

    // endregion setup

    // region test methods

    @Test
    public void onStart_listenersRegistered() {
        // Arrange
        // Act
        SUT.onStart();
        // Assert
        verify(mQuestionDetailsViewMvcMock).registerListener(SUT);
        mUseCaseTd.verifyListenerRegistered(SUT);
    }

    @Test
    public void onStop_listenersUnregistered() {
        // Arrange
        SUT.onStart();
        // Act
        SUT.onStop();
        // Assert
        verify(mQuestionDetailsViewMvcMock).unregisterListener(SUT);
        mUseCaseTd.verifyListenerNotRegistered(SUT);
    }

    @Test
    public void onStart_success_questionDetailsBoundToView() {
        // Arrange
        success();
        // Act
        SUT.onStart();
        // Assert
        verify(mQuestionDetailsViewMvcMock).bindQuestion(QUESTION_DETAILS);
    }

    @Test
    public void onStart_failure_errorToastShown() {
        // Arrange
        failure();
        // Act
        SUT.onStart();
        // Assert
        verify(mToastsHelperMock).showUseCaseError();
    }

    @Test
    public void onStart_progressIndicationShown() {
        // Arrange
        // Act
        SUT.onStart();
        // Assert
        verify(mQuestionDetailsViewMvcMock).showProgressIndication();
    }

    @Test
    public void onStart_success_progressIndicationHidden() {
        // Arrange
        success();
        // Act
        SUT.onStart();
        // Assert
        verify(mQuestionDetailsViewMvcMock).hideProgressIndication();
    }

    @Test
    public void onStart_failure_progressIndicationShown() {
        // Arrange
        failure();
        // Act
        SUT.onStart();
        // Assert
        verify(mQuestionDetailsViewMvcMock).hideProgressIndication();
    }

    @Test
    public void onNavigateUpClicked_navigatedUp() {
        // Arrange
        // Act
        SUT.onNavigateUpClicked();
        // Assert
        verify(mScreensNavigatorMock).navigateUp();
    }

    // endregion test methods

    // region helper methods

    private void success() {
        // currently no-op
    }

    private void failure() {
        mUseCaseTd.mFailure = true;
    }

    // endregion helper methods

    // region helper classes

    private static class UseCaseTd extends FetchQuestionDetailsUseCase {

        private boolean mFailure;

        public UseCaseTd() {
            super(null, null);
        }

        @Override
        public void fetchQuestionDetailsAndNotify(String questionId) {
            if (!questionId.equals(QUESTION_ID)) {
                throw new RuntimeException("invalid question ID: " + questionId);
            }
            for (Listener listener : getListeners()) {
                if (mFailure) {
                    listener.onQuestionDetailsFetchFailed();
                } else {
                    listener.onQuestionDetailsFetched(QUESTION_DETAILS);
                }
            }
        }

        public void verifyListenerRegistered(QuestionDetailsController questionDetailsController) {
            for (Listener listener : getListeners()) {
                if (listener == questionDetailsController) {
                    return;
                }
            }
            throw new RuntimeException("listener not registered");
        }

        public void verifyListenerNotRegistered(QuestionDetailsController questionDetailsController) {
            for (Listener listener : getListeners()) {
                if (listener == questionDetailsController) {
                    throw new RuntimeException("listener registered");
                }
            }
        }
    }

    // endregion helper classes
}

package com.felippeneves.mockito_fundamentals.exercise5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.felippeneves.mockito_fundamentals.exercise5.UpdateUsernameUseCaseSync.UseCaseResult;
import com.felippeneves.mockito_fundamentals.exercise5.eventbus.EventBusPoster;
import com.felippeneves.mockito_fundamentals.exercise5.eventbus.UserDetailsChangedEvent;
import com.felippeneves.mockito_fundamentals.exercise5.networking.NetworkErrorException;
import com.felippeneves.mockito_fundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.felippeneves.mockito_fundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResult;
import com.felippeneves.mockito_fundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResultStatus;
import com.felippeneves.mockito_fundamentals.exercise5.users.User;
import com.felippeneves.mockito_fundamentals.exercise5.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UpdateUsernameUseCaseSyncTest {

    // region constants

    private static final String USER_ID = "userId";
    private static final String USERNAME = "username";

    // endregion constants

    // region helper fields

    @Mock UpdateUsernameHttpEndpointSync mUpdateUsernameHttpEndpointSyncMock;
    @Mock UsersCache mUsersCacheMock;
    @Mock EventBusPoster mEventBusPosterMock;

    // endregion helper fields

    UpdateUsernameUseCaseSync SUT;

    @Before
    public void setUp() {
        SUT = new UpdateUsernameUseCaseSync(mUpdateUsernameHttpEndpointSyncMock, mUsersCacheMock, mEventBusPosterMock);
    }

    // userId and username passed to the endpoint

    @Test
    public void updateUsername_success_userIdAndUsernamePassedToEndpoint() throws NetworkErrorException {
        success();
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.updateUsernameSync(USER_ID, USERNAME);
        // Assert
        verify(mUpdateUsernameHttpEndpointSyncMock).updateUsername(ac.capture(), ac.capture());
        List<String> captures = ac.getAllValues();
        assertEquals(USER_ID, captures.get(0));
        assertEquals(USERNAME, captures.get(1));
    }



    // if username update succeeds - user must be cached

    @Test
    public void updateUsername_success_userCached() throws NetworkErrorException {
        success();
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verify(mUsersCacheMock).cacheUser(ac.capture());
        User cachedUser = ac.getValue();
        assertEquals(USER_ID, cachedUser.getUserId());
        assertEquals(USERNAME, cachedUser.getUsername());
    }

    // if username update fails - user is not be cached

    @Test
    public void updateUsername_generalError_userNotCached() throws NetworkErrorException {
        generalError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mUsersCacheMock);
    }

    @Test
    public void updateUsername_authError_userNotCached() throws NetworkErrorException {
        authError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mUsersCacheMock);
    }

    @Test
    public void updateUsername_serverError_userNotCached() throws NetworkErrorException {
        serverError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mUsersCacheMock);
    }

    // if username update succeeds - UserDetailsChangedEvent must be posted to event bus

    @Test
    public void updateUsername_success_loggedInEventPosted() throws NetworkErrorException {
        success();
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verify(mEventBusPosterMock).postEvent(ac.capture());
        assertTrue(ac.getValue() instanceof UserDetailsChangedEvent);
    }

    // if username update fails - no UserDetailsChangedEvent must be posted to event bus

    @Test
    public void updateUsername_generalError_noInteractionWithEventBusPoster() throws NetworkErrorException {
        generalError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateUsername_authError_noInteractionWithEventBusPoster() throws NetworkErrorException {
        authError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    @Test
    public void updateUsername_serverError_noInteractionWithEventBusPoster() throws NetworkErrorException {
        serverError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(mEventBusPosterMock);
    }

    // if username update succeeds - success returned

    @Test
    public void updateUsername_success_successReturned() throws NetworkErrorException {
        success();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(UseCaseResult.SUCCESS, result);
    }

    // if username update fails - failure returned

    @Test
    public void updateUsername_generalError_failureReturned() throws NetworkErrorException {
        generalError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(UseCaseResult.FAILURE, result);
    }

    @Test
    public void updateUsername_authError_failureReturned() throws NetworkErrorException {
        authError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(UseCaseResult.FAILURE, result);
    }

    @Test
    public void updateUsername_serverError_failureReturned() throws NetworkErrorException {
        serverError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(UseCaseResult.FAILURE, result);
    }

    // if network error - network error returned

    @Test
    public void loginSync_newWorkError_networkErrorReturned() throws NetworkErrorException {
        networkError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(UseCaseResult.NETWORK_ERROR, result);
    }

    //region Helper methods

    private void success() throws NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(any(), any()))
                .thenReturn(new EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, USERNAME));
    }

    private void generalError() throws NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(any(), any()))
                .thenReturn(new EndpointResult(EndpointResultStatus.GENERAL_ERROR, null, null));
    }

    private void authError() throws NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(any(), any()))
                .thenReturn(new EndpointResult(EndpointResultStatus.AUTH_ERROR, null, null));
    }

    private void serverError() throws NetworkErrorException {
        when(mUpdateUsernameHttpEndpointSyncMock.updateUsername(any(), any()))
                .thenReturn(new EndpointResult(EndpointResultStatus.SERVER_ERROR, null, null));
    }

    private void networkError() throws NetworkErrorException {
        doThrow(new NetworkErrorException()).when(mUpdateUsernameHttpEndpointSyncMock).updateUsername(any(), any());
    }

    //endregion Helper methods
}
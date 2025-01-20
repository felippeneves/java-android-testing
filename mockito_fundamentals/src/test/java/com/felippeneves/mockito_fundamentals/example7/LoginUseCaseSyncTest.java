package com.felippeneves.mockito_fundamentals.example7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import android.accounts.NetworkErrorException;

import com.felippeneves.mockito_fundamentals.example7.authtoken.AuthTokenCache;
import com.felippeneves.mockito_fundamentals.example7.eventbus.EventBusPoster;
import com.felippeneves.mockito_fundamentals.example7.eventbus.LoggedInEvent;
import com.felippeneves.mockito_fundamentals.example7.networking.LoginHttpEndpointSync;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

public class LoginUseCaseSyncTest {

    LoginUseCaseSync SUT;

    LoginHttpEndpointSync mLoginHttpEndpointSyncMock;
    AuthTokenCache authTokenCacheMock;
    EventBusPoster mEventBusPosterMock;

    //region Constants

    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static String AUTH_TOKEN = "authToken";

    //endregion Constants

    @Before
    public void setUp() {
        mLoginHttpEndpointSyncMock = mock(LoginHttpEndpointSync.class);
        authTokenCacheMock = mock(AuthTokenCache.class);
        mEventBusPosterMock = mock(EventBusPoster.class);
        SUT = new LoginUseCaseSync(mLoginHttpEndpointSyncMock, authTokenCacheMock, mEventBusPosterMock);
    }

    // username and password passed to the endpoint

    @Test
    public void loginSync_success_usernameAndPasswordPassedToEndpoint() throws NetworkErrorException {
        success();
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.loginSync(USERNAME, PASSWORD);
        verify(mLoginHttpEndpointSyncMock).loginSync(ac.capture(), ac.capture());
        List<String> captures = ac.getAllValues();
        assertEquals(USERNAME, captures.get(0));
        assertEquals(PASSWORD, captures.get(1));
    }

    // if login succeeds - user's auth token must be cached

    @Test
    public void loginSync_success_authTokenCached() throws NetworkErrorException {
        success();
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.loginSync(USERNAME, PASSWORD);
        verify(authTokenCacheMock).cacheAuthToken(ac.capture());
        assertEquals(AUTH_TOKEN, ac.getValue());
    }

    // if login fails - auth token is not changed

    @Test
    public void loginSync_generalError_authTokenNotCached() throws NetworkErrorException {
        generalError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(authTokenCacheMock);
    }

    @Test
    public void loginSync_authError_authTokenNotCached() throws NetworkErrorException {
        authError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(authTokenCacheMock);
    }

    @Test
    public void loginSync_serverError_authTokenNotCached() throws NetworkErrorException {
        serverError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(authTokenCacheMock);
    }

    // if login succeeds - login event must be posted to event bus

    @Test
    public void loginSync_success_loggedInEventBusPosted() throws NetworkErrorException {
        success();
        ArgumentCaptor<Object> ac = ArgumentCaptor.forClass(Object.class);
        SUT.loginSync(USERNAME, PASSWORD);
        verify(mEventBusPosterMock).postEvent(ac.capture());
        assertTrue(ac.getValue() instanceof LoggedInEvent);
    }

    // if login fails - no login event must be posted to event bus

    @Test
    public void loginSync_generalError_noInteractionWithEventBusPosted() throws NetworkErrorException {
        generalError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(authTokenCacheMock);
    }

    @Test
    public void loginSync_authError_noInteractionWithEventBusPosted() throws NetworkErrorException {
        authError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(authTokenCacheMock);
    }

    @Test
    public void loginSync_serverError_noInteractionWithEventBusPosted() throws NetworkErrorException {
        serverError();
        SUT.loginSync(USERNAME, PASSWORD);
        verifyNoMoreInteractions(authTokenCacheMock);
    }

    // if login succeeds - success returned

    @Test
    public void loginSync_success_successReturned() throws NetworkErrorException {
        success();
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(LoginUseCaseSync.UseCaseResult.SUCCESS, result);
    }

    // if login fails - failure returned

    @Test
    public void loginSync_generalError_failureReturned() throws NetworkErrorException {
        generalError();
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result);
    }

    @Test
    public void loginSync_authError_failureReturned() throws NetworkErrorException {
        authError();
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result);
    }

    @Test
    public void loginSync_serverError_failureReturned() throws NetworkErrorException {
        authError();
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result);
    }

    // if network error - network error returned

    @Test
    public void loginSync_newWorkError_networkErrorReturned() throws NetworkErrorException {
        networkError();
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(LoginUseCaseSync.UseCaseResult.NETWORK_ERROR, result);
    }

    //region Helper methods

    private void success() throws NetworkErrorException {
        when(mLoginHttpEndpointSyncMock.loginSync(any(), any()))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SUCCESS, AUTH_TOKEN));
    }

    private void generalError() throws NetworkErrorException {
        when(mLoginHttpEndpointSyncMock.loginSync(any(), any()))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, null));
    }

    private void authError() throws NetworkErrorException {
        when(mLoginHttpEndpointSyncMock.loginSync(any(), any()))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.AUTH_ERROR, null));
    }

    private void serverError() throws NetworkErrorException {
        when(mLoginHttpEndpointSyncMock.loginSync(any(), any()))
                .thenReturn(new LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, null));
    }

    private void networkError() throws NetworkErrorException {
        doThrow(new NetworkErrorException()).when(mLoginHttpEndpointSyncMock).loginSync(any(), any());
    }

    //endregion Helper methods
}

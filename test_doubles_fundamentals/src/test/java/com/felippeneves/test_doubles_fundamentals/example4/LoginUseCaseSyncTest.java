package com.felippeneves.test_doubles_fundamentals.example4;

import static org.junit.Assert.*;

import com.felippeneves.test_doubles_fundamentals.example4.authtoken.AuthTokenCache;
import com.felippeneves.test_doubles_fundamentals.example4.eventbus.EventBusPoster;
import com.felippeneves.test_doubles_fundamentals.example4.eventbus.LoggedInEvent;
import com.felippeneves.test_doubles_fundamentals.example4.networking.LoginHttpEndpointSync;
import com.felippeneves.test_doubles_fundamentals.example4.networking.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;

public class LoginUseCaseSyncTest {

    LoginHttpEndpointSyncTd mLoginHttpEndpointSyncTd;
    AuthTokenCacheTd mAuthTokenCacheTd;
    EventBusPosterTd mEventBusPosterTd;

    LoginUseCaseSync SUT;

    //region Constants

    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static String AUTH_TOKEN = "authToken";

    //endregion Constants

    @Before
    public void setUp() {
        mLoginHttpEndpointSyncTd = new LoginHttpEndpointSyncTd();
        mAuthTokenCacheTd = new AuthTokenCacheTd();
        mEventBusPosterTd = new EventBusPosterTd();
        SUT = new LoginUseCaseSync(mLoginHttpEndpointSyncTd, mAuthTokenCacheTd, mEventBusPosterTd);
    }

    // username and password passed to the endpoint

    @Test
    public void loginSync_success_usernameAndPasswordPassedToEndpoint() {
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(USERNAME, mLoginHttpEndpointSyncTd.mUsername);
        assertEquals(PASSWORD, mLoginHttpEndpointSyncTd.mPassword);
    }

    // if login succeeds - user's auth token must be cached

    @Test
    public void loginSync_success_authTokenCached() {
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(AUTH_TOKEN, mAuthTokenCacheTd.mAuthToken);
    }

    // if login fails - auth token is not changed

    @Test
    public void loginSync_generalError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd.mIsGeneralError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertNull(mAuthTokenCacheTd.mAuthToken);
    }

    @Test
    public void loginSync_authError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd.mIsAuthError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertNull(mAuthTokenCacheTd.mAuthToken);
    }

    @Test
    public void loginSync_serverError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd.mIsServerError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertNull(mAuthTokenCacheTd.mAuthToken);
    }

    // if login succeeds - login event must be posted to event bus

    @Test
    public void loginSync_success_loggedInEventBusPosted() {
        SUT.loginSync(USERNAME, PASSWORD);
        assertTrue(mEventBusPosterTd.mEvent instanceof LoggedInEvent);
    }

    // if login fails - no login event must be posted to event bus

    @Test
    public void loginSync_generalError_noInteractionWithEventBusPosted() {
        mLoginHttpEndpointSyncTd.mIsGeneralError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(0, mEventBusPosterTd.mInteractionsCount);
    }

    @Test
    public void loginSync_authError_noInteractionWithEventBusPosted() {
        mLoginHttpEndpointSyncTd.mIsAuthError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(0, mEventBusPosterTd.mInteractionsCount);
    }

    @Test
    public void loginSync_serverError_noInteractionWithEventBusPosted() {
        mLoginHttpEndpointSyncTd.mIsServerError = true;
        SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(0, mEventBusPosterTd.mInteractionsCount);
    }

    // if login succeeds - success returned

    @Test
    public void loginSync_success_successReturned() {
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(LoginUseCaseSync.UseCaseResult.SUCCESS, result);
    }

    // if login fails - failure returned

    @Test
    public void loginSync_generalError_failureReturned() {
        mLoginHttpEndpointSyncTd.mIsGeneralError = true;
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result);
    }

    @Test
    public void loginSync_authError_failureReturned() {
        mLoginHttpEndpointSyncTd.mIsAuthError = true;
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result);
    }

    @Test
    public void loginSync_serverError_failureReturned() {
        mLoginHttpEndpointSyncTd.mIsServerError = true;
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result);
    }

    // if network error - network error returned

    @Test
    public void loginSync_newWorkError_networkErrorReturned() {
        mLoginHttpEndpointSyncTd.mIsNetworkError = true;
        LoginUseCaseSync.UseCaseResult result = SUT.loginSync(USERNAME, PASSWORD);
        assertEquals(LoginUseCaseSync.UseCaseResult.NETWORK_ERROR, result);
    }

    //region Helper classes

    private static class LoginHttpEndpointSyncTd implements LoginHttpEndpointSync {
        public String mUsername;
        public String mPassword;
        public boolean mIsGeneralError;
        public boolean mIsAuthError;
        public boolean mIsServerError;
        public boolean mIsNetworkError;

        @Override
        public EndpointResult loginSync(String username, String password) throws NetworkErrorException {
            mUsername = username;
            mPassword = password;
            if (mIsGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, null);
            } else if (mIsAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, null);
            } else if (mIsServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, null);
            } else if (mIsNetworkError) {
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, AUTH_TOKEN);
            }
        }
    }

    private static class AuthTokenCacheTd implements AuthTokenCache {
        public String mAuthToken;

        @Override
        public void cacheAuthToken(String authToken) {
            mAuthToken = authToken;
        }

        @Override
        public String getAuthToken() {
            return mAuthToken;
        }
    }

    private static class EventBusPosterTd implements EventBusPoster {
        public Object mEvent;
        public int mInteractionsCount;

        @Override
        public void postEvent(Object event) {
            mInteractionsCount++;
            mEvent = event;
        }
    }

    //endregion Helper classes
}

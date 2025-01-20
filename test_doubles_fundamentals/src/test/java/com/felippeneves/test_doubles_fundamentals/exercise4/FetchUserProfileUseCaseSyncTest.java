package com.felippeneves.test_doubles_fundamentals.exercise4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import androidx.annotation.Nullable;

import com.felippeneves.test_doubles_fundamentals.example4.networking.NetworkErrorException;
import com.felippeneves.test_doubles_fundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.felippeneves.test_doubles_fundamentals.exercise4.users.User;
import com.felippeneves.test_doubles_fundamentals.exercise4.users.UsersCache;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FetchUserProfileUseCaseSyncTest {

    UserProfileHttpEndpointSyncTd mUserProfileHttpEndpointSyncTd;
    UsersCacheTd mUsersCacheTd;

    FetchUserProfileUseCaseSync SUT;

    //region Constants

    public static final String USER_ID = "userId";
    public static final String FULL_NAME = "fullName";
    public static final String IMAGE_URL = "imageUrl";

    //endregion Constants

    @Before
    public void setUp() {
        mUserProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
        mUsersCacheTd = new UsersCacheTd();
        SUT = new FetchUserProfileUseCaseSync(mUserProfileHttpEndpointSyncTd, mUsersCacheTd);
    }

    // userId passed to the endpoint

    @Test
    public void fetchUserProfileSync_success_userIdPassedToEndpoint() {
        SUT.fetchUserProfileSync(USER_ID);
        assertEquals(USER_ID, mUserProfileHttpEndpointSyncTd.mUserId);
    }

    // if fetch User Profile succeeds - user must be cached

    @Test
    public void fetchUserProfileSync_success_userCached() {
        SUT.fetchUserProfileSync(USER_ID);
        User cachedUser = mUsersCacheTd.getUser(USER_ID);
        assertEquals(USER_ID, cachedUser.getUserId());
        assertEquals(FULL_NAME, cachedUser.getFullName());
        assertEquals(IMAGE_URL, cachedUser.getImageUrl());
    }

    // if fetch User Profile fails - user is not be cached

    @Test
    public void fetchUserProfileSync_generalError_userNotCached() {
        mUserProfileHttpEndpointSyncTd.mIsGeneralError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertNull(mUsersCacheTd.getUser(USER_ID));
    }

    @Test
    public void fetchUserProfileSync_authError_userNotCached() {
        mUserProfileHttpEndpointSyncTd.mIsAuthError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertNull(mUsersCacheTd.getUser(USER_ID));
    }

    @Test
    public void fetchUserProfileSync_serverError_userNotCached() {
        mUserProfileHttpEndpointSyncTd.mIsServerError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertNull(mUsersCacheTd.getUser(USER_ID));
    }

    // if fetch User Profile succeeds - success returned

    @Test
    public void fetchUserProfileSync_success_successReturned() {
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS, result);
    }

    // if  fetch User Profile fails - failure returned

    @Test
    public void fetchUserProfileSync_generalError_failureReturned() {
        mUserProfileHttpEndpointSyncTd.mIsGeneralError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE, result);
    }

    @Test
    public void fetchUserProfileSync_authError_failureReturned() {
        mUserProfileHttpEndpointSyncTd.mIsAuthError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE, result);
    }

    @Test
    public void fetchUserProfileSync_serverError_failureReturned() {
        mUserProfileHttpEndpointSyncTd.mIsServerError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE, result);
    }

    // if network error - network error returned

    @Test
    public void fetchUserProfileSync_newWorkError_networkErrorReturned() {
        mUserProfileHttpEndpointSyncTd.mIsNetworkError = true;
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(USER_ID);
        assertEquals(FetchUserProfileUseCaseSync.UseCaseResult.NETWORK_ERROR, result);
    }

    //region Helper classes

    private static class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync {
        public String mUserId = "";
        public boolean mIsGeneralError;
        public boolean mIsAuthError;
        public boolean mIsServerError;
        public boolean mIsNetworkError;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            mUserId = userId;
            if (mIsGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", "", "");
            } else if (mIsAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", "", "");
            } else if (mIsServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", "", "");
            } else if (mIsNetworkError) {
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, FULL_NAME, IMAGE_URL);
            }
        }
    }

    private static class UsersCacheTd implements UsersCache {

        private List<User> mUsers = new ArrayList<>();

        @Override
        public void cacheUser(User user) {
            User existingUser = getUser(user.getUserId());
            if (existingUser != null) {
                mUsers.remove(existingUser);
            }
            mUsers.add(user);
        }

        @Override
        @Nullable
        public User getUser(String userId) {
            for (User user : mUsers) {
                if (user.getUserId().equals(userId)) {
                    return user;
                }
            }
            return null;
        }
    }

    //endregion Helper classes
}

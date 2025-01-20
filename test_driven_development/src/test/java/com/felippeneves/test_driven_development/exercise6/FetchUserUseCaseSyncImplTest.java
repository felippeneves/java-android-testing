package com.felippeneves.test_driven_development.exercise6;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.felippeneves.test_driven_development.exercise6.FetchUserUseCaseSync.UseCaseResult;
import com.felippeneves.test_driven_development.exercise6.networking.FetchUserHttpEndpointSync.EndpointResult;
import com.felippeneves.test_driven_development.exercise6.networking.FetchUserHttpEndpointSync.EndpointStatus;
import com.felippeneves.test_driven_development.exercise6.networking.FetchUserHttpEndpointSyncImpl;
import com.felippeneves.test_driven_development.exercise6.networking.NetworkErrorException;
import com.felippeneves.test_driven_development.exercise6.users.User;
import com.felippeneves.test_driven_development.exercise6.users.UsersCacheImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FetchUserUseCaseSyncImplTest {

    //region constants

    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final User USER = new User(USER_ID, USERNAME);

    //endregion constants

    // region helper fields

    @Mock
    FetchUserHttpEndpointSyncImpl mFetchUserHttpEndpointSyncMock;

    @Mock
    UsersCacheImpl mUsersCacheMock;

    // endregion helper fields

    FetchUserUseCaseSyncImpl SUT;

    @Before
    public void setUp() {
        SUT = new FetchUserUseCaseSyncImpl(mFetchUserHttpEndpointSyncMock, mUsersCacheMock);
    }

    @Test
    public void fetchUserSync_notInCacheEndpointSuccess_successStatus() throws Exception {
        // Arrange
        success();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID, USERNAME);
        // Assert
        assertEquals(FetchUserUseCaseSync.Status.SUCCESS, result.getStatus());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointSuccess_correctUserReturned() throws Exception {
        // Arrange
        success();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID, USERNAME);
        // Assert
        assertEquals(USER, result.getUser());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointSuccess_userCached() throws Exception {
        // Arrange
        success();
        userInCache();
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        // Act
        SUT.fetchUserSync(USER_ID, USERNAME);
        // Assert
        verify(mUsersCacheMock).cacheUser(ac.capture());
        assertEquals(USER, ac.getValue());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointAuthError_failureStatus() throws Exception {
        // Arrange
        authError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID, USERNAME);
        // Assert
        assertEquals(FetchUserUseCaseSync.Status.FAILURE, result.getStatus());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointAuthError_nullUserReturned() throws Exception {
        // Arrange
        authError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID, USERNAME);
        // Assert
        assertNull(result.getUser());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointAuthError_nothingCached() throws Exception {
        // Arrange
        authError();
        // Act
        SUT.fetchUserSync(USER_ID, USERNAME);
        // Assert
        verify(mUsersCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUserSync_notInCacheEndpointNetworkError_failureStatus() throws Exception {
        // Arrange
        networkError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID, USERNAME);
        // Assert
        assertEquals(FetchUserUseCaseSync.Status.NETWORK_ERROR, result.getStatus());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointNetworkError_nullUserReturned() throws Exception {
        // Arrange
        networkError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID, USERNAME);
        // Assert
        assertNull(result.getUser());
    }

    @Test
    public void fetchUserSync_notInCacheEndpointNetworkError_nothingCached() throws Exception {
        // Arrange
        networkError();
        // Act
        SUT.fetchUserSync(USER_ID, USERNAME);
        // Assert
        verify(mUsersCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUserSync_inCache_successStatus() throws Exception {
        // Arrange
        success();
        userInCache();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID, USERNAME);
        // Assert
        assertEquals(FetchUserUseCaseSync.Status.SUCCESS, result.getStatus());
    }

    @Test
    public void fetchUserSync_inCache_cachedUserReturned() throws Exception {
        // Arrange
        success();
        userInCache();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID, USERNAME);
        // Assert
        assertEquals(USER, result.getUser());
    }

    // region helper methods

    private void success() throws NetworkErrorException {
        when(mFetchUserHttpEndpointSyncMock.fetchUserSync(anyString(), anyString()))
                .thenReturn(new EndpointResult(EndpointStatus.SUCCESS, USER_ID, USERNAME));
    }

    private void generalError() throws NetworkErrorException {
        when(mFetchUserHttpEndpointSyncMock.fetchUserSync(any(), any()))
                .thenReturn(new EndpointResult(EndpointStatus.GENERAL_ERROR, null, null));
    }

    private void authError() throws NetworkErrorException {
        when(mFetchUserHttpEndpointSyncMock.fetchUserSync(any(), any()))
                .thenReturn(new EndpointResult(EndpointStatus.AUTH_ERROR, null, null));
    }

    private void userNotInCache() {
        when(mUsersCacheMock.getUser(anyString())).thenReturn(null);
    }

    private void userInCache() {
        when(mUsersCacheMock.getUser(anyString())).thenReturn(USER);
    }

    private void networkError() throws NetworkErrorException {
        doThrow(new NetworkErrorException())
                .when(mFetchUserHttpEndpointSyncMock)
                .fetchUserSync(anyString(), anyString());
    }

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}

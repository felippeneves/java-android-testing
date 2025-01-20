package com.felippeneves.test_driven_development.exercise6;

import com.felippeneves.test_driven_development.exercise6.networking.FetchUserHttpEndpointSync;
import com.felippeneves.test_driven_development.exercise6.networking.FetchUserHttpEndpointSync.EndpointResult;
import com.felippeneves.test_driven_development.exercise6.networking.FetchUserHttpEndpointSync.EndpointStatus;
import com.felippeneves.test_driven_development.exercise6.networking.NetworkErrorException;
import com.felippeneves.test_driven_development.exercise6.users.User;
import com.felippeneves.test_driven_development.exercise6.users.UsersCache;

public class FetchUserUseCaseSyncImpl implements FetchUserUseCaseSync {

    private FetchUserHttpEndpointSync mFetchUserHttpEndpointSync;
    private UsersCache mUsersCache;

    public FetchUserUseCaseSyncImpl(FetchUserHttpEndpointSync fetchUserHttpEndpointSync, UsersCache usersCache) {
        mFetchUserHttpEndpointSync = fetchUserHttpEndpointSync;
        mUsersCache = usersCache;
    }

    @Override
    public UseCaseResult fetchUserSync(String userId, String username) {

        EndpointResult endpointResult;

        try {
            endpointResult = mFetchUserHttpEndpointSync.fetchUserSync(userId, username);
        } catch (NetworkErrorException e) {
            return new UseCaseResult(Status.NETWORK_ERROR, null);
        }

        if (endpointResult.getStatus() == EndpointStatus.SUCCESS) {
            User user = new User(userId, username);
            mUsersCache.cacheUser(user);
            return new UseCaseResult(Status.SUCCESS, user);
        } else {
            return new UseCaseResult(Status.FAILURE, null);
        }
    }
}

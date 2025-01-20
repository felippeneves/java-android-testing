package com.felippeneves.test_driven_development.exercise6.networking;

public class FetchUserHttpEndpointSyncImpl implements FetchUserHttpEndpointSync {

    @Override
    public EndpointResult fetchUserSync(String userId, String username) throws NetworkErrorException {
        try {
            return new EndpointResult(EndpointStatus.SUCCESS, userId, username);
        } catch (Exception e) {
            throw new NetworkErrorException();
        }
    }
}

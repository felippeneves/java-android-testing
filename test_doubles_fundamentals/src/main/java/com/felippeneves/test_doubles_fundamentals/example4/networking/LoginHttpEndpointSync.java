package com.felippeneves.test_doubles_fundamentals.example4.networking;

import androidx.annotation.Nullable;

public interface LoginHttpEndpointSync {

    EndpointResult loginSync(String username, String password) throws NetworkErrorException;

    enum EndpointResultStatus {
        SUCCESS,
        AUTH_ERROR,
        SERVER_ERROR,
        GENERAL_ERROR
    }

    class EndpointResult {
        private final EndpointResultStatus mStatus;
        @Nullable
        private final String mAuthToken;

        public EndpointResult(EndpointResultStatus status, @Nullable String authToken) {
            mStatus = status;
            mAuthToken = authToken;
        }

        public EndpointResultStatus getStatus() {
            return mStatus;
        }

        public String getAuthToken() {
            return mAuthToken;
        }
    }
}

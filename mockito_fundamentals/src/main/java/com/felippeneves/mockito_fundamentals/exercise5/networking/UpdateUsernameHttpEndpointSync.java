package com.felippeneves.mockito_fundamentals.exercise5.networking;

import androidx.annotation.Nullable;

public interface UpdateUsernameHttpEndpointSync {

    EndpointResult updateUsername(String userId, String username) throws NetworkErrorException;

    enum EndpointResultStatus {
        SUCCESS,
        AUTH_ERROR,
        SERVER_ERROR,
        GENERAL_ERROR
    }

    class EndpointResult {
        private final EndpointResultStatus mStatus;
        @Nullable
        private final String mUserId;
        @Nullable
        private final String mUsername;

        public EndpointResult(EndpointResultStatus status, @Nullable String userId, @Nullable String username) {
            mStatus = status;
            mUserId = userId;
            mUsername = username;
        }

        public EndpointResultStatus getStatus() {
            return mStatus;
        }

        public String getUserId() {
            return mUserId;
        }

        public String getUsername() {
            return mUsername;
        }
    }
}

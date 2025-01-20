package com.felippeneves.test_driven_development.example10;

import com.felippeneves.test_driven_development.example10.networking.PingServerHttpEndpointSync;

public class PingServerSyncUseCase {

    public enum UseCaseResult {
        FAILURE, SUCCESS
    }

    private final PingServerHttpEndpointSync mPingServerHttpEndpointSync;

    public PingServerSyncUseCase(PingServerHttpEndpointSync mPingServerHttpEndpointSync) {
        this.mPingServerHttpEndpointSync = mPingServerHttpEndpointSync;
    }

    public UseCaseResult pingServerSync() {
        PingServerHttpEndpointSync.EndpointResult result = mPingServerHttpEndpointSync.pingServerSync();
        switch (result) {
            case SUCCESS:
                return UseCaseResult.SUCCESS;
            case GENERAL_ERROR:
            case NETWORK_ERROR:
                return UseCaseResult.FAILURE;
            default:
                throw new RuntimeException("invalid result: " + result);
        }
    }
}

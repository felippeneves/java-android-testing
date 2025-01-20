package com.felippeneves.test_driven_development.exercise7;

import com.felippeneves.test_driven_development.exercise7.networking.GetReputationHttpEndpointSync;

public class FetchReputationUseCaseSync {

    private final GetReputationHttpEndpointSync mGetReputationHttpEndpointSync;

    public FetchReputationUseCaseSync(GetReputationHttpEndpointSync getReputationHttpEndpointSync) {
        mGetReputationHttpEndpointSync = getReputationHttpEndpointSync;
    }

    public UseCaseResult fetchReputation(int reputation) {

        GetReputationHttpEndpointSync.EndpointResult result = mGetReputationHttpEndpointSync.getReputationSync();

        switch (result.getStatus()) {
            case SUCCESS:
                return new UseCaseResult(UseCaseResultEnum.SUCCESS, reputation);
            case NETWORK_ERROR:
            case GENERAL_ERROR:
                return new UseCaseResult(UseCaseResultEnum.FAILURE, 0);
            default:
                throw new RuntimeException("invalid result: " + result);
        }
    }

    public class UseCaseResult {
       private UseCaseResultEnum status;
       private int reputation;

        public UseCaseResult(UseCaseResultEnum status, int reputation) {
            this.status = status;
            this.reputation = reputation;
        }

        public UseCaseResultEnum getStatus() {
            return status;
        }

        public int getReputation() {
            return reputation;
        }
    }

    public enum UseCaseResultEnum {
        FAILURE, SUCCESS
    }
}

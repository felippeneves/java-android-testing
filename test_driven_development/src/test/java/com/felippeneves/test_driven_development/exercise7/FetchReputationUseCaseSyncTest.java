package com.felippeneves.test_driven_development.exercise7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.felippeneves.test_driven_development.exercise7.networking.GetReputationHttpEndpointSync;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FetchReputationUseCaseSyncTest {

    //region constants

    private static final int REPUTATION = 10;
    private static final int DEFAULT_FAILURE_REPUTATION = 0;

    //endregion constants

    // region helper fields

    @Mock
    private GetReputationHttpEndpointSync mGetReputationHttpEndpointSyncMock;

    // endregion helper fields

    // region SUT

    FetchReputationUseCaseSync SUT;

    // endregion SUT

    // region setup

    @Before
    public void setUp() {
        SUT = new FetchReputationUseCaseSync(mGetReputationHttpEndpointSyncMock);
        success();
    }

    // endregion setup

    // region test methods

    @Test
    public void fetchReputation_success_successReturned() {
        // Arrange
        // Act
        FetchReputationUseCaseSync.UseCaseResult result = SUT.fetchReputation(REPUTATION);
        // Assert
        assertEquals(FetchReputationUseCaseSync.UseCaseResultEnum.SUCCESS, result.getStatus());
    }

    @Test
    public void fetchReputation_generalError_failureReturned() {
        // Arrange
        generalError();
        // Act
        FetchReputationUseCaseSync.UseCaseResult result = SUT.fetchReputation(REPUTATION);
        // Assert
        assertEquals(FetchReputationUseCaseSync.UseCaseResultEnum.FAILURE, result.getStatus());
    }

    @Test
    public void fetchReputation_networkError_failureReturned() {
        // Arrange
        networkError();
        // Act
        FetchReputationUseCaseSync.UseCaseResult result = SUT.fetchReputation(REPUTATION);
        // Assert
        assertEquals(FetchReputationUseCaseSync.UseCaseResultEnum.FAILURE, result.getStatus());
    }

    @Test
    public void fetchReputation_success_reputationReturned() {
        // Arrange
        // Act
        FetchReputationUseCaseSync.UseCaseResult result = SUT.fetchReputation(REPUTATION);
        // Assert
        assertEquals(REPUTATION, result.getReputation());
    }

    @Test
    public void fetchReputation_generalError_zeroReturned() {
        // Arrange
        generalError();
        // Act
        FetchReputationUseCaseSync.UseCaseResult result = SUT.fetchReputation(REPUTATION);
        // Assert
        assertEquals(DEFAULT_FAILURE_REPUTATION, result.getReputation());
    }

    @Test
    public void fetchReputation_networkError_zeroReturned() {
        // Arrange
        networkError();
        // Act
        FetchReputationUseCaseSync.UseCaseResult result = SUT.fetchReputation(REPUTATION);
        // Assert
        assertEquals(DEFAULT_FAILURE_REPUTATION, result.getReputation());
    }

    // endregion test methods

    // region helper methods

    private void success() {
        when(mGetReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.SUCCESS, REPUTATION));
    }

    private void generalError() {
        when(mGetReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR, 0));
    }

    private void networkError() {
        when(mGetReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR, 0));
    }

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}

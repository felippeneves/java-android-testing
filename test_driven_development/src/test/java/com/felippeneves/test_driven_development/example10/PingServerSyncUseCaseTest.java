package com.felippeneves.test_driven_development.example10;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.felippeneves.test_driven_development.example10.PingServerSyncUseCase.UseCaseResult;
import com.felippeneves.test_driven_development.example10.networking.PingServerHttpEndpointSync;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PingServerSyncUseCaseTest {

    //region constants

    //endregion constants

    // region helper fields

    @Mock
    PingServerHttpEndpointSync mPingServerHttpEndpointSyncMock;

    // endregion helper fields

    // region SUT

    PingServerSyncUseCase SUT;

    // endregion SUT

    // region setup methods

    @Before
    public void setUp() {
        SUT = new PingServerSyncUseCase(mPingServerHttpEndpointSyncMock);
        success();
    }

    // endregion setup methods

    // region test methods

    @Test
    public void pingServerSync_success_successReturned() {
        // Arrange
        // Act
        UseCaseResult result = SUT.pingServerSync();
        // Assert
        assertEquals(UseCaseResult.SUCCESS, result);
    }

    @Test
    public void pingServerSync_generalError_failureReturned() {
        // Arrange
        generalError();
        // Act
        UseCaseResult result = SUT.pingServerSync();
        // Assert
        assertEquals(UseCaseResult.FAILURE, result);
    }

    @Test
    public void pingServerSync_networkError_failureReturned() {
        // Arrange
        networkError();
        // Act
        UseCaseResult result = SUT.pingServerSync();
        // Assert
        assertEquals(UseCaseResult.FAILURE, result);
    }

    // endregion test methods

    // region helper methods

    private void success() {
        when(mPingServerHttpEndpointSyncMock.pingServerSync()).thenReturn(PingServerHttpEndpointSync.EndpointResult.SUCCESS);
    }

    private void generalError() {
        when(mPingServerHttpEndpointSyncMock.pingServerSync()).thenReturn(PingServerHttpEndpointSync.EndpointResult.GENERAL_ERROR);
    }

    private void networkError() {
        when(mPingServerHttpEndpointSyncMock.pingServerSync()).thenReturn(PingServerHttpEndpointSync.EndpointResult.NETWORK_ERROR);
    }

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}

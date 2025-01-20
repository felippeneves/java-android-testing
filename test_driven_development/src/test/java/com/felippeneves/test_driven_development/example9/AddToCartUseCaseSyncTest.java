package com.felippeneves.test_driven_development.example9;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.felippeneves.test_driven_development.example9.AddToCartUseCaseSync.UseCaseResult;
import com.felippeneves.test_driven_development.example9.networking.AddToCartHttpEndpointSync;
import com.felippeneves.test_driven_development.example9.networking.AddToCartHttpEndpointSync.EndpointResult;
import com.felippeneves.test_driven_development.example9.networking.CartItemScheme;
import com.felippeneves.test_driven_development.example9.networking.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AddToCartUseCaseSyncTest {

    //region constants

    public static final String OFFER_ID = "offerId";
    public static final int AMOUNT = 4;

    //endregion constants

    // region helper fields

    @Mock
    AddToCartHttpEndpointSync mAddToCartHttpEndpointSyncMock;

    // endregion helper fields

    AddToCartUseCaseSync SUT;

    @Before
    public void setUp() {
        SUT = new AddToCartUseCaseSync(mAddToCartHttpEndpointSyncMock);

    }

    // correct parameters passed to the endpoint

    @Test
    public void addToCartSync_correctParametersPassedToEndpoint() throws Exception {
        // Arrange
        success();
        ArgumentCaptor<CartItemScheme> ac = ArgumentCaptor.forClass(CartItemScheme.class);
        // Act
        SUT.addToCartSync(OFFER_ID, AMOUNT);
        // Assert
        verify(mAddToCartHttpEndpointSyncMock).addToCartSync(ac.capture());
        assertEquals(OFFER_ID, ac.getValue().getOfferId());
        assertEquals(AMOUNT, ac.getValue().getAmount());
    }

    // endpoint success - success returned

    @Test
    public void addToCartSync_success_successReturned() throws Exception {
        // Arrange
        success();
        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        // Assert
        assertEquals(UseCaseResult.SUCCESS, result);
    }

    // endpoint auth error - failure returned

    @Test
    public void addToCartSync_authError_failureReturned() throws Exception {
        // Arrange
        authError();
        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        // Assert
        assertEquals(UseCaseResult.FAILURE, result);
    }

    // endpoint general error - failure returned

    @Test
    public void addToCartSync_generalError_failureReturned() throws Exception {
        // Arrange
        generalError();
        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        // Assert
        assertEquals(UseCaseResult.FAILURE, result);
    }

    // endpoint network error - network error returned

    @Test
    public void addToCartSync_networkError_networkErrorReturned() throws Exception {
        // Arrange
        networkError();
        // Act
        UseCaseResult result = SUT.addToCartSync(OFFER_ID, AMOUNT);
        // Assert
        assertEquals(UseCaseResult.NETWORK_ERROR, result);
    }

    // region helper methods

    private void success() throws NetworkErrorException {
        when(mAddToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(EndpointResult.SUCCESS);
    }

    private void authError() throws NetworkErrorException {
        when(mAddToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(EndpointResult.AUTH_ERROR);
    }

    private void generalError() throws NetworkErrorException {
        when(mAddToCartHttpEndpointSyncMock.addToCartSync(any(CartItemScheme.class)))
                .thenReturn(EndpointResult.GENERAL_ERROR);
    }

    private void networkError() throws NetworkErrorException {
        doThrow(new NetworkErrorException())
                .when(mAddToCartHttpEndpointSyncMock)
                .addToCartSync(any(CartItemScheme.class));
    }

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}

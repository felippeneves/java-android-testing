package com.felippeneves.test_driven_development.example11;

import static com.felippeneves.test_driven_development.example11.networking.GetCartItemsHttpEndpoint.Callback;
import static com.felippeneves.test_driven_development.example11.networking.GetCartItemsHttpEndpoint.FailReason;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.felippeneves.test_driven_development.example11.cart.CartItem;
import com.felippeneves.test_driven_development.example11.networking.CartItemSchema;
import com.felippeneves.test_driven_development.example11.networking.GetCartItemsHttpEndpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class FetchCartItemsUseCaseTest {

    //region constants

    public static final int LIMIT = 10;

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final int PRICE = 5;

    //endregion constants

    // region helper fields

    @Mock
    GetCartItemsHttpEndpoint mGetCartItemsHttpEndpointMock;
    @Mock
    FetchCartItemsUseCase.Listener mListenerMock1;
    @Mock
    FetchCartItemsUseCase.Listener mListenerMock2;

    @Captor
    ArgumentCaptor<List<CartItem>> mAcCartItemList;

    // endregion helper fields

    // region SUT

    FetchCartItemsUseCase SUT;

    // endregion SUT

    // region setup

    @Before
    public void setUp() {
        SUT = new FetchCartItemsUseCase(mGetCartItemsHttpEndpointMock);
        success();
    }

    // endregion setup

    // region test methods

    // correct limit passed to the endpoint

    @Test
    public void fetchCartItems_correctLimitPassedToEndpoint() {
        // Arrange
        ArgumentCaptor<Integer> acInt = ArgumentCaptor.forClass(Integer.class);
        // Act
        SUT.fetchCartItemsAndNotify(LIMIT);
        // Assert
        verify(mGetCartItemsHttpEndpointMock).getCartItems(acInt.capture(), any(Callback.class));
        assertEquals(LIMIT, acInt.getValue().intValue());
    }


    // success - all observers notified with the correct data

    @Test
    public void fetchCartItems_success_observersNotifiedWithCorrectData() {
        // Arrange
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);
        // Assert
        verify(mListenerMock1).onCartItemsFetched(mAcCartItemList.capture());
        verify(mListenerMock2).onCartItemsFetched(mAcCartItemList.capture());
        List<List<CartItem>> captures = mAcCartItemList.getAllValues();
        List<CartItem> capture1 = captures.get(0);
        List<CartItem> capture2 = captures.get(1);
        assertEquals(getCartItems(), capture1);
        assertEquals(getCartItems(), capture2);
    }

    // success - unsubscribed observers not notified

    @Test
    public void fetchCartItems_success_unsubscribedObserversNotNotified() {
        // Arrange
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.unregisterListener(mListenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);
        // Assert
        verify(mListenerMock1).onCartItemsFetched(any());
        verifyNoMoreInteractions(mListenerMock2);
    }

    // general error - observers notified of failure

    @Test
    public void fetchCartItems_generalError_observersNotifiedOfFailure() {
        // Arrange
        generalError();
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);
        // Assert
        verify(mListenerMock1).onCartItemsFailed();
        verify(mListenerMock2).onCartItemsFailed();
    }

    // network error - observers notified of failure

    @Test
    public void fetchCartItems_networkError_observersNotifiedOfFailure() {
        // Arrange
        networkError();
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchCartItemsAndNotify(LIMIT);
        // Assert
        verify(mListenerMock1).onCartItemsFailed();
        verify(mListenerMock2).onCartItemsFailed();
    }

    // endregion test methods

    // region helper methods

    private void success() {
        doAnswer(invocation -> {
            // the 0 index is the first argument passed to the method, so in this case it's an integer
            Callback callback = invocation.getArgument(1);
            callback.onGetCartItemsSucceeded(getCartItemsSchemes());
            return null;
        }).when(mGetCartItemsHttpEndpointMock).getCartItems(anyInt(), any(Callback.class));
    }

    private List<CartItemSchema> getCartItemsSchemes() {
        List<CartItemSchema> schemaList = new ArrayList<>();
        schemaList.add(new CartItemSchema(ID, TITLE, DESCRIPTION, PRICE));
        return schemaList;
    }

    private List<CartItem> getCartItems() {
        List<CartItem> cartItemsList = new ArrayList<>();
        cartItemsList.add(new CartItem(ID, TITLE, DESCRIPTION, PRICE));
        return cartItemsList;
    }

    private void generalError() {
        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onGetCartItemsFailed(FailReason.GENERAL_ERROR);
            return null;
        }).when(mGetCartItemsHttpEndpointMock).getCartItems(anyInt(), any(Callback.class));
    }

    private void networkError() {
        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onGetCartItemsFailed(FailReason.NETWORK_ERROR);
            return null;
        }).when(mGetCartItemsHttpEndpointMock).getCartItems(anyInt(), any(Callback.class));
    }

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}

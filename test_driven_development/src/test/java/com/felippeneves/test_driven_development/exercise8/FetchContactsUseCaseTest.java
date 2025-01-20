package com.felippeneves.test_driven_development.exercise8;

import static com.felippeneves.test_driven_development.exercise8.networking.GetContactsHttpEndpoint.Callback;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.felippeneves.test_driven_development.exercise8.contacts.Contact;
import com.felippeneves.test_driven_development.exercise8.networking.ContactSchema;
import com.felippeneves.test_driven_development.exercise8.networking.GetContactsHttpEndpoint;
import com.felippeneves.test_driven_development.exercise8.networking.GetContactsHttpEndpoint.FailReason;

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
public class FetchContactsUseCaseTest {

    //region constants

    public static final String FILTER_TERM = "filter term";

    public static final String ID = "id";
    public static final String FULL_NAME = "full name";
    public static final String FULL_PHONE_NUMBER = "phone number";
    public static final String IMAGE_URL = "image url";
    public static final double AGE = 20.0;

    //endregion constants

    // region helper fields

    @Mock
    GetContactsHttpEndpoint mGetContactsHttpEndpointMock;
    @Mock
    FetchContactsUseCase.Listener mListenerMock1;
    @Mock
    FetchContactsUseCase.Listener mListenerMock2;

    @Captor
    ArgumentCaptor<List<Contact>> mAcContactList;

    // endregion helper fields

    // region SUT

    FetchContactsUseCase SUT;

    // endregion SUT

    // region setup

    @Before
    public void setUp() {
        SUT = new FetchContactsUseCase(mGetContactsHttpEndpointMock);
        success();
    }

    // endregion setup

    // region test methods

    // correct limit passed to the endpoint

    @Test
    public void fetchContacts_correctFilterTermPassedToEndpoint() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.fetchContactsAndNotify(FILTER_TERM);
        // Assert
        verify(mGetContactsHttpEndpointMock).getContacts(ac.capture(), any(Callback.class));
        assertEquals(FILTER_TERM, ac.getValue());
    }

    // success - all observers notified with the correct data

    @Test
    public void fetchContacts_success_observersNotifiedWithCorrectData() {
        // Arrange
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchContactsAndNotify(FILTER_TERM);
        // Assert
        verify(mListenerMock1).onContactsFetched(mAcContactList.capture());
        verify(mListenerMock2).onContactsFetched(mAcContactList.capture());
        List<List<Contact>> captures = mAcContactList.getAllValues();
        List<Contact> capture1 = captures.get(0);
        List<Contact> capture2 = captures.get(1);
        assertEquals(getContactList(), capture1);
        assertEquals(getContactList(), capture2);
    }

    // success - unsubscribed observers not notified

    @Test
    public void fetchContacts_success_unsubscribedObserversNotNotified() {
        // Arrange
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.unregisterListener(mListenerMock2);
        SUT.fetchContactsAndNotify(FILTER_TERM);
        // Assert
        verify(mListenerMock1).onContactsFetched(any());
        verifyNoMoreInteractions(mListenerMock2);
    }

    // general error - observers notified of failure

    @Test
    public void fetchContacts_generalError_observersNotifiedOfFailure() {
        // Arrange
        generalError();
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchContactsAndNotify(FILTER_TERM);
        // Assert
        verify(mListenerMock1).onContactsFailed();
        verify(mListenerMock2).onContactsFailed();
    }

    // network error - observers notified of failure

    @Test
    public void fetchCartItems_networkError_observersNotifiedOfFailure() {
        // Arrange
        networkError();
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchContactsAndNotify(FILTER_TERM);
        // Assert
        verify(mListenerMock1).onContactsFailed();
        verify(mListenerMock2).onContactsFailed();
    }

    // endregion test methods

    // region helper methods

    private void success() {
        doAnswer(invocation -> {
            // the 0 index is the first argument passed to the method, so in this case it's an integer
            GetContactsHttpEndpoint.Callback callback = invocation.getArgument(1);
            callback.onGetContactsSucceeded(getContactsSchemaList());
            return null;
        }).when(mGetContactsHttpEndpointMock).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
    }

    private List<ContactSchema> getContactsSchemaList() {
        List<ContactSchema> contactSchemaList = new ArrayList<>();
        contactSchemaList.add(new ContactSchema(ID, FULL_NAME, FULL_PHONE_NUMBER, IMAGE_URL, AGE));
        return contactSchemaList;
    }

    private List<Contact> getContactList() {
        List<Contact> contactList = new ArrayList<>();
        contactList.add(new Contact(ID, FULL_NAME, IMAGE_URL));
        return contactList;
    }

    private void generalError() {
        doAnswer(invocation -> {
            GetContactsHttpEndpoint.Callback callback = invocation.getArgument(1);
            callback.onGetContactsFailed(FailReason.GENERAL_ERROR);
            return null;
        }).when(mGetContactsHttpEndpointMock).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
    }

    private void networkError() {
        doAnswer(invocation -> {
            GetContactsHttpEndpoint.Callback callback = invocation.getArgument(1);
            callback.onGetContactsFailed(FailReason.NETWORK_ERROR);
            return null;
        }).when(mGetContactsHttpEndpointMock).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
    }

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}

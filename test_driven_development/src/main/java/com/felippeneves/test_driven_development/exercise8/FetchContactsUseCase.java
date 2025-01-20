package com.felippeneves.test_driven_development.exercise8;

import com.felippeneves.test_driven_development.exercise8.contacts.Contact;
import com.felippeneves.test_driven_development.exercise8.networking.ContactSchema;
import com.felippeneves.test_driven_development.exercise8.networking.GetContactsHttpEndpoint;
import com.felippeneves.test_driven_development.exercise8.networking.GetContactsHttpEndpoint.Callback;
import com.felippeneves.test_driven_development.exercise8.networking.GetContactsHttpEndpoint.FailReason;

import java.util.ArrayList;
import java.util.List;

public class FetchContactsUseCase {

    public interface Listener {
        void onContactsFetched(List<Contact> capture);

        void onContactsFailed();
    }

    private final List<Listener> mListeners = new ArrayList<>();
    private final GetContactsHttpEndpoint mGetContactsHttpEndpoint;

    public FetchContactsUseCase(GetContactsHttpEndpoint mGetContactsHttpEndpoint) {
        this.mGetContactsHttpEndpoint = mGetContactsHttpEndpoint;
    }

    public void fetchContactsAndNotify(String filterTerm) {
        mGetContactsHttpEndpoint.getContacts(filterTerm, new Callback() {

            @Override
            public void onGetContactsSucceeded(List<ContactSchema> contactSchemaList) {
                for (Listener listener : mListeners) {
                    listener.onContactsFetched(contactsFromSchemas(contactSchemaList));
                }
            }

            @Override
            public void onGetContactsFailed(FailReason failReason) {
                switch (failReason) {
                    case GENERAL_ERROR:
                    case NETWORK_ERROR:
                        for (Listener listener : mListeners) {
                            listener.onContactsFailed();
                        }
                        break;
                }
            }
        });
    }

    private List<Contact> contactsFromSchemas(List<ContactSchema> contactSchemaList) {
        List<Contact> contactList = new ArrayList<>();

        for (ContactSchema schema : contactSchemaList) {
            contactList.add(new Contact(
                    schema.getId(),
                    schema.getFullName(),
                    schema.getImageUrl()
            ));
        }

        return contactList;
    }

    public void registerListener(Listener listener) {
        mListeners.add(listener);
    }

    public void unregisterListener(Listener listener) {
        mListeners.remove(listener);
    }
}

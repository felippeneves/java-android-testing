package com.felippeneves.test_driven_development.exercise8.contacts;

import androidx.annotation.Nullable;

public class Contact {

    private final String mId;
    private final String mFullName;
    private final String mImageUrl;

    public Contact(String id, String fullName, String imageUrl) {
        mId = id;
        mFullName = fullName;
        mImageUrl = imageUrl;
    }

    public String getId() {
        return mId;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Contact contact = (Contact) obj;

        if (mId != contact.mId) return false;
        if (!mFullName.equals(contact.mFullName)) return false;
        return mImageUrl.equals(contact.mImageUrl);
    }

    @Override
    public int hashCode() {
        int result = mId.hashCode();
        result = 31 * result + mId.hashCode();
        result = 31 * result + mFullName.hashCode();
        result = 31 * result + mImageUrl.hashCode();
        return result;
    }
}

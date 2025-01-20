package com.felippeneves.unit_testing_in_android.example12;

import android.content.Context;

public class StringRetriever {
    private final Context mContext;

    public StringRetriever(Context context) {
        mContext = context;
    }

    public String getString(int id) {
        return mContext.getString(id);
    }
}

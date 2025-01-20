package com.felippeneves.test_driven_development.example9.networking;

public class CartItemScheme {

    private final String mOfferId;
    private final int mAmount;

    public CartItemScheme(String offerId, int amount) {
        mOfferId = offerId;
        mAmount = amount;
    }

    public String getOfferId() {
        return mOfferId;
    }

    public int getAmount() {
        return mAmount;
    }
}

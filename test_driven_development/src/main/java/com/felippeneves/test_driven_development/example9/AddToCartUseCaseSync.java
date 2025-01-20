package com.felippeneves.test_driven_development.example9;

import com.felippeneves.test_driven_development.example9.networking.AddToCartHttpEndpointSync;
import com.felippeneves.test_driven_development.example9.networking.CartItemScheme;
import com.felippeneves.test_driven_development.example9.networking.NetworkErrorException;

public class AddToCartUseCaseSync {

    private final AddToCartHttpEndpointSync mAddToCartHttpEndpointSync;

    public enum UseCaseResult {
        SUCCESS,
        NETWORK_ERROR, FAILURE
    }

    public AddToCartUseCaseSync(AddToCartHttpEndpointSync addToCartHttpEndpointSync) {
        mAddToCartHttpEndpointSync = addToCartHttpEndpointSync;
    }

    public UseCaseResult addToCartSync(String offedId, int amount) throws NetworkErrorException {
        AddToCartHttpEndpointSync.EndpointResult result;

        try {
            result = mAddToCartHttpEndpointSync.addToCartSync(new CartItemScheme(offedId, amount));
        } catch (NetworkErrorException e) {
            return UseCaseResult.NETWORK_ERROR;
        }

        switch (result) {
            case SUCCESS:
                return UseCaseResult.SUCCESS;
            case AUTH_ERROR:
            case GENERAL_ERROR:
                return UseCaseResult.FAILURE;
            default:
                throw new RuntimeException("invalid status: " + result);
        }
    }
}

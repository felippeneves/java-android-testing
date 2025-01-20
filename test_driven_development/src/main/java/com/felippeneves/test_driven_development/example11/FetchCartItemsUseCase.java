package com.felippeneves.test_driven_development.example11;

import static com.felippeneves.test_driven_development.example11.networking.GetCartItemsHttpEndpoint.Callback;
import static com.felippeneves.test_driven_development.example11.networking.GetCartItemsHttpEndpoint.FailReason;

import com.felippeneves.test_driven_development.example11.cart.CartItem;
import com.felippeneves.test_driven_development.example11.networking.CartItemSchema;
import com.felippeneves.test_driven_development.example11.networking.GetCartItemsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

public class FetchCartItemsUseCase {

    public interface Listener {
        void onCartItemsFetched(List<CartItem> capture);

        void onCartItemsFailed();
    }

    private final List<Listener> mListeners = new ArrayList<>();
    private final GetCartItemsHttpEndpoint mGetCartItemsHttpEndpoint;

    public FetchCartItemsUseCase(GetCartItemsHttpEndpoint mGetCartItemsHttpEndpoint) {
        this.mGetCartItemsHttpEndpoint = mGetCartItemsHttpEndpoint;
    }

    public void fetchCartItemsAndNotify(int limit) {
        mGetCartItemsHttpEndpoint.getCartItems(limit, new Callback() {

            @Override
            public void onGetCartItemsSucceeded(List<CartItemSchema> cartItemSchemaList) {
                for (Listener listener : mListeners) {
                    listener.onCartItemsFetched(cartItemsFromSchemas(cartItemSchemaList));
                }
            }

            @Override
            public void onGetCartItemsFailed(FailReason failReason) {
                switch (failReason) {
                    case GENERAL_ERROR:
                    case NETWORK_ERROR:
                        for (Listener listener : mListeners) {
                            listener.onCartItemsFailed();
                        }
                        break;
                }
            }
        });
    }

    private List<CartItem> cartItemsFromSchemas(List<CartItemSchema> cartItemSchemaList) {
        List<CartItem> cartItemList = new ArrayList<>();

        for (CartItemSchema schema : cartItemSchemaList) {
            cartItemList.add(new CartItem(
                    schema.getId(),
                    schema.getTitle(),
                    schema.getDescription(),
                    schema.getPrice())
            );
        }

        return cartItemList;
    }

    public void registerListener(Listener listener) {
        mListeners.add(listener);
    }

    public void unregisterListener(Listener listener) {
        mListeners.remove(listener);
    }
}

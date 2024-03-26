package com.example.edrobe;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private List<CartItem> cartItems;

    private static CartManager instance;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public void addItemToCart(CartItem cartItem) {
        for (CartItem item : cartItems) {
            if (item.getItemName().equals(cartItem.getItemName())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(cartItem);
    }


    public List<CartItem> getCartItems() {
        return cartItems;
    }

}

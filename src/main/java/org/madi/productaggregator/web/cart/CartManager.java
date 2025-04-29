package org.madi.productaggregator.web.cart;

import java.util.Set;

public interface CartManager {
    Set<CartItem> getCart();
    void addProduct(Long aggregateProductId, Integer quantity);
    void removeProduct(Long aggregateProductId);
    void clearCart();
}

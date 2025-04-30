package org.madi.productaggregator.web.cart;

import java.util.Set;

public interface CartManager {
    Set<CartItem> getCart();
    void addProd(Long aggregateProductId, Integer quantity);
    void updateProdQuantity(Long aggregateProductId, Integer quantity);
    void removeProd(Long aggregateProductId);
}

package org.madi.productaggregator.web.cart;

import org.madi.productaggregator.web.dao.AggregatorProductRepository;
import org.madi.productaggregator.web.entities.AggregatorProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Service
public class CartService implements CartManager {
    @Autowired
    private Cart cart;

    @Autowired
    private AggregatorProductRepository aggregatorProductRepository;


    @Override
    public Set<CartItem> getCart() {
        return cart.getCart();
    }

    @Override
    public void addProduct(Long aggregateProductId, Integer quantity) {
        Optional<AggregatorProductEntity> aggregatorProduct = aggregatorProductRepository.findById(aggregateProductId);
        if (aggregatorProduct.isPresent()) {
            CartItem existingItem = cart.getCart().stream()
                    .filter(item -> item.getAggregatorEntity().getId().equals(aggregateProductId))
                    .findFirst()
                    .orElse(null);
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
            } else {
                cart.getCart().add(new CartItem(aggregatorProduct.get(), quantity));
            }
        }
    }

    @Override
    public void removeProduct(Long aggregateProductId) {
        cart.getCart().removeIf(cartItem -> cartItem.getAggregatorEntity().getId().equals(aggregateProductId));
    }

    @Override
    public void clearCart() {
        cart.getCart().clear();
    }
}

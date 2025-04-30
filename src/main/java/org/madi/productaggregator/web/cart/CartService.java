package org.madi.productaggregator.web.cart;

import lombok.RequiredArgsConstructor;
import org.madi.productaggregator.web.dao.AggregatorProductRepository;
import org.madi.productaggregator.web.entities.AggregatorProductEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartService implements CartManager {
    private final Cart cart;
    private final AggregatorProductRepository aggregatorProductRepository;

    @Override
    public Set<CartItem> getCart() {
        return cart.getCart();
    }

    @Override
    public void addProd(Long aggregateProductId, Integer quantity) {
        Optional<AggregatorProductEntity> aggregatorProduct = aggregatorProductRepository.findById(aggregateProductId);
        cart.getCart().add(new CartItem(aggregatorProduct.get(), quantity));
    }

    @Override
    public void updateProdQuantity(Long aggregateProductId, Integer quantity) {
        Optional<AggregatorProductEntity> aggregatorProduct = aggregatorProductRepository.findById(aggregateProductId);
        if (aggregatorProduct.isPresent()) {
            CartItem productFromCart = cart.getCart().stream()
                    .filter(item -> item.getAggregatorEntity().getId().equals(aggregateProductId))
                    .findFirst()
                    .orElse(null);
            if (productFromCart != null) {
                productFromCart.setQuantity(quantity);
            } else {
                cart.getCart().add(new CartItem(aggregatorProduct.get(), quantity));
            }
        }
    }

    @Override
    public void removeProd(Long aggregateProductId) {
        cart.getCart().removeIf(cartItem -> cartItem.getAggregatorEntity().getId().equals(aggregateProductId));
    }
}
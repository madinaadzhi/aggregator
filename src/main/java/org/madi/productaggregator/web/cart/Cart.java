package org.madi.productaggregator.web.cart;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Component
@SessionScope
public class Cart {
    private Set<CartItem> cart = new HashSet<>();
}

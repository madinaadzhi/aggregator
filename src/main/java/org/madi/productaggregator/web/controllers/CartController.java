package org.madi.productaggregator.web.controllers;

import org.madi.productaggregator.web.cart.Cart;
import org.madi.productaggregator.web.cart.CartService;
import org.madi.productaggregator.web.dao.MarketRepository;
import org.madi.productaggregator.web.entities.MarketEntity;
import org.madi.productaggregator.web.market.api.Market;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private CartHelper cartHelper;

    @GetMapping("/cart")
    public String showCart(Model model) {
        model.addAttribute("markets", cartHelper.getMarkets());
        model.addAttribute("cart", cartService.getCart());
        return "cart";
    }

    @PostMapping("/cart/addProduct")
    public String addProduct(@RequestParam("productId") Long productId,
                             @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
                             @RequestParam("redirectUrl") String redirectUrl) {
        cartService.addProduct(productId, quantity);
        redirectUrl.substring(0, redirectUrl.length() - 13);
        return "redirect:" + redirectUrl;
    }

    @PostMapping("/cart/removeProduct")
    public String removeProduct(@RequestParam("productId") Long productId) {
        cartService.removeProduct(productId);
        return "redirect:/cart?removed=true";
    }

    @PostMapping("/cart/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart?cleared=true";
    }
}

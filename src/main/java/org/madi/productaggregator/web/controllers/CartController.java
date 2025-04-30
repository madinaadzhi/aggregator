package org.madi.productaggregator.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.madi.productaggregator.web.cart.CartService;
import org.madi.productaggregator.web.cart.CartHelperService;
import org.madi.productaggregator.web.geo.MarketGeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private CartHelperService cartHelperService;

    @GetMapping("/cart")
    public String showCart(HttpServletRequest request, Model model) {
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("markets", cartHelperService.getMarkets());
        model.addAttribute("cart", cartService.getCart());
        return "cart";
    }

    @PostMapping("/cart/addProduct")
    public String addProduct(@RequestParam("productId") Long productId,
                             @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
                             @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        cartService.addProd(productId, quantity);
        if (redirectUrl != null && !redirectUrl.isBlank()) {
            return "redirect:" + redirectUrl;
        }
        return null;
    }

    @PostMapping("/cart/updateProduct")
    public String updateQuantity(@RequestParam Long productId,
                                 @RequestParam Integer quantity) {
        cartService.updateProdQuantity(productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/cart/removeProduct")
    public String removeProduct(@RequestParam("productId") Long productId) {
        cartService.removeProd(productId);
        return "redirect:/cart?removed=true";
    }
}
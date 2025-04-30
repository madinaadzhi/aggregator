package org.madi.productaggregator.web.controllers;

import org.madi.productaggregator.web.cart.CartHelperService;
import org.madi.productaggregator.web.cart.CartService;
import org.madi.productaggregator.web.geo.MarketDist;
import org.madi.productaggregator.web.geo.MarketGeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class GeoController {
    @Autowired
    private CartService cartService;

    @Autowired
    private MarketGeoService marketGeoService;

    @Autowired
    private CartHelperService cartHelperService;

    @PostMapping("/markets/distances")
    public String handleCoordinates(@RequestParam("lat") double lat,
                                    @RequestParam("lng") double lng,
                                    Model model) throws IOException {
        List<MarketDist> nearbyMarkets = marketGeoService.findMarkets(lat, lng);
        model.addAttribute("lat", lat);
        model.addAttribute("lng", lng);
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("markets", cartHelperService.getMarkets());
        model.addAttribute("nearbyMarkets", nearbyMarkets);
        return "cart";
    }
}

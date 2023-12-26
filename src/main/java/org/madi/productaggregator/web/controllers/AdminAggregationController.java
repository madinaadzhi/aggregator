package org.madi.productaggregator.web.controllers;

import org.madi.productaggregator.web.dao.ProductRepository;
import org.madi.productaggregator.web.entities.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class AdminAggregationController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("admin/aggregation/link")
    public String list(@RequestParam("productIds") String productIdsString, Model model) {
        List<Long> productIds = Arrays.stream(productIdsString.split(",")).map(Long::parseLong).toList();
        List<ProductEntity> products = productRepository.findProductEntitiesByIdIn(productIds);
        model.addAttribute("products", products);
        return "admin-product-aggregation";
    }
}

package org.madi.productaggregator.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminAggregateProductController {

    @Autowired
    private AggregatorProductHelper aggregatorProductHelper;

    @GetMapping("/admin/aggregate-products")
    public String list( @RequestParam(required = false, defaultValue = "10") int pageSize ,
                        @RequestParam(required = false, defaultValue = "1") int activePage,
                       @RequestParam(required = false) String productName, Model model) {
        aggregatorProductHelper.putAggregatorProductInfoToModel(pageSize, activePage, productName, model, null);
        return "admin-aggregator-products";
    }
}

package org.madi.productaggregator.web.controllers;

import org.apache.commons.lang.StringUtils;
import org.madi.productaggregator.web.dao.AggregatorProductRepository;
import org.madi.productaggregator.web.dao.ProductRepository;
import org.madi.productaggregator.web.entities.AggregatorProductEntity;
import org.madi.productaggregator.web.entities.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang.StringUtils.join;

@Controller
public class AdminAggregationController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AggregatorProductRepository aggregatorProductRepository;

    @GetMapping("admin/aggregation/link")
    public String list(@RequestParam("productIds") String productIdsString,
                       @RequestParam(required = false) String aggregatorProductName,
                       Model model) {
        List<Long> productIds = Arrays.stream(productIdsString.split(",")).map(Long::parseLong).toList();
        List<ProductEntity> products = productRepository.findProductEntitiesByIdIn(productIds);
        model.addAttribute("products", products);
        model.addAttribute("productIds", join(productIds, ","));

        if (StringUtils.isNotBlank(aggregatorProductName)) {
            model.addAttribute("aggregatorProductName", aggregatorProductName);
            List<AggregatorProductEntity> aggregatorProductEntities =
                    aggregatorProductRepository.findFirst10ByNameLike('%' + aggregatorProductName + '%');
            model.addAttribute("aggregatorProductEntities", aggregatorProductEntities);
        }
        return "admin-product-aggregation";
    }
}

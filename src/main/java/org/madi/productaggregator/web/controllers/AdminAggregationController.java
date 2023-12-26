package org.madi.productaggregator.web.controllers;

import org.apache.commons.lang.StringUtils;
import org.madi.productaggregator.web.dao.AggregatorProductRepository;
import org.madi.productaggregator.web.dao.CategoryRepository;
import org.madi.productaggregator.web.dao.ProductRepository;
import org.madi.productaggregator.web.entities.AggregatorProductEntity;
import org.madi.productaggregator.web.entities.CategoryEntity;
import org.madi.productaggregator.web.entities.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang.StringUtils.join;
import static org.madi.productaggregator.web.controllers.CategoryUtils.buildCategories;

@Controller
public class AdminAggregationController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AggregatorProductRepository aggregatorProductRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Value("${aggregator.market.id}")
    private Long aggrMarketId;
    @GetMapping("admin/aggregation/link")
    public String list(@RequestParam("productIds") String productIdsString,
                       @RequestParam(required = false) String aggregatorProductName,
                       Model model) {
        List<Long> productIds = Arrays.stream(productIdsString.split(",")).map(Long::parseLong).toList();
        List<ProductEntity> products = productRepository.findProductEntitiesByIdIn(productIds);
        List<CategoryEntity> parentCategories = categoryRepository.getCategoryEntitiesByMarketIdAndParentIdIsNull(aggrMarketId);
        List<CategoryEntity> categories = buildCategories(parentCategories);
        model.addAttribute("categories", categories);
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

    @PostMapping("admin/aggregation/link")
    public String link(@RequestParam("productIds") String productIdsString,
                       @RequestParam Long aggProdId) {
        List<Long> productIds = Arrays.stream(productIdsString.split(",")).map(Long::parseLong).toList();
        List<ProductEntity> products = productRepository.findProductEntitiesByIdIn(productIds);

        AggregatorProductEntity aggregatorProductEntity = aggregatorProductRepository.findById(aggProdId).get();
        for (ProductEntity product : products) {
            product.setAggregatorProductEntity(aggregatorProductEntity);
            productRepository.save(product);
        }
        return "redirect:/admin/products?success=true";
    }
}

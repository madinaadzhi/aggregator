package org.madi.productaggregator.web.controllers;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.madi.productaggregator.web.controllers.CategoryUtils.buildCategories;

@Controller
public class AdminAggregateProductController {

    @Autowired
    private AggregatorProductHelper aggregatorProductHelper;
    @Autowired
    private AggregatorProductRepository aggregatorProductRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Value("${aggregator.market.id}")
    private Long aggrMarketId;
    @GetMapping("/admin/aggregate-products")
    public String list( @RequestParam(required = false, defaultValue = "10") int pageSize ,
                        @RequestParam(required = false, defaultValue = "1") int activePage,
                       @RequestParam(required = false) String productName, Model model) {
        aggregatorProductHelper.putAggregatorProductInfoToModel(pageSize, activePage, productName, model, null);
        return "admin-aggregator-products";
    }

    @GetMapping("/admin/aggregate-products/{id}")
    public String editView(@PathVariable Long id, Model model) {
        AggregatorProductEntity aggregatorProduct = aggregatorProductRepository.findById(id).get();
        List<CategoryEntity> parentCategories = categoryRepository.getCategoryEntitiesByMarketIdAndParentIdIsNull(aggrMarketId);
        List<CategoryEntity> categories = buildCategories(parentCategories);
        model.addAttribute("categories", categories);
        model.addAttribute("aggregateProduct", aggregatorProduct);
        return "admin-edit-product";
    }
}

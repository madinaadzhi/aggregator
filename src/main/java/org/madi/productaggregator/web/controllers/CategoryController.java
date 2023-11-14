package org.madi.productaggregator.web.controllers;

import org.madi.productaggregator.web.dao.CategoryRepository;
import org.madi.productaggregator.web.entities.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${aggregator.market.id}")
    private Long aggrMarketId;

    @GetMapping("categories")
    public String list(Model model) {
        Iterable<CategoryEntity> entities = categoryRepository.getCategoryEntitiesByMarketId(aggrMarketId);
        List<CategoryEntity> parentCategories = new ArrayList<>();
        for (CategoryEntity entity : entities) {
            if (entity.getParentId() == null && entity.getMarketId().equals(aggrMarketId)) {
                parentCategories.add(entity);
            }
        }
        model.addAttribute("parentCategories", parentCategories);
        return "aggregate-categories";
    }
}

package org.madi.productaggregator.web.controllers;

import org.madi.productaggregator.web.dao.CategoryRepository;
import org.madi.productaggregator.web.entities.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminCategoryController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Value("${aggregator.market.id}")
    private Long aggrMarketId;
    @GetMapping("/admin/categories")
    public String list(Model model) {
        List<CategoryEntity> parentCategories = categoryRepository.getCategoryEntitiesByMarketIdAndParentIdIsNull(aggrMarketId);
        List<CategoryEntity> categories = buildCategories(parentCategories);
        model.addAttribute("categories", categories);
        return "admin-categories";
    }

    @GetMapping("/admin/categories/{categoryId}")
    public String editView(Model model, @PathVariable Long categoryId,

                           @RequestParam(required = false) Boolean success) {
        CategoryEntity category = categoryRepository.findById(categoryId).get();
        List<CategoryEntity> parentCategories = categoryRepository.getCategoryEntitiesByMarketIdAndParentIdIsNull(aggrMarketId);
        List<CategoryEntity> categories = buildCategories(parentCategories);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        if (success != null && success) {
            model.addAttribute("success", success);

        }
        return "admin-edit-categories";
    }

    @PostMapping("/admin/categories/{categoryId}")
    public String edit( @PathVariable Long categoryId, @RequestParam Long parentId,
                        @RequestParam String name) {

        CategoryEntity category = categoryRepository.findById(categoryId).get();
        category.setParentId(parentId);
        category.setName(name);
        categoryRepository.save(category);

        return "redirect:/admin/categories/" + categoryId + "?success=true";
    }


    private List<CategoryEntity> buildCategories(List<CategoryEntity> parentCategories) {
        List<CategoryEntity> categories = new ArrayList<>();
        for (CategoryEntity category : parentCategories) {
            categories.add(category);
            if (category.getChildCategories() != null) {
                categories.addAll(buildCategories(category.getChildCategories()));
            }
        }
        return categories;
    }
}

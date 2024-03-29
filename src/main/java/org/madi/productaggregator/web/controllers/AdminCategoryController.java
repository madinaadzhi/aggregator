package org.madi.productaggregator.web.controllers;

import org.madi.productaggregator.web.dao.CategoryRepository;
import org.madi.productaggregator.web.entities.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.madi.productaggregator.web.controllers.CategoryUtils.buildCategories;

@Controller
public class AdminCategoryController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Value("${aggregator.market.id}")
    private Long aggrMarketId;
    @GetMapping("/admin/categories")
    public String list(Model model, @RequestParam(required = false) Boolean error) {
        List<CategoryEntity> parentCategories = categoryRepository.getCategoryEntitiesByMarketIdAndParentIdIsNull(aggrMarketId);
        List<CategoryEntity> categories = buildCategories(parentCategories);
        model.addAttribute("categories", categories);
        if (error != null && error) {
            model.addAttribute("error", error);
        }
        return "admin-categories";
    }

    @GetMapping("/admin/categories/{categoryId}")
    public String editView(Model model, @PathVariable String categoryId,
                           @RequestParam(required = false) Boolean success) {
        CategoryEntity category;
        if (categoryId.equals("new")) {
            category = new CategoryEntity();
        } else {
            category = categoryRepository.findById(Long.valueOf(categoryId)).get();
        }
        List<CategoryEntity> parentCategories = categoryRepository.getCategoryEntitiesByMarketIdAndParentIdIsNull(aggrMarketId);
        List<CategoryEntity> categories = buildCategories(parentCategories);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        if (success != null && success) {
            model.addAttribute("success", success);
        }
        return "admin-edit-categories";
    }

    @GetMapping("/admin/categories/{categoryId}/delete")
    public String delete( @PathVariable Long categoryId) {
        try {
            categoryRepository.deleteById(categoryId);
            return "redirect:/admin/categories";
        } catch (Exception e) {
            return "redirect:/admin/categories?error=true";
        }
    }

    @PostMapping("/admin/categories/{categoryId}")
    public String edit( @PathVariable Long categoryId, @RequestParam Long parentId,
                        @RequestParam String name) {
        if (parentId == -1) {
            parentId = null;
        }
        CategoryEntity category;
        if (categoryId == -1) {
            category = new CategoryEntity();
            category.setExternalId("none");
            category.setMarketId(aggrMarketId);
        } else {
            category = categoryRepository.findById(categoryId).get();
        }
        category.setParentId(parentId);
        category.setName(name);
        CategoryEntity saved = categoryRepository.save(category);
        return "redirect:/admin/categories/" + saved.getId() + "?success=true";
    }



}

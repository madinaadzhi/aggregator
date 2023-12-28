package org.madi.productaggregator.web.controllers;

import org.madi.productaggregator.web.dao.AggregatorProductRepository;
import org.madi.productaggregator.web.dao.CategoryRepository;
import org.madi.productaggregator.web.entities.AggregatorProductEntity;
import org.madi.productaggregator.web.entities.CategoryEntity;
import org.madi.productaggregator.web.util.PagingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Controller
public class AggregateProductController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AggregatorProductHelper aggregatorProductHelper;

    @GetMapping("products")
    public String list(@RequestParam Long categoryId, @RequestParam int pageSize, @RequestParam int activePage,
                       @RequestParam(required = false) String productName, Model model) {
        Optional<CategoryEntity> category = categoryRepository.findById(categoryId);
        List<Long> childCategoryIds = getChildCategoryIds(category.get());
        List<CategoryEntity> parentCategories = getParentCategories(category.get());
        parentCategories.add(category.get());
        model.addAttribute("parentCategories", parentCategories);
        model.addAttribute("category", category.get());

        aggregatorProductHelper.putAggregatorProductInfoToModel(pageSize, activePage, productName, model, childCategoryIds);
        return "aggregate-products";
    }



    private List<CategoryEntity> getParentCategories(CategoryEntity category) {
        List<CategoryEntity> parentCategories = new ArrayList<>();
        while (category.getParentId() != null) {
            parentCategories.add(0, category.getParentCategory());
            category = category.getParentCategory();
        }
        return parentCategories;
    }

    private List<Long> getChildCategoryIds(CategoryEntity category) {
        List<Long> ids = new ArrayList<>();
        ids.add(category.getId());
        for (CategoryEntity childCategory : category.getChildCategories()) {
            ids.add(childCategory.getId());
            ids.addAll(getChildCategoryIds(childCategory));
        }
        return ids;
    }
}

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
    private AggregatorProductRepository aggregatorProductRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("products")
    public String list(@RequestParam Long categoryId, @RequestParam int pageSize, @RequestParam int activePage, Model model) {
        Optional<CategoryEntity> category = categoryRepository.findById(categoryId);
        Pageable paging = PageRequest.of(activePage - 1, pageSize);
        List<AggregatorProductEntity> aggregatorProductEntitiesByCategoryId = aggregatorProductRepository.findAggregatorProductEntitiesByCategoryId(categoryId, paging);
        List<AggregatorProductEntity> allAggregatorProductEntitiesByCategoryId = aggregatorProductRepository.findAggregatorProductEntitiesByCategoryId(categoryId);
        model.addAttribute("title", category.get().getName());
        model.addAttribute("categoryId", category.get().getId());
        model.addAttribute("prods", aggregatorProductEntitiesByCategoryId);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("activePage", activePage);
        model.addAttribute("pages", PagingUtil.buildPageNavItem(pageSize, allAggregatorProductEntitiesByCategoryId.size(), activePage));
        return "aggregate-products";
    }
}

package org.madi.productaggregator.web.controllers;

import org.madi.productaggregator.web.dao.AggregatorProductRepository;
import org.madi.productaggregator.web.entities.AggregatorProductEntity;
import org.madi.productaggregator.web.util.PagingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class AggregatorProductHelper {
    @Autowired
    private AggregatorProductRepository aggregatorProductRepository;

    public void putAggregatorProductInfoToModel(int pageSize, int activePage, String productName, Model model, List<Long> childCategoryIds) {
        Pageable paging = PageRequest.of(activePage - 1, pageSize);
        List<AggregatorProductEntity> aggregatorProductEntities;
        int aggProdCount;
        if (productName != null) {
            String productNameTempl = "%" + productName + "%";
            if (childCategoryIds != null ) {
                aggregatorProductEntities = aggregatorProductRepository.findAggregatorProductEntitiesByCategoryIdInAndNameLike(childCategoryIds, productNameTempl, paging);
                aggProdCount = aggregatorProductRepository.countByCategoryIdInAndNameLike(childCategoryIds, productNameTempl);
            } else {
                aggregatorProductEntities = aggregatorProductRepository.findAggregatorProductEntitiesByNameLike(productNameTempl, paging);
                aggProdCount = aggregatorProductRepository.countByNameLike(productNameTempl);
            }
        } else {
            if (childCategoryIds != null) {
                aggregatorProductEntities = aggregatorProductRepository.findAggregatorProductEntitiesByCategoryIdIn(childCategoryIds, paging);
                aggProdCount = aggregatorProductRepository.countByCategoryIdIn(childCategoryIds);
            } else {
                aggregatorProductEntities = aggregatorProductRepository.findAllBy(paging);
                aggProdCount = aggregatorProductRepository.countBy();
            }
        }

        model.addAttribute("aggProdCount", aggProdCount);
        model.addAttribute("productName", productName);
        model.addAttribute("prods", aggregatorProductEntities);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("activePage", activePage);
        model.addAttribute("pages", PagingUtil.buildPageNavItem(pageSize, aggProdCount, activePage));
    }
}

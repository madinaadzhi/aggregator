package org.madi.productaggregator.web.controllers;

import org.apache.commons.lang.StringUtils;
import org.madi.productaggregator.web.dao.ProductRepository;
import org.madi.productaggregator.web.entities.ProductEntity;
import org.madi.productaggregator.web.util.PagingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminProductController {

    @Autowired
    private ProductRepository productRepository;
    @GetMapping("admin/products")
    public String list( @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer activePage,
                       @RequestParam(required = false) String productName,
                        @RequestParam(required = false) Boolean success,
                        Model model) {
        if (pageSize == null ) {
            pageSize = 25;
        }
        if (activePage == null) {
            activePage = 1;
        }
        Pageable paging = PageRequest.of(activePage - 1, pageSize);
        int productCount;
        List<ProductEntity> products;

        if (StringUtils.isNotBlank(productName)) {
            String prodNameTemplate = '%' + productName + '%';
            productCount = productRepository.countByAggregatorProductEntityIdIsNullAndNameLike(prodNameTemplate);
            products = productRepository.findProductEntitiesByAggregatorProductEntityIdIsNullAndNameLike(prodNameTemplate, paging);
        } else {
            productCount = productRepository.countByAggregatorProductEntityIdIsNull();
            products = productRepository.findProductEntitiesByAggregatorProductEntityIdIsNull(paging);
        }

        if (success != null && success) {
            model.addAttribute("success", success);
        }
        model.addAttribute("productCount", productCount);
        model.addAttribute("productName", productName);
        model.addAttribute("products", products);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("activePage", activePage);
        model.addAttribute("pages", PagingUtil.buildPageNavItem(pageSize, productCount, activePage));

        return "admin-supermarkets-products";
    }
}
package org.madi.productaggregator.web.controllers;

import org.madi.productaggregator.web.entities.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoryUtils {
    public static List<CategoryEntity> buildCategories(List<CategoryEntity> parentCategories) {
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

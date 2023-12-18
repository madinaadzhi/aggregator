package org.madi.productaggregator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.madi.productaggregator.web.dao.CategoryRepository;
import org.madi.productaggregator.web.entities.CategoryEntity;
import org.madi.productaggregator.web.market.api.Product;
import org.madi.productaggregator.web.market.impl.silpo.SilpoUtils;
import org.madi.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class SilpoUtilsTests {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testParseProduct() throws IOException {
        String json = FileUtil.getFileContent("silpo/product/response.json");
        List<Product> products = SilpoUtils.getProduct(json);
        Assertions.assertNotNull(products);
        Assertions.assertEquals(30, products.size());
        for (Product product : products) {
            Assertions.assertEquals("422", product.getCategoryId());
        }
        Product product = products.get(4);
        Assertions.assertEquals("Тамарило", product.getName());
    }

    @Test
    public void testParseEmptyProducts() throws IOException {
        String json = FileUtil.getFileContent("silpo/product/response-empty-products.json");
        List<Product> products = SilpoUtils.getProduct(json);
        Assertions.assertNotNull(products);
        Assertions.assertEquals(0, products.size());
    }

    @Test
    public void testGetNotImportedCategories() {
        List<CategoryEntity> categories = categoryRepository.getCategoryEntitiesByMarketId(9L);
        Assertions.assertNotNull(categories);
        Assertions.assertEquals(false, categories.get(0).isImported());
    }

}

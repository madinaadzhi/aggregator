ALTER TABLE product
    ADD CONSTRAINT fk_product_category_id FOREIGN KEY (category_id) REFERENCES categories(id);
ALTER TABLE aggr_products
    ADD CONSTRAINT fk_agg_product_category_id FOREIGN KEY (category_id) REFERENCES categories(id);

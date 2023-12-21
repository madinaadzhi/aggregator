insert into aggr_products (category_id, image_url, name)
select distinct category_id, image_url, name from product
                                    where market_id = (select id from markets where domain_name = 'shop.silpo.ua') and image_url not like '%default%';

update aggr_products ap
    set ap.category_id =
        (select agg_cat.id as agg_cat_id
FROM (
select c.id, c.name, c.external_id from categories c where market_id = 2) as silpo_cat
join (select c.id, c.name, c.external_id from categories c where market_id = 1) as agg_cat on silpo_cat.external_id = agg_cat.external_id
where silpo_cat.id = ap.category_id);


update product p
set p.aggr_product_id = (select ap.id from aggr_products ap where ap.image_url = p.image_url)
where p.aggr_product_id is null;
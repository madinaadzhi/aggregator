create table aggr_products
(
    id          bigint auto_increment
        primary key,
    category_id bigint        not null,
    image_url   varchar(256) null,
    name        varchar(512)  not null
);

create index aggr_products_image_url on aggr_products(image_url);
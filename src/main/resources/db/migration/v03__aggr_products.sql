create table aggr_products
(
    id          bigint auto_increment
        primary key,
    category_id bigint        not null,
    image_url   varchar(1024) null,
    name        varchar(512)  not null
);


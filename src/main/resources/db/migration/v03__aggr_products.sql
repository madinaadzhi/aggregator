create table aggr_products
(
    id          bigint auto_increment
        primary key,
    category_id bigint       not null,
    image_url   varchar(256) null,
    name        varchar(256) not null,
    unit varchar (256) not null

);

create index aggr_products_image_url on aggr_products (image_url);
create index aggr_products_category_id on aggr_products (category_id);
create index aggr_products_name on aggr_products (name);
create index aggr_products_unit on aggr_products (unit);

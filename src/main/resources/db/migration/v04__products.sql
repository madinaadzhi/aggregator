create table product
(
    id              bigint auto_increment
        primary key,
    category_id     bigint        not null,
    external_id     varchar(1024) not null,
    image_url       varchar(256) null,
    is_available    bit           not null,
    name            varchar(512)  not null,
    price           double        not null,
    site_url        varchar(1024) not null,
    aggr_product_id bigint        null,
    market_id       bigint        null,
    constraint FKee90sc62qjuybcexvgyjln2cd
        foreign key (aggr_product_id) references aggr_products (id),
    constraint FKfl54i2ot7gmvmee2ers94uuao
        foreign key (market_id) references markets (id)
);

create index products_image_url on product(image_url);

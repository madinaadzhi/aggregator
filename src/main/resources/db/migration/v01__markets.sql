create table markets
(
    id          bigint auto_increment
        primary key,
    domain_name varchar(512)  not null,
    logo_url    varchar(1024) null,
    name        varchar(512)  not null
);

INSERT INTO aggDB.markets (id, domain_name, logo_url, name) VALUES (1, 'aggregator.ua', '', 'Aggregator');

package org.madi.productaggregator.web.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "Product")
public class ProductEntity {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "image_name")
    private String imgName;
    @Column(name = "name")
    private String name;
    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "price")
    private double price;
    @Column(name = "site_url")
    private String siteUrl;
    @ManyToOne
    @JoinColumn(name = "aggr_product_id")
    private AggregatorProductEntity aggregatorProductEntity;
    @Column(name = "image_url")
    private String imgUrl;
    @ManyToOne
    @JoinColumn(name = "market_id")
    private MarketEntity marketEntity;
}

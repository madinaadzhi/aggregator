package org.madi.productaggregator.web.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "Product")
public class ProductEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 512, nullable = false)
    private String name;
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;
    @Column(name = "site_url", length = 1024, nullable = false)
    private String siteUrl;
    @ManyToOne
    @JoinColumn(name = "aggr_product_id")
    private AggregatorProductEntity aggregatorProductEntity;
    @Column(name = "image_url", length = 1024)
    private String imgUrl;
    @ManyToOne
    @JoinColumn(name = "market_id")
    private MarketEntity marketEntity;
    @Column(name = "external_id", length = 1024, nullable = false)
    private String externalId;
    @Column(name = "unit", length = 256, nullable = false)
    private String unit;
}

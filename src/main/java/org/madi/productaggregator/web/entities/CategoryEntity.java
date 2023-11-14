package org.madi.productaggregator.web.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "categories")
public class CategoryEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "market_id")
    private Long marketId;
    @Column(name = "external_id")
    private String externalId;
    @Column(name = "logo_url")
    private String logoUrl;
    @OneToMany(mappedBy="parentId", fetch = FetchType.EAGER)
    private List<CategoryEntity> childCategories;
//    @ManyToOne
//    @JoinColumn(name = "market_id")
//    private MarketEntity market;
}

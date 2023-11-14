package org.madi.productaggregator.web.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "aggr_products")
public class AggregatorProductEntity {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "image_url")
    private String imageUrl;
    @OneToMany(mappedBy="aggregatorProductEntity", fetch = FetchType.EAGER)
    private List<ProductEntity> productEntities;

    public AggregatorProductEntity(Long id, String name, Long categoryID, String imageUrl) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryID;
        this.imageUrl = imageUrl;
    }


}

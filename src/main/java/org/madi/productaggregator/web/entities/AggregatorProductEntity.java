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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 512, nullable = false)
    private String name;
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    @ManyToOne
    @JoinColumn(name = "category_id", updatable = false, insertable = false)
    private CategoryEntity category;
    @Column(name = "image_url", length = 1024)
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

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
    @Column(name = "name", length = 512, nullable = false)
    private String name;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "market_id", nullable = false)
    private Long marketId;
    @Column(name = "external_id", length = 1024, nullable = false)
    private String externalId;
    @Column(name = "logo_url", length = 1024)
    private String logoUrl;
    @OneToMany(mappedBy = "parentId", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    private List<CategoryEntity> childCategories;
    @ManyToOne
    @JoinColumn(name = "parent_id", updatable = false, insertable = false)
    private CategoryEntity parentCategory;
    @Column(name = "is_imported", nullable = false)
    private boolean isImported;
//    @ManyToOne
//    @JoinColumn(name = "market_id")
//    private MarketEntity market;

    public int getLevel() {
        int level = 1;
        CategoryEntity category = this;
        while (category.getParentCategory() != null) {
            level++;
            category = category.getParentCategory();
        }
        return level;
    }


}

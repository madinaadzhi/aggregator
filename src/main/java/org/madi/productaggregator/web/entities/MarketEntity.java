package org.madi.productaggregator.web.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "markets")
public class MarketEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 512, nullable = false)
    private String name;
    @Column(name = "logo_url", length = 1024)
    private String logoUrl;
    @Column(name = "domain_name", length = 512, nullable = false)
    private String domainName;
    @OneToMany(mappedBy="marketEntity", fetch = FetchType.EAGER)
    private List<ProductEntity> productEntities;
}

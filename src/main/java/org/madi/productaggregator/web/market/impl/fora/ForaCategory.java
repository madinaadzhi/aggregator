package org.madi.productaggregator.web.market.impl.fora;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.madi.productaggregator.web.market.api.Category;

@AllArgsConstructor
@Data
public class ForaCategory implements Category {
    private String id;
    private String name;
    private String parentId;
    private String logoUrl;

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getLogoUrl() {
        return logoUrl;
    }


}

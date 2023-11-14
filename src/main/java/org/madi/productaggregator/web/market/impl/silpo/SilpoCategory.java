package org.madi.productaggregator.web.market.impl.silpo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.madi.productaggregator.web.market.api.Category;

@AllArgsConstructor
@Data
public class SilpoCategory implements Category {
    private String id;
    private String name;
    private String parentId;
    private String logoUrl;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLogoUrl() {
        return logoUrl;
    }


}

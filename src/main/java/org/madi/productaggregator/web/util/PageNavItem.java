package org.madi.productaggregator.web.util;

import lombok.Data;

@Data
public class PageNavItem {
    private int pageNumber;
    private boolean isActive;

    public PageNavItem(int pageNumber, boolean isActive) {
        this.pageNumber = pageNumber;
        this.isActive = isActive;
    }
}

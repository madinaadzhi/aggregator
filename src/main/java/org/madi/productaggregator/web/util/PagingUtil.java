package org.madi.productaggregator.web.util;

import java.util.ArrayList;
import java.util.List;

public class PagingUtil {
    public static List<PageNavItem> buildPageNavItem(int pageSize, int totalProdCnt, int activePage) {
        int navItemCnt = 5;
        List<PageNavItem> pageNums = new ArrayList<>();
        double pageCnt = Math.round((double) totalProdCnt / pageSize);
        if (totalProdCnt % pageSize != 0) {
            pageCnt += 1;
        }
        if (totalProdCnt <= pageSize) {
            pageNums.add(new PageNavItem(activePage, true));
        } else if ((activePage == 1 || activePage == 2) && pageCnt > navItemCnt) {
            for (int i = 1; i <= navItemCnt; i++) {
                pageNums.add(new PageNavItem(i, i == activePage));
            }
        } else if ((activePage == pageCnt - 1) && pageCnt > navItemCnt) {
            for (int i = activePage - 3; i <= activePage + 1; i++) {
                pageNums.add(new PageNavItem(i, i == activePage));
            }
        } else if ((activePage == pageCnt) && pageCnt > navItemCnt) {
            for (int i = activePage - 4; i <= activePage; i++) {
                pageNums.add(new PageNavItem(i, i == activePage));
            }
        } else if (pageCnt <= navItemCnt) {
            for (int i = 1; i <= pageCnt; i++) {
                pageNums.add(new PageNavItem(i, i == activePage));
            }
        } else {
            for (int i = activePage - 2; i <= activePage + 2; i++) {
                pageNums.add(new PageNavItem(i, i == activePage));
            }
        }
        return pageNums;
    }
}

package com.djl.dmall.search;

import com.djl.dmall.vo.search.SearchParam;
import com.djl.dmall.vo.search.SearchResponse;

/**
 * 商品的检索服务
 */
public interface SearchProductService {
    /**
     * 检索商品
     * @param searchParam
     * @return
     */
    SearchResponse searchProduct(SearchParam searchParam);
}

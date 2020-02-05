package com.djl.dmall.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.djl.dmall.pms.service.ProductService;
import com.djl.dmall.to.es.EsProduct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductItemController {

    @Reference
    ProductService productService;

    /**
     * 商品的详情
     * @param id
     * @return
     */
    @GetMapping("/item/{id}.html")
    public EsProduct productInfo(@PathVariable("id") Long id) {
        EsProduct esProduct = productService.productAllInfo(id);
        return esProduct;
    }

    /**
     * 根据商品的sku查询
     * @param id
     * @return
     */
    @GetMapping("/item/sku/{id}.html")
    public EsProduct productSkuInfo(@PathVariable("id") Long id){
        EsProduct esProduct = productService.productSkuInfo(id);
        return esProduct;
    }

}

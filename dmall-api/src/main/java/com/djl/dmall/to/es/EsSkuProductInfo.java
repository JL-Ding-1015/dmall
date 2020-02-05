package com.djl.dmall.to.es;

import com.djl.dmall.pms.entity.SkuStock;

import java.io.Serializable;
import java.util.List;

public class EsSkuProductInfo extends SkuStock implements Serializable {
    //sku的特定标题
    private String skuTitle;

    public String getSkuTitle() {
        return skuTitle;
    }

    public void setSkuTitle(String skuTitle) {
        this.skuTitle = skuTitle;
    }

    /**
     * 每个sku有不同的属性以及其他的值
     *
     * 颜色：黑色
     * 内存128g
     *
     */
    List<EsProductAttributeValue> attributeValues;

    public List<EsProductAttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<EsProductAttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }
}

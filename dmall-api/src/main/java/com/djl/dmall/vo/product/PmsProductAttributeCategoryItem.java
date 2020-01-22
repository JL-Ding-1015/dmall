package com.djl.dmall.vo.product;

import com.djl.dmall.pms.entity.ProductAttribute;
import com.djl.dmall.pms.entity.ProductAttributeCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 包含有分类下属性的vo
 */
@Data
public class PmsProductAttributeCategoryItem extends ProductAttributeCategory  implements Serializable {
    private List<ProductAttribute> productAttributeList;


}

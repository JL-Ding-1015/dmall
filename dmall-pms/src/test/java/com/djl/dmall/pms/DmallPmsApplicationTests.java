package com.djl.dmall.pms;

import com.djl.dmall.pms.entity.Brand;
import com.djl.dmall.pms.entity.Product;
import com.djl.dmall.pms.service.BrandService;
import com.djl.dmall.pms.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DmallPmsApplicationTests {

    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

    @Test
    void contextLoads() {
        /*Product product = productService.getById(2);
        System.out.println(product.getName());*/


        //读写分离测试
        /*Brand brand = new Brand();
        brand.setName("djl北京");
        brandService.save(brand);
        System.out.println("保存操作执行了......");*/
        //人为造成主从不一致之后，进行读取测试
        Brand brand = brandService.getById(53);
        System.out.println(brand.getName());
    }

}

package com.djl.dmall.pms;

import com.djl.dmall.pms.entity.Brand;
import com.djl.dmall.pms.entity.Product;
import com.djl.dmall.pms.service.BrandService;
import com.djl.dmall.pms.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class DmallPmsApplicationTests {

    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Autowired
    RedisTemplate<Object,Object> redisTemplateObj;

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
        Brand brand = brandService.getById(50);
        System.out.println(brand.getName());
    }

    @Test
    void myTest(){
        redisTemplate.opsForValue().set("hello","world");
        System.out.println("保存了数据");

        System.out.println(redisTemplate.opsForValue().get("hello"));

    }

    @Test
    void myTestObj(){
        Brand brand = new Brand();
        brand.setName("my brand");
        redisTemplateObj.opsForValue().set("objobj",brand);

        System.out.println("保存了对象");

        Brand brand_ret = (Brand) redisTemplateObj.opsForValue().get("objobj");
        System.out.println("从redis读取的对象名称是: " + brand_ret.getName());

    }

}

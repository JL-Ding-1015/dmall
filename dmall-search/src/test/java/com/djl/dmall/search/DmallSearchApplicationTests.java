package com.djl.dmall.search;

import com.djl.dmall.vo.search.SearchParam;
import io.searchbox.client.JestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DmallSearchApplicationTests {

    @Autowired
    SearchProductService searchProductService;

    @Test
    public void dslTest(){
        SearchParam searchParam = new SearchParam();
        searchParam.setKeyword("手机");

        String[] strings = {"苹果"};
        searchParam.setBrand(strings);

        String[] cate = {"19", "20"};
        searchParam.setCatelog3(cate);

        searchParam.setPriceFrom(2000);
        searchParam.setPriceTo(8000);

        String[] props = {"45:4.7","46:4G"};
        searchParam.setProps(props);
        searchProductService.searchProduct(searchParam);
    }
    @Test
    void contextLoads() {
    }

}

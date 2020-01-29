package com.djl.esdemo;

import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class EsDemoApplicationTests {

    @Autowired
    JestClient jestClient;

    @Test
    void contextLoads() throws IOException {
        esUser user = new esUser();
        user.setUserName("ddd");
        user.setAge(20);
        user.setEmail("aaa.com");
        Index index = new Index.Builder(user)
                .index("user")
                .type("userInfo")
                .build();

        DocumentResult execute = jestClient.execute(index);

        System.out.println("执行成功吗？" + execute.isSucceeded() + "---》" + execute.getVersion());

    }

    @Test
    void myTest() throws IOException {

        String jsonQuery = "{\n" +
                "  \"query\": {\n" +
                "    \"match_all\": {}\n" +
                "  }\n" +
                "}";

        Search search = new Search.Builder(jsonQuery).addIndex("user").build();

        SearchResult execute = jestClient.execute(search);
        List<SearchResult.Hit<esUser, Void>> hits = execute.getHits(esUser.class);
        for (SearchResult.Hit<esUser, Void> hit : hits) {
            esUser user = hit.source;
            System.out.println(user.getUserName());
            System.out.println(user.getAge());
        }

    }

}

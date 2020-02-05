package com.djl.dmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.djl.dmall.contant.EsConstant;
import com.djl.dmall.search.SearchProductService;
import com.djl.dmall.to.es.EsProduct;
import com.djl.dmall.vo.search.SearchParam;
import com.djl.dmall.vo.search.SearchResponse;
import com.djl.dmall.vo.search.SearchResponseAttrVo;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.Aggregation;
import io.searchbox.core.search.aggregation.ChildrenAggregation;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.swing.text.Highlighter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Component
public class SearchProductServiceImpl implements SearchProductService {

    @Autowired
    JestClient jestClient;


    @Override
    public SearchResponse searchProduct(SearchParam searchParam) {

        //1.构建dsl语句
        String dsl = buildDsl(searchParam);
        log.debug("商品检索的dsl数据：{}", dsl);

        //2.检索
        Search search = new Search.Builder(dsl)
                .addIndex(EsConstant.PRODUCT_ES_INDEX)
                .addType(EsConstant.PRODUCT_ES_TYPE)
                .build();
        SearchResult execute = null;
        try {
            execute = jestClient.execute(search);
            if (execute.isSucceeded()) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //3.通过对execute中的数据包装成SearchResponse返回
        SearchResponse searchResponse = buildSearchResponse(execute);
        searchResponse.setPageNum(searchParam.getPageNum());
        searchResponse.setPageSize(searchParam.getPageSize());
        return searchResponse;
    }

    private SearchResponse buildSearchResponse(SearchResult execute) {
        MetricAggregation aggregations = execute.getAggregations();
        //=======分析品牌信息++++++
        SearchResponseAttrVo brandVo = new SearchResponseAttrVo();
        TermsAggregation brand_agg = aggregations.getTermsAggregation("brand_agg");
        brandVo.setName("品牌");
        List<TermsAggregation.Entry> brandAggBuckets = brand_agg.getBuckets();
        List<String> brandNames = new ArrayList<>();
        for (TermsAggregation.Entry brandAggBucket : brandAggBuckets) {
            brandNames.add(brandAggBucket.getKeyAsString());
        }
        brandVo.setValue(brandNames);
        //=======分析品牌信息------

        //=======提取分类信息++++++
        SearchResponseAttrVo categoryVo = new SearchResponseAttrVo();
        TermsAggregation category_agg = aggregations.getTermsAggregation("category_agg");
        List<TermsAggregation.Entry> categoryAggBuckets = category_agg.getBuckets();
        categoryVo.setName("分类");
        List<String> categoryInfo = new ArrayList<>();
        for (TermsAggregation.Entry categoryAggBucket : categoryAggBuckets) {
            String categoryName = categoryAggBucket.getKeyAsString();
            String categoryId = categoryAggBucket.getTermsAggregation("categoryId_agg")
                    .getBuckets().get(0).getKeyAsString();
            HashMap<String, String> categoryMap = new HashMap<>();
            categoryMap.put("name", categoryName);
            categoryMap.put("id", categoryId);
            String json = JSON.toJSONString(categoryMap);
            categoryInfo.add(json);
        }
        categoryVo.setValue(categoryInfo);
        //=======提取分类信息------

        // =======提取聚合的属性信息++++++
        List<SearchResponseAttrVo> attrs = new ArrayList<>();
        ChildrenAggregation attr_agg = aggregations.getChildrenAggregation("attr_agg");
        TermsAggregation attrName_agg = attr_agg.getTermsAggregation("attrName_agg");
        List<TermsAggregation.Entry> attrsBuckets = attrName_agg.getBuckets();
        if(attrsBuckets == null || attrsBuckets.size() == 0){
            log.error("目前的查询条件下没有任何属性");
        }else{
            for (TermsAggregation.Entry attrsBucket : attrsBuckets) {
                String attrName = attrsBucket.getKeyAsString();
                String attrId = null;
                List<TermsAggregation.Entry> attrIdBuckets = attrsBucket.getTermsAggregation("attrId_agg").getBuckets();
                if(attrIdBuckets != null && attrIdBuckets.size() > 0){
                    attrId = attrIdBuckets.get(0).getKeyAsString();
                }
                List<TermsAggregation.Entry> attrValueBuckets = attrsBucket.getTermsAggregation("attrValue_agg").getBuckets();
                List<String> values = new ArrayList<>();
                if(attrValueBuckets != null && attrValueBuckets.size() > 0){
                    for (TermsAggregation.Entry attrValueBucket : attrValueBuckets) {
                        String attrValue = attrValueBucket.getKeyAsString();
                        values.add(attrValue);
                    }
                }
                if(attrName == null || attrId == null || values.size() == 0){
                    log.error("当前查询之后的聚合结果存在空数据现象");
                }else{
                    SearchResponseAttrVo attrVo = new SearchResponseAttrVo();
                    attrVo.setName(attrName);
                    attrVo.setProductAttributeId(Long.parseLong(attrId));
                    attrVo.setValue(values);
                    attrs.add(attrVo);
                }

            }
        }
        //=======提取聚合的属性信息------

        //=======提取商品信息++++++
        List<SearchResult.Hit<EsProduct, Void>> hits = execute.getHits(EsProduct.class);
        List<EsProduct> esProducts = new ArrayList<>();
        for (SearchResult.Hit<EsProduct, Void> hit : hits) {
            EsProduct product = hit.source;
            Map<String, List<String>> highlight = hit.highlight;
            if(highlight != null && highlight.size() > 0){
                List<String> highLightTitles = highlight.get("skuProductInfos.skuTitle");
                if(highLightTitles != null && highLightTitles.size() > 0){
                    product.setName(highLightTitles.get(0));
                }
            }
            esProducts.add(product);
        }

        //=======提取商品信息------





        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setAttrs(attrs);//所有商品的顶头显示的筛选属性
        searchResponse.setBrand(brandVo);
        searchResponse.setCatelog(categoryVo);
        searchResponse.setProducts(esProducts);//检索出来的商品信息
        searchResponse.setTotal(execute.getTotal());


        return searchResponse;
    }

    private String buildDsl(SearchParam searchParam) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //1.查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 1.1. 检索
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            MatchQueryBuilder matchQueryBuilder = QueryBuilders
                    .matchQuery("skuProductInfos.skuTitle", searchParam.getKeyword());
            NestedQueryBuilder nestedQueryBuilder = QueryBuilders
                    .nestedQuery("skuProductInfos", matchQueryBuilder, ScoreMode.None);
            boolQuery.must(nestedQueryBuilder);
        }
        // 1.2. 过滤
        if (searchParam.getCatelog3() != null && searchParam.getCatelog3().length > 0) {
            //按照三级分类数据查询
            boolQuery.filter(QueryBuilders.termsQuery("productCategoryId", searchParam.getCatelog3()));

        }
        if (searchParam.getBrand() != null && searchParam.getBrand().length > 0) {
            //按照品牌数据查询
            boolQuery.filter(QueryBuilders.termsQuery("brandName.keyword", searchParam.getBrand()));

        }
        if (searchParam.getProps() != null && searchParam.getProps().length > 0) {
            String[] props = searchParam.getProps();
            for (String prop : props) {
                String[] split = prop.split(":");

                BoolQueryBuilder must = QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("attrValueList.productAttributeId", split[0]))
                        .must(QueryBuilders.termsQuery("attrValueList.value.keyword", split[1].split("-")));
                NestedQueryBuilder query = QueryBuilders.nestedQuery("attrValueList", must, ScoreMode.None);
                boolQuery.filter(query);
            }
        }
        if (searchParam.getPriceFrom() != null || searchParam.getPriceTo() != null) {
            //价格区间过滤
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("price");
            if (searchParam.getPriceFrom() != null) {
                rangeQuery.gte(searchParam.getPriceFrom());
            }
            if (searchParam.getPriceTo() != null) {
                rangeQuery.lte(searchParam.getPriceTo());
            }
            boolQuery.filter(rangeQuery);
        }
        //完成查询
        builder.query(boolQuery);

        //2.高亮
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuProductInfos.skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            builder.highlighter(highlightBuilder);
        }

        //3.聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg").field("brandName.keyword");
        brand_agg.subAggregation(AggregationBuilders.terms("brandId").field("brandId"));
        builder.aggregation(brand_agg);

        TermsAggregationBuilder category_agg = AggregationBuilders.terms("category_agg").field("productCategoryName.keyword");
        category_agg.subAggregation(AggregationBuilders.terms("categoryId_agg").field("productCategoryId"));
        builder.aggregation(category_agg);

        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrValueList");
        TermsAggregationBuilder attrName_agg = AggregationBuilders.terms("attrName_agg").field("attrValueList.name");
        attrName_agg.subAggregation(AggregationBuilders.terms("attrValue_agg").field("attrValueList.value.keyword"));
        attrName_agg.subAggregation(AggregationBuilders.terms("attrId_agg").field("attrValueList.productAttributeId"));
        attr_agg.subAggregation(attrName_agg);
        builder.aggregation(attr_agg);



        //4.分页
        builder.from((searchParam.getPageNum() - 1) * searchParam.getPageSize());
        builder.size(searchParam.getPageSize());
        //5.排列
        if (!StringUtils.isEmpty(searchParam.getOrder())) {
            String order = searchParam.getOrder();
            String[] split = order.split(":");
            if (split[0].equals("0")) {
                //综合排序
            }
            if (split[0].equals("1")) {
                //销量排序
                FieldSortBuilder sale = SortBuilders.fieldSort("sale");
                if (split[1].equalsIgnoreCase("asc")) {
                    sale.order(SortOrder.ASC);
                } else {
                    sale.order(SortOrder.DESC);
                }
                builder.sort(sale);
            }
            if (split[0].equals("2")) {
                //价格排序
                FieldSortBuilder price = SortBuilders.fieldSort("price");
                if (split[1].equalsIgnoreCase("asc")) {
                    price.order(SortOrder.ASC);
                } else {
                    price.order(SortOrder.DESC);
                }
                builder.sort(price);
            }
        }
        return builder.toString();
    }


}

package com.djl.dmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djl.dmall.contant.EsConstant;
import com.djl.dmall.pms.entity.*;
import com.djl.dmall.pms.mapper.*;
import com.djl.dmall.pms.service.ProductService;
import com.djl.dmall.to.es.EsProduct;
import com.djl.dmall.to.es.EsProductAttributeValue;
import com.djl.dmall.to.es.EsSkuProductInfo;
import com.djl.dmall.vo.PageInfoVo;
import com.djl.dmall.vo.product.PmsProductParam;
import com.djl.dmall.vo.product.PmsProductQueryParam;
import io.searchbox.client.JestClient;
import io.searchbox.core.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author JL_Ding
 * @since 2020-01-20
 */
@Slf4j
@Service
@Component
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductAttributeValueMapper productAttributeValueMapper;

    @Autowired
    ProductFullReductionMapper productFullReductionMapper;

    @Autowired
    ProductLadderMapper productLadderMapper;

    @Autowired
    SkuStockMapper skuStockMapper;

    @Autowired
    JestClient jestClient;

    //当前线程共享变量
    ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    @Override
    public PageInfoVo productPageInfo(PmsProductQueryParam param) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();

        if (param.getBrandId() != null) {
            wrapper.eq("brand_id", param.getBrandId());
        }
        if (!StringUtils.isEmpty(param.getKeyword())) {
            wrapper.like("name", param.getKeyword());
        }
        if (param.getProductCategoryId() != null) {
            wrapper.eq("product_category_id", param.getProductCategoryId());
        }
        if (!StringUtils.isEmpty(param.getProductSn())) {
            wrapper.like("product_sn", param.getProductSn());
        }
        if (param.getPublishStatus() != null) {
            wrapper.eq("publish_status", param.getPublishStatus());
        }
        if (param.getVerifyStatus() != null) {
            wrapper.eq("verify_status", param.getVerifyStatus());
        }


        IPage<Product> page = productMapper.selectPage(new Page<Product>(param.getPageNum(),
                param.getPageSize()), wrapper);

        PageInfoVo resPageInfo = new PageInfoVo(page.getTotal(), page.getPages(), param.getPageSize(),
                page.getRecords(), page.getCurrent());
        new PageInfoVo();

        return resPageInfo;
    }

    /**
     * 保存商品的所有数据
     * 考虑事务....
     * 1）、哪些东西是一定要回滚的、哪些即使出错了不必要回滚的。
     * 商品的核心信息（基本数据、sku）保存的时候，不要受到别的无关信息的影响。
     * 无关信息出问题，核心信息也不用回滚的。
     * 2）、事务的传播行为;propagation:当前方法的事务[是否要和别人共用一个事务]如何传播下去（里面的方法如果用事务，是否和他共用一个事务）
     * <p>
     * Propagation propagation() default Propagation.REQUIRED;
     * <p>
     * REQUIRED:(必须):
     * Support a current transaction, create a new one if none exists.
     * 如果以前有事务，就和之前的事务公用一个事务，没有就创建一个事务。
     * <p>
     * REQUIRES_NEW（总是用新的事务）:
     * Create a new transaction, and suspend the current transaction if one exists.
     * 创建一个新的事务，如果以前有事务，暂停前面的事务。
     * <p>
     * SUPPORTS（支持）:
     * Support a current transaction, execute non-transactionally if none exists.
     * 之前有事务，就以事务的方式运行，没有事务也可以。
     * <p>
     * MANDATORY（强制）:没事务就报错
     * Support a current transaction, throw an exception if none exists
     * 一定要有事务，如果没事务就报错
     * <p>
     * NOT_SUPPORTED（不支持）:
     * Execute non-transactionally, suspend the current transaction if one exists
     * 不支持在事务内运行，如果已经有事务了，就挂起当前存在的事务
     * <p>
     * NEVER（从不使用）:
     * Execute non-transactionally, throw an exception if a transaction exists.
     * 不支持在事务内运行，如果已经有事务了，抛异常
     * <p>
     * NESTED:
     * Execute within a nested transaction if a current transaction exists,
     * 开启一个子事务（MySQL不支持），需要支持还原点功能的数据库才行；
     * <p>
     * <p>
     * 一家人带着老王去旅游；
     * 一家人：开自己的车还是坐老王的车
     * <p>
     * Required：坐老王车
     * Requires_new：一定得开车，开新的
     * <p>
     * SUPPORTS：用车，有车就用，没车走路；
     * MANDATORY：用车，没车就骂街。。。
     * <p>
     * NOT_SUPPORTED：不支持用车。有车放那不用
     * NEVER：从不用车，有车抛异常
     * <p>
     * <p>
     * <p>
     * <p>
     * 外事务{
     * <p>
     * A();//事务.Required：跟着回滚
     * b();//事务.Requires_new：不回滚
     * //自己给数据库插入数据
     * int i = 10/0;
     * }
     * <p>
     * Required_new
     * 外事务{
     * A（）;Required; A
     * B（）;Requires_new B
     * try{
     * C();Required; C
     * }catch(Exception e){
     * //c出异常？
     * }
     * D();Requires_new; D
     * //给数据库存 --外
     * // int i = 10/0;
     * }
     * <p>
     * 场景1：
     * A方法出现了异常；由于异常机制导致代码停止，下面无法执行，数据库什么都没有
     * 场景2：
     * C方法出现异常；A回滚，B成功，C回滚，D无法执行，外无法执行
     * 场景3：
     * 外成了后，int i = 10/0; B,D成功。A,C,外都执行了但是必须回滚
     * 场景4：
     * D炸；抛异常。外事务感知到异常。A,C回滚，外执行不到，D自己回滚，B成功
     * 场景5：
     * C用try-catch执行；C出了异常回滚，由于异常被捕获，外事务没有感知异常。A,B,D都成，C自己回滚
     * <p>
     * 总结：
     * 传播行为过程中，只要Requires_new被执行过就一定成功，不管后面出不出问题。异常机制还是一样的，出现异常代码以后不执行。
     * Required只要感觉到异常就一定回滚。和外事务是什么传播行为无关。
     * <p>
     * 传播行为总是来定义，当一个事务存在的时候，他内部的事务该怎么执行。
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * 如何让某些可以不回滚
     * <p>
     * <p>
     * 事务Spring中是怎么做的？
     * TransactionManager；
     * AOP做；
     * 动态代理。
     * hahaServiceProxy.saveBaseInfo();
     * <p>
     * A{
     * A(){
     * B(); //1,2,3
     * C(); //4,5,6
     * D(); //7,8,9
     * }
     * }
     * <p>
     * 自己类调用自己类里面的方法，就是一个复制粘贴。归根到底，只是给
     * controller{
     * serviceProxy.a();
     * }
     * 对象.方法()才能加上事务。
     * <p>
     * <p>
     * A(){
     * //1,2,3,4,5,6,7,8,9
     * //
     * }
     * <p>
     * A{
     * A(){
     * hahaService.B();
     * hahaService.C();
     * hahaService.D();
     * <p>
     * }
     * }
     * <p>
     * 事务的问题：
     * Service自己调用自己的方法，无法加上真正的自己内部调整的各个事务
     * 解决：如果是  对象.方法()那就好了
     * 1）、要是能拿到ioc容器，从容器中再把我们的组件获取一下，用对象调方法。
     * <p>
     * <p>
     * <p>
     * 复习：事务传播行为，
     * ====================================================================
     * 隔离级别：解决读写加锁问题的（数据底层的方案）。  可重复读（快照）；
     * <p>
     * 读未提交：
     * 读已提交：
     * 可重复读：
     * 串行化：
     * <p>
     * ===========================================================
     * 异常回滚策略
     * 异常：
     * 运行时异常（不受检查异常）
     * ArithmeticException ......
     * 编译时异常（受检异常）
     * FileNotFound；1）要么throw要么try- catch
     * <p>
     * 运行的异常默认是一定回滚
     * 编译时异常默认是不回滚的；
     * rollbackFor：指定哪些异常一定回滚的。
     *
     * @param productParam 大保存...
     */
    @Transactional
    @Override
    public void saveProduct(PmsProductParam productParam) {
        ProductServiceImpl serviceProxy = (ProductServiceImpl) AopContext.currentProxy();

        //1.pms_product 保存商品的基本信息
        serviceProxy.saveBaseInfo(productParam);

        //5.pms_sku_stock sku的库存表
        serviceProxy.saveSkuStock(productParam);


        //如果上面两个出异常，下面的根本不会执行
        //2.pms_product_attribute_value 保存这个商品的所有属性值
        serviceProxy.saveAttributeValue(productParam);

        //3.pms_product_full_reduction 保存商品的满减信息
        serviceProxy.saveFullReduction(productParam);

        //4.pms_product_ladder 阶梯价格
        serviceProxy.saveLadderPrice(productParam);


    }

    @Override
    public void updateRecommendStatus(List<Long> ids, Integer recommendStatus) {

    }

    @Override
    public void updatePublishStatus(List<Long> ids, Integer publishStatus) {
        log.debug("商品的ids是：{}，要达到的状态是：{}", ids, publishStatus);
        if (publishStatus == 1) {
            //上架
            for (Long id : ids) {
                //1.对于数据库修改商品的上架状态值
                setProductPublishStatus(publishStatus, id);

                //2.对于es要求保存商品
                saveProductToEs(id);


            }
        } else {
            //下架
            for (Long id : ids) {

                //1.对于数据库修改商品的上架状态值
                setProductPublishStatus(publishStatus, id);

                //2.对于es要求删除商品
                deleteProductFromEs(id);
            }
        }


    }

    private void deleteProductFromEs(Long id) {
        //从Es中删除商品的数据
        Delete delete = new Delete.Builder(id.toString())
                .index(EsConstant.PRODUCT_ES_INDEX)
                .type(EsConstant.PRODUCT_ES_TYPE)
                .build();
        try {
            DocumentResult execute = jestClient.execute(delete);
            if (execute.isSucceeded()) {
                log.debug("Es删除商品 {} 成功。", id);
            } else {
                log.error("Es删除商品 {} 失败。", id);
            }
        } catch (IOException e) {
            log.error("Es删除商品 {} 失败。", id);
            e.printStackTrace();
        }

    }

    public void saveProductToEs(Long id) {
        Product productInfo = productInfoById(id);
        EsProduct esProduct = new EsProduct();
        //2.1基本信息
        BeanUtils.copyProperties(productInfo, esProduct);
        //2.2 spu信息
        List<EsProductAttributeValue> esProductAttributeValues = productAttributeValueMapper.selectBaseAttributeValues(id);
        esProduct.setAttrValueList(esProductAttributeValues);
        //2.3 sku信息
        List<SkuStock> skuStockList = skuStockMapper.selectList(new QueryWrapper<SkuStock>().eq("product_id", id));
        List<EsSkuProductInfo> esSkuProductInfoList = new ArrayList<>(skuStockList.size());
        //2.3.x 查询sku的attribute_name, p_a_id, type
        List<EsProductAttributeValue> skuAttributeNames = productAttributeValueMapper.selectSkuAttributeNames(id);

        for (SkuStock skuStock : skuStockList) {
            EsSkuProductInfo info = new EsSkuProductInfo();
            BeanUtils.copyProperties(skuStock, info);

            String skuTitle = productInfo.getName();
            if (!StringUtils.isEmpty(skuStock.getSp1())) {
                skuTitle += " " + skuStock.getSp1();
                if (!StringUtils.isEmpty(skuStock.getSp2())) {
                    skuTitle += " " + skuStock.getSp2();
                    if (!StringUtils.isEmpty(skuStock.getSp3())) {
                        skuTitle += " " + skuStock.getSp3();
                    }
                }
            }
            info.setSkuTitle(skuTitle);

            List<EsProductAttributeValue> attributeValues = new ArrayList<>();
            String[] skuValues = new String[3];
            skuValues[0] = skuStock.getSp1();
            skuValues[1] = skuStock.getSp2();
            skuValues[2] = skuStock.getSp3();
            for (int i = 0; i < skuAttributeNames.size(); i++) {
                EsProductAttributeValue esSkuValue = new EsProductAttributeValue();
                esSkuValue.setName(skuAttributeNames.get(i).getName());
                esSkuValue.setProductId(id);
                esSkuValue.setProductAttributeId(skuAttributeNames.get(i).getProductAttributeId());
                esSkuValue.setValue(skuValues[i]);
                esSkuValue.setType(0);
                attributeValues.add(esSkuValue);
            }
            info.setAttributeValues(attributeValues);


            esSkuProductInfoList.add(info);
        }
        //至此得到一个完整的esProduct对象
        esProduct.setSkuProductInfos(esSkuProductInfoList);
        //保存入es
        Index index = new Index.Builder(esProduct).index(EsConstant.PRODUCT_ES_INDEX)
                .type(EsConstant.PRODUCT_ES_TYPE)
                .id(id.toString())
                .build();
        try {
            DocumentResult execute = jestClient.execute(index);
            if (execute.isSucceeded()) {
                log.debug("Es上架成功，商品的id是：{}", id);
            } else {
                log.error("Es上架失败，商品的id是：{}", id);
            }
        } catch (IOException e) {
            log.error("Es上架出现错误，商品id是：{}，错误信息是：{}", id, e.getMessage());
            e.printStackTrace();
        }
    }

    public void setProductPublishStatus(Integer publishStatus, Long id) {
        Product product = new Product();
        product.setId(id);
        product.setPublishStatus(publishStatus);
        productMapper.updateById(product);
    }

    @Override
    public Product productInfoById(Long id) {
        Product product = productMapper.selectById(id);
        return product;
    }

    @Override
    public EsProduct productAllInfo(Long id) {
        //按照id查询商品
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.termQuery("id", id));


        Search search = new Search.Builder(builder.toString())
                .addIndex(EsConstant.PRODUCT_ES_INDEX)
                .addType(EsConstant.PRODUCT_ES_TYPE)
                .build();
        EsProduct esProduct = null;
        SearchResult execute = null;
        try {
            execute = jestClient.execute(search);
            List<SearchResult.Hit<EsProduct, Void>> hits = execute.getHits(EsProduct.class);
            esProduct = hits.get(0).source;
        } catch (IOException e) {
            log.error("出现了某些异常");
        }

        return esProduct;
    }

    @Override
    public EsProduct productSkuInfo(Long id) {
        //按照id查询商品
        EsProduct esProduct = null;
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.nestedQuery("skuProductInfos",
                QueryBuilders.termQuery("id", id), ScoreMode.None));


        Search search = new Search.Builder(builder.toString())
                .addIndex(EsConstant.PRODUCT_ES_INDEX)
                .addType(EsConstant.PRODUCT_ES_TYPE)
                .build();

        SearchResult execute = null;
        try {
            execute = jestClient.execute(search);
            List<SearchResult.Hit<EsProduct, Void>> hits = execute.getHits(EsProduct.class);
            esProduct = hits.get(0).source;
        } catch (IOException e) {
            log.error("出现了某些异常");
        }

        return esProduct;
    }


    /**
     * pms_sku_stock sku的库存表
     *
     * @param productParam
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSkuStock(PmsProductParam productParam) {
        List<SkuStock> skuStockList = productParam.getSkuStockList();
        for (int i = 0; i < skuStockList.size(); i++) {
            SkuStock skuStock = skuStockList.get(i);
            if (StringUtils.isEmpty(skuStock.getSkuCode())) {
                skuStock.setSkuCode(threadLocal.get() + "_" + i);
            }

            skuStock.setProductId(threadLocal.get());
            skuStockMapper.insert(skuStock);
        }
    }

    /**
     * pms_product_ladder 阶梯价格
     *
     * @param productParam
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLadderPrice(PmsProductParam productParam) {
        List<ProductLadder> ladderList = productParam.getProductLadderList();
        for (ProductLadder ladderPrice : ladderList) {
            ladderPrice.setProductId(threadLocal.get());
            productLadderMapper.insert(ladderPrice);
        }
//        int a = 1 / 0;
    }

    /**
     * pms_product_full_reduction 保存商品的满减信息
     *
     * @param productParam
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFullReduction(PmsProductParam productParam) {
        List<ProductFullReduction> productFullReductionList = productParam.getProductFullReductionList();
        for (ProductFullReduction reduction : productFullReductionList) {
            reduction.setProductId(threadLocal.get());
            productFullReductionMapper.insert(reduction);
        }
    }

    /**
     * pms_product_attribute_value 保存这个商品的所有属性值
     *
     * @param productParam
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAttributeValue(PmsProductParam productParam) {
        List<ProductAttributeValue> attributeValueList = productParam.getProductAttributeValueList();
        for (ProductAttributeValue attributeValue : attributeValueList) {
            attributeValue.setProductId(threadLocal.get());
            productAttributeValueMapper.insert(attributeValue);
        }
    }

    /**
     * pms_product 保存商品的基本信息
     *
     * @param productParam
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveBaseInfo(PmsProductParam productParam) {
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        productMapper.insert(product);
        //mybatisplus可以获取到刚才商品的自增id
        log.debug("刚才储存的商品的id是：{}", product.getId());
        threadLocal.set(product.getId());
    }
}

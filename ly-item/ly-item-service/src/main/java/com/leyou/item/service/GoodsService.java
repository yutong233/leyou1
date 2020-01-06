package com.leyou.item.service;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.common.dto.CartDTO;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.*;
import com.leyou.item.vo.SpuVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<SpuVO> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        //分页
        PageHelper.startPage(page,rows);
        //过滤
        Example example = new Example(Spu.class); //这里是要去查数据库，所以不能用SpuVO
        Example.Criteria criteria = example.createCriteria();
        /*
        select * from tb_spu where title like ? and saleable =? order by last_update_time DESC
         */
        //按搜索字段过滤
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        //上下架过滤
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        //默认排序 按最后修改时间降序
        example.setOrderByClause("last_update_time DESC");
        //查询
        List<Spu> spus = spuMapper.selectByExample(example);
        //判断是否为空
        if (CollectionUtils.isEmpty(spus)) {
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        //进行转换，将vo转换为po
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        ArrayList<SpuVO> spuVOlist = new ArrayList<>();
        for (Spu spu : spus) {
            spuVOlist.add(mapper.map(spu, SpuVO.class));
        }
        //解析分类和品牌的名称
        loadCategoryAndBrandName(spuVOlist);

        //解析查询结果，只能将spu封装到pageinfo，因为你是用查询的spu表，而不是spuVO
        PageInfo<Spu> info = new PageInfo<>(spus);
        return new PageResult<>(info.getTotal(),spuVOlist);
    }

    private void loadCategoryAndBrandName(List<SpuVO> spuVO) {
        for (SpuVO spu : spuVO) {
            //处理分类名称
            //将数组转化为集合
            List<String> names = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());//map(category -> category.getName())
            /*
            List<Category> list = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            Stream<String> stringStream = list.stream().map(Category::getName);
            List<String> names = stringStream.collect(Collectors.toList());
             */

            spu.setCname(StringUtils.join(names, "/"));
            //处理品牌名称
            spu.setBname(brandService.queryById(spu.getBrandId()).getName());
        }
        /*
        1、Arrays.asList将数组转成集合，但是转成集合后不能添加数据了
        2、Collection.stream().map().collect();
            (1)java.util.Collection 接口中加入了default方法 stream 用来获取流，所以其所有实现类均可获取流
            (2)需要将流中的元素映射到另一个流中，可以使用 map 方法。
            (3)其中的双冒号 :: 写法，这被称为“方法引用”，而双冒号是一种新的语法。
        3、Category::getName  Category是类，存在的； getName是方法也是存在的所以可以使用方法引用
        4、Java8新特性Stream之Collectors(toList()、toSet()、toCollection()、joining()、partitioningBy()、collectingAndT)
            stream().collect(Collectors.toList())将数据收集进一个列表(Stream 转换为 List，允许重复值，有顺序)
        5、StringUtils.join(names, "/") 将names和/拼接起来 = names/names/names
         */
    }

    @Transactional
    public void saveGoods(SpuVO spuVO) {
        //改四张表spu -> spuDetail -> sku -> stock
        //spu
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        Spu spu = mapper.map(spuVO, Spu.class);
        spu.setId(null);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);
        spu.setValid(true);
        int count = spuMapper.insertSelective(spu);
        if (count != 1) {
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERRO);
        }
        //spuDetail
        SpuDetail spuDetail = spuVO.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insertSelective(spuDetail);

        //sku
        List<Sku> skus = spuVO.getSkus();
        ArrayList<Stock> stocklist = new ArrayList<>();
        for (Sku sku : skus) {
            sku.setId(null);
            sku.setSpuId(spu.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            count = skuMapper.insert(sku);
            if (count != 1) {
                throw new LyException(ExceptionEnum.GOODS_SAVE_ERRO);
            }
            //stock
            Stock stock = new Stock();
            stock.setSkuId(spu.getId());
            stock.setStock(sku.getStock());
            stocklist.add(stock);
            /* 这里进行优化 改为批量新增
            count = stockMapper.insertSelective(stock);
            if (count != 1) {
                throw new LyException(ExceptionEnum.GOODS_SAVE_ERRO);
            }
            */
        }
        count = stockMapper.insertList(stocklist);
        if (count != 1) {
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERRO);
        }

        //发送mq消息
        amqpTemplate.convertAndSend("item.insert", spuVO.getId());

    }

    public SpuDetail querySpuDetailById(Long spuid) {
        return spuDetailMapper.selectByPrimaryKey(spuid);
    }

    public List<Sku> querySkuBySpuId(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> record = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(record)) {
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        record.forEach(sku1 -> {
            Stock stock = stockMapper.selectByPrimaryKey(sku1.getId());
            sku1.setStock(stock.getStock());
        });
        return record;
    }

    @Transactional
    public void updateGoods(SpuVO spuVO) {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        Spu spu = mapper.map(spuVO, Spu.class);

        Sku sku1 = new Sku();
        sku1.setSpuId(spuVO.getId());
        List<Sku> skuList = skuMapper.select(sku1);
        skuList.forEach(sku -> {
            //先删除stock
            stockMapper.deleteByPrimaryKey(sku.getId());
        });
        //然后删除sku
        Sku sku2 = new Sku();
        sku2.setSpuId(spuVO.getId());
        skuMapper.delete(sku2);
        //新增sku,stock
        //sku
        List<Sku> skus = spuVO.getSkus();
        List<Stock> stocklist = new ArrayList<>();
        for (Sku sku : skus) {
            sku.setId(null);
            sku.setSpuId(spu.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            int count = skuMapper.insert(sku);
            if (count != 1) {
                throw new LyException(ExceptionEnum.GOODS_SAVE_ERRO);
            }
            //stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stocklist.add(stock);
            /* 这里进行优化 改为批量新增
            count = stockMapper.insertSelective(stock);
            if (count != 1) {
                throw new LyException(ExceptionEnum.GOODS_SAVE_ERRO);
            }
            */
        }
        stockMapper.insertList(stocklist);

        // 更新 spu和spuDetail
        spuMapper.updateByPrimaryKey(spu);
        spuDetailMapper.updateByPrimaryKey(spuVO.getSpuDetail());

        //发送mq消息
        amqpTemplate.convertAndSend("item.update", spuVO.getId());

    }

    @Transactional
    public void deleteGoods(Long spuId) {
        Sku sku1 = new Sku();
        sku1.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku1);
        skuList.forEach(sku -> {
            //先删除stock
            stockMapper.deleteByPrimaryKey(sku.getId());
        });
        //然后删除sku
        Sku sku2 = new Sku();
        sku2.setSpuId(spuId);
        skuMapper.delete(sku2);
        //删除spuDetail
        spuDetailMapper.deleteByPrimaryKey(spuId);
        //删除spu
        spuMapper.deleteByPrimaryKey(spuId);

    }

    @Transactional
    public void updateSealable(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        Boolean saleable = spu.getSaleable();
        spu.setSaleable(!saleable);
        spuMapper.updateByPrimaryKey(spu);
    }


    public SpuVO querySpuById(Long id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //spu转换为spuVo
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        SpuVO spuVO = mapper.map(spu, SpuVO.class);
        if (spu == null) {
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        //查询sku
        spuVO.setSkus(querySkuBySpuId(id));
        //查询detail
        spuVO.setSpuDetail(querySpuDetailById(id));
        return spuVO;
    }

    public List<Sku> querySkuByIds(List<Long> ids) {
        List<Sku> skus = skuMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(skus)){
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        //查询库存
        List<Stock> stockList = stockMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(stockList)){
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        //把stock变成一个map，其key是sku的ID，值是库存值
        Map<Long, Integer> stockmap = stockList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skus.forEach(sku -> sku.setStock(stockmap.get(sku.getId())));
        return skus;
    }

    @Transactional
    public void decreaseStock(List<CartDTO> carts) {
        for (CartDTO cart : carts) {
            //减库存
            int count = stockMapper.decreaseStock(cart.getSkuId(), cart.getNum());
            if (count != 1) {
                throw new LyException(ExceptionEnum.STOCK_NOT_ENOUGH);
            }
        }
    }
}

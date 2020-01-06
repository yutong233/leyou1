package com.leyou;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.leyou.item.pojo.Spu;
import com.leyou.item.vo.SpuVO;
import org.junit.Test;

import java.util.Date;

public class Dozertest {

    @Test
    public void test1() {
        Spu spu = new Spu();
        spu.setId(1l);
        spu.setTitle("ceshi");
        spu.setCreateTime(new Date());
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        SpuVO spuVO = mapper.map(spu, SpuVO.class);
        System.out.println(spuVO);


    }
}

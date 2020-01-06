package com.leyou.item.mapper;

import com.leyou.item.pojo.Sku;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface SkuMapper extends Mapper<Sku>, IdListMapper<Sku,Long>,InsertListMapper<Sku>,DeleteByIdListMapper<Sku,Long> {

}

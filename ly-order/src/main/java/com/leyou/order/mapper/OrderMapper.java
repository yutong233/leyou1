package com.leyou.order.mapper;


import com.leyou.order.pojo.Order;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface OrderMapper extends Mapper<Order>, IdListMapper<Order,Long> {


}

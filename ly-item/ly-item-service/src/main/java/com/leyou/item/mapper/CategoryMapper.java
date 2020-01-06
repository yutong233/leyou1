package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category,Long> {
    //IdListMapper<Category,Long> ==> IdListMapper<T, PK>  T是实体类  PK 主键的类型

    /**
     * 根据品牌ID查询出类别信息
     * @param bid
     * @return
     */
    @Select("select * from tb_category where id in (select category_id from tb_category_brand where brand_id = #{bid})")
    List<Category> queryCategoryByBrandId(Long bid);

}

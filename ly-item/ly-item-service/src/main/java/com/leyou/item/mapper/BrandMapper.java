package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand>, IdListMapper<Brand, Long> {

    /**
     * 保存 tb_category_brand表中的数据，因为没有对应实体类
     * @param cid
     * @param bid
     * @return
     */
    @Insert("insert into tb_category_brand (category_id, brand_id) values (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid")Long bid);

    /**
     * 删除中间表tb_category_brand的数据
     * @param bid
     * @return
     */
    @Delete("delete from tb_category_brand where brand_id = #{bid}")
    int deleteCategoryBrand(@Param("bid")Long bid);

    @Select("select b.id,b.name from tb_category_brand inner join tb_brand b on brand_id = b.id where category_id = #{cid}")
    List<Brand> queryCategoryByCid(@Param("cid")Long cid);


}

package com.leyou.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_spu_detail")
public class SpuDetail {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long spuId;
    private String description;
    private String genericSpec; //存放的是公用的参数属性 如品牌，尺寸
    private String specialSpec; //存放的是特有的属性及可选值模板，如：颜色，内存
    private String packingList;
    private String afterService;


}

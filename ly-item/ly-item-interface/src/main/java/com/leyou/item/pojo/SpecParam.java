package com.leyou.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_spec_param")
public class SpecParam {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long cid;
    private Long groupId;
    private String name;
    @Column(name = "`numeric`") //查询的时候以  `numeric` 此查
    private Boolean numeric; //是否是数字类型的参数
    private String unit;    //数字类型的单位
    private Boolean generic; //是否是通用属性
    private Boolean searching;
    private String segments;

}

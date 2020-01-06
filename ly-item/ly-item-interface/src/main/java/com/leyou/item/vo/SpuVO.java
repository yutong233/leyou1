package com.leyou.item.vo;

import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SpuVO{

    private Long id;
    private Long brandId;
    private Long cid1;// 1级类目
    private Long cid2;// 2级类目
    private Long cid3;// 3级类目
    private String title;// 标题
    private String subTitle;// 子标题
    private Boolean saleable;// 是否上架
    private Boolean valid;// 是否有效，逻辑删除用
    private Date createTime;// 创建时间
    private Date lastUpdateTime;// 最后修改时间

    private String cname; //类别名字
    private String bname; //品牌名字

    private SpuDetail spuDetail;
    private List<Sku> skus;
}

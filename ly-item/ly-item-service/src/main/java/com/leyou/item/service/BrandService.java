package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

   /* @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;*/

    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page,rows);
        /**
         * select * from tb_brand
         * where name like "%X%" or letter == "X"
         * order by id DESC
         */
        //过滤 key
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            //过滤条件
            example.createCriteria().orLike("name", "%"+key+"%")
                    .orEqualTo("letter",key.toUpperCase());
            //where name like "%X%" or letter == "X"
        }
        //排序
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
            // order by id DESC
        }
        //查询
        List<Brand> list = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //解析分页结果
        PageInfo<Brand> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),list);
    }

    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增品牌
        brand.setId(null);
        //返回1 即表示成功
        int count = brandMapper.insert(brand);
        if (count != 1) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //新增中间表
        for (Long cid : cids) {
            //brand.getId()是可以回写的
            count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if (count != 1) {
                throw new LyException(ExceptionEnum.BRAND_SAVE_ERRO);
            }
        }
    }

    @Transactional
    public void updateBrand(Brand brand, List<Long> cids) {
        //更新品牌
        int count = brandMapper.updateByPrimaryKey(brand);
        if (count != 1) {
            throw new LyException(ExceptionEnum.BRAND_UPDATE_ERRO);
        }
        //删除旧的品牌类别表
        count = brandMapper.deleteCategoryBrand(brand.getId());
        if (count != 1) {
            throw new LyException(ExceptionEnum.BRAND_UPDATE_ERRO);
        }
        //新增中间表
        for (Long cid : cids) {
            //brand.getId()是可以回写的
            count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if (count != 1) {
                throw new LyException(ExceptionEnum.BRAND_UPDATE_ERRO);
            }
        }
    }

    @Transactional
    public void deleteBrand(Long bid) {

   /*     //删除fastDFS上的相关图片
        List<ServiceInstance> uploadservice = discoveryClient.getInstances("upload-service");
        ServiceInstance instance = uploadservice.get(0);
        String url = "http://"+ instance.getHost() + ":" + instance.getPort() + "/upload/" + ;
        restTemplate.delete(url);
*/
        //删除数据库品牌
        int count = brandMapper.deleteByPrimaryKey(bid);
        if (count != 1) {
            throw new LyException(ExceptionEnum.BRAND_UPDATE_ERRO);
        }
        //删除品牌相关的中间表tb_Category_Brand
        count = brandMapper.deleteCategoryBrand(bid);
        if (count != 1) {
            throw new LyException(ExceptionEnum.BRAND_UPDATE_ERRO);
        }

    }

    public Brand queryById(Long id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand == null) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    public List<Brand> queryCategoryByCid(Long cid) {
        List<Brand> list = brandMapper.queryCategoryByCid(cid);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return list;
    }

    public List<Brand> queryBrandByIds(List<Long> ids) {
        List<Brand> list = brandMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return list;
    }
}

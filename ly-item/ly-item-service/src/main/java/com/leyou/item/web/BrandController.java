package com.leyou.item.web;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 分页查询品牌
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key
    ) {
        //http://api.leyou.com/api/item/brand/page?key=&page=1&rows=5&sortBy=id&desc=false GET key 为搜索关键词
        return ResponseEntity.ok(brandService.queryBrandByPage(page,rows,sortBy,desc,key));
    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids")List<Long> cids) {
        //http://api.leyou.com/api/item/brand  POST
        //name: 阿迪达斯  image:  cids: 类别ID   letter: A
        brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();//如果没有返回对象就用build，否则用body
    }

    /**
     * 修改品牌
     * @param brand
     * @param cids
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids")List<Long> cids) {
            //http://api.leyou.com/api/item/brand  PUT
        // id: 325404  name: zhangsan  image:  cids: 230,3  letter: Z
        brandService.updateBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据品牌ID删除品牌
     * @param bid
     * @return
     */
    @DeleteMapping("bid/{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid") Long bid) {
        //http://api.leyou.com/api/item/brand/bid/325404  DELETE
        brandService.deleteBrand(bid);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * 根据分类ID查询品牌列表
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid") Long cid) {
        //  http://api.leyou.com/api/item/brand/cid/76 GET
        List<Brand> list = brandService.queryCategoryByCid(cid);
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(brandService.queryById(id));
    }

    @GetMapping("list")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(brandService.queryBrandByIds(ids));
    }

}

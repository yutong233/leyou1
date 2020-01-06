package com.leyou.item.web;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据pid查询分类信息
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryListByParent(@RequestParam("pid")Long pid) {
        //http://api.leyou.com/api/item/category/list?pid=0   GET
        return ResponseEntity.ok(categoryService.queryListByParent(pid));
    }

    /**
     * 查询品牌类别，用于修改品牌时类别的回显
     * @param bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryCategoryByBrandId(@PathVariable("bid") Long bid) {
        //http://api.leyou.com/api/item/category/bid/1115  GET
        return ResponseEntity.ok(categoryService.queryCategoryByBrandId(bid));
    }

    /**
     * 根据分类ID查询分类信息
     * @param ids
     * @return
     */
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(categoryService.queryByIds(ids));
    }

}

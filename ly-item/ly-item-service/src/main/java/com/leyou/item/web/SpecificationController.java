package com.leyou.item.web;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据分类ID查询规格组信息
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid") Long cid) {
        //http://api.leyou.com/api/item/spec/groups/76  GET
        List<SpecGroup> list = specificationService.queryGroupByCid(cid);
        return ResponseEntity.ok(list);
    }

    /**
     * 新增商品规格组
     * @param specGroup
     * @return
     */
    @PostMapping("group")
    public ResponseEntity<Void> saveSpeicGroup(@RequestBody SpecGroup specGroup) {
        //http://api.leyou.com/api/item/spec/group  POST  cid: 76  name: "测试"
        specificationService.saveSpeicGroup(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改规格组信息
     * @param specGroup
     * @return
     */
    @PutMapping("group")
    public ResponseEntity<Void> updateSpeicGroup(@RequestBody SpecGroup specGroup) {
        //http://api.leyou.com/api/item/spec/group PUT  {"cid":76,"name":"主体","id":1}
        specificationService.updateSpeicGroup(specGroup);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * 删除规格组
     * @param sid
     * @return
     */
    @DeleteMapping("group/{sid}")
    public ResponseEntity<Void> deleteSpeicGroup(@PathVariable("sid") Long sid) {
        //http://api.leyou.com/api/item/spec/group/1  DELETE
        specificationService.deleteSpeicGroup(sid);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    //* * * * * * * * 规格组参数 * * * * * * * *

    /**
     * 查询规格组参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamList(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "generic", required = false) Boolean generic,
            @RequestParam(value = "searching", required = false) Boolean searching
    ) {
        // http://api.leyou.com/api/item/spec/params?gid=14   GET
        // http://api.leyou.com/api/item/spec/params?cid=76   GET
        List<SpecParam> list = specificationService.queryParamList(gid,cid,generic,searching);
        return ResponseEntity.ok(list);
    }

    /**
     * 新增规格组参数
     * @param specParam
     * @return
     */
    @PostMapping("param")
    public ResponseEntity<Void> saveSpecParam(@RequestBody SpecParam specParam) {
        //http://api.leyou.com/api/item/spec/param POST
        //{"cid":76,"groupId":2,"segments":"1-2,3-4","numeric":true,"searching":true,"generic":true,"name":"测试"}
        specificationService.saveSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * 修改规格组参数
     * @param specParam
     * @return
     */
    @PutMapping("param")
    public ResponseEntity<Void> updateSpecParam(@RequestBody SpecParam specParam) {
        //http://api.leyou.com/api/item/spec/param PUT
        specificationService.updateSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * 删除规格组参数
     * @param sid
     * @return
     */
    @DeleteMapping("param/{sid}")
    public ResponseEntity<Void> deleteSpecParam(@PathVariable("sid") Long sid) {
        //http://api.leyou.com/api/item/spec/param/24  DELETE
        specificationService.deleteSpecParam(sid);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * 根据分类查询规格组及组内参数
     * @param cid
     * @return
     */
    @GetMapping("group")
    public ResponseEntity<List<SpecGroup>> queryListByCid(@RequestParam("cid") Long cid){
        return ResponseEntity.ok(specificationService.queryListByCid(cid));
    }



}

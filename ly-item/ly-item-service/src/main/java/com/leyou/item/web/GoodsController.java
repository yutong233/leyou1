package com.leyou.item.web;

import com.leyou.common.vo.PageResult;
import com.leyou.common.dto.CartDTO;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import com.leyou.item.vo.SpuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  //可以不用加@ResponseBody
@RequestMapping
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询Spu
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuVO>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key
    ) {
        //http://api.leyou.com/api/item/spu/page?key=&saleable=true&page=1&rows=5 GET
        PageResult<SpuVO> result = goodsService.querySpuByPage(page,rows,saleable,key);
        return ResponseEntity.ok(result);
    }

    /**
     * 保存商品信息
     * @param spuVO
     * @return
     */
    @PostMapping("/goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuVO spuVO) {
        // http://api.leyou.com/api/item/goods POST
        goodsService.saveGoods(spuVO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改商品信息时，spuDetail数据的回显
     * @param spuid
     * @return
     */
    @GetMapping("/spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("spuId") Long spuid) {
        //http://api.leyou.com/api/item/spu/detail/206 GET
        SpuDetail spuDetail = goodsService.querySpuDetailById(spuid);
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 修改商品信息时，sku数据的回显
     * @param id
     * @return
     */
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id")Long id) {
        // http://api.leyou.com/api/item/sku/list?id=206 GET
        List<Sku> list = goodsService.querySkuBySpuId(id);
        return ResponseEntity.ok(list);
    }

    /**
     * 更新商品
     * @param spuVO
     * @return
     */
    @PutMapping("/goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuVO spuVO) {
        //http://api.leyou.com/api/item/goods PUT
        goodsService.updateGoods(spuVO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * 删除商品
     * @param spuId
     * @return
     */
    @DeleteMapping("/goods/spu/{spuId}")
    public ResponseEntity<Void> deleteGoods(@PathVariable Long spuId) {
        //http://api.leyou.com/api/item/goods/spu/206 DELETE
        goodsService.deleteGoods(spuId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * 商品的上下架
     * @param spuId
     * @return
     */
    @PutMapping("/goods/spu/out/{spuId}")
    public ResponseEntity<Void> updateSealable(@PathVariable Long spuId) {
        //http://api.leyou.com/api/item/goods/spu/out/194 PUT
        goodsService.updateSealable(spuId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("spu/{id}")
    public ResponseEntity<SpuVO> querySpuById(@PathVariable("id") Long id){
        return ResponseEntity.ok(goodsService.querySpuById(id));
    }

    /**
     *根据sku的id集合查询所有sku
     * @param ids
     * @return
     */
    @GetMapping("sku/list/ids")
    public ResponseEntity<List<Sku>> querySkuByIds(@RequestParam("ids")List<Long> ids){
        return ResponseEntity.ok(goodsService.querySkuByIds(ids));
    }

    /**
     * 订单减库存
     * @param carts
     * @return
     */
    @PostMapping("stock/decrease")
    public ResponseEntity<Void> decreaseStock(@RequestBody List<CartDTO> carts) {
        goodsService.decreaseStock(carts);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}

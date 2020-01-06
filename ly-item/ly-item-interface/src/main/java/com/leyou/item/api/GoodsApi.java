package com.leyou.item.api;

import com.leyou.common.vo.PageResult;
import com.leyou.common.dto.CartDTO;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.vo.SpuVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GoodsApi {

    @GetMapping("spu/detail/{spuId}")
    SpuDetail querySpuDetailById(@PathVariable("spuId") Long spuid);

    @GetMapping("sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id")Long id);

    @GetMapping("spu/page")
    PageResult<SpuVO> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key
    );

    @GetMapping("spu/{id}")
    SpuVO querySpuById(@PathVariable("id") Long id);

    /**
     * 根据ID批量查询sku
     * @param ids
     * @return
     */
    @GetMapping("sku/list/ids")
    List<Sku> querySkuByIds(@RequestParam("ids") List<Long> ids);

    @PostMapping("stock/decrease")
    void decreaseStock(@RequestBody List<CartDTO> carts);


}

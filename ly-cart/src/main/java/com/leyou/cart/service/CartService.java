package com.leyou.cart.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.common.utils.JsonUtils;
import com.leyou.cart.intercepter.UserInterceptor;
import com.leyou.cart.pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "cart:uid:";

    public void addCart(Cart cart) {
        //获取登录用户
        UserInfo user = UserInterceptor.getUser();
        //key
        String key = KEY_PREFIX + user.getId();
        //hashKey
        String hashKey = cart.getSkuId().toString();
        //记录num
        Integer num = cart.getNum();
        //获取登录用户的所有购物车
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        //判断当前购物车商品是否存在
        if (operation.hasKey(hashKey)) {
            //存在，修改数量
            String json = operation.get(hashKey).toString(); //得到的一定是个json格式的数据
            cart = JsonUtils.parse(json, Cart.class);
            cart.setNum(cart.getNum() + num);
            //若redis中存在该商品，覆盖当前的cart,前面已经记录下来了redis中的数量
        }
        //buguan写回redis,前面已经绑定了key,所以后面直接存map就行了
        operation.put(hashKey,JsonUtils.serialize(cart));

    }

    public List<Cart> queryCartList() {
        //获取登录用户
        UserInfo user = UserInterceptor.getUser();
        //key
        String key = KEY_PREFIX + user.getId();
        if (!redisTemplate.hasKey(key)) {
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }
        //获取登录用户的所有购物车
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        Stream<Cart> cartStream = operation.values().stream().map(o -> JsonUtils.parse(o.toString(), Cart.class));
        List<Cart> carts = cartStream.collect(Collectors.toList());
        return carts;

    }

    public void updateNum(Long skuId, Integer num) {
        //获取登录用户
        UserInfo user = UserInterceptor.getUser();
        //key
        String key = KEY_PREFIX + user.getId();
        //hashkey
        String hashKey = skuId.toString();
        //获取登录用户的所有购物车
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);

        //判断是否存在
        if (!operation.hasKey(hashKey)) {
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }

        //查询
        Object json = operation.get(hashKey);
        Cart cart = JsonUtils.parse(json.toString(), Cart.class);
        cart.setNum(num);
        //写回redis
        operation.put(hashKey,JsonUtils.serialize(cart));
    }


    public void deleteCart(Long skuId) {
        //获取登录用户
        UserInfo user = UserInterceptor.getUser();
        //key
        String key = KEY_PREFIX + user.getId();
        //删除
        redisTemplate.opsForHash().delete(key, skuId.toString());

    }
}

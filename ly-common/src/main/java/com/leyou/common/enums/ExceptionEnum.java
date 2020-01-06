package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ExceptionEnum {

    BRAND_NOT_FOUND(404, "品牌未找到！"),
    BRAND_UPDATE_ERRO(406, "品牌更新失败！"),
    CATEGORY_NOT_FOUND(404,"商品信息未找到！"),
    BRAND_SAVE_ERRO(500,"新增品牌失败！"),
    UPLOAD_FILE_ERRO(500,"文件上传失败！"),
    INVALID_FILE_TYPE(400,"无效文件类型！"),
    SPEC_GROUP_NOT_FOUND(404,"商品规格组不存在！"),
    SPEC_GROUP_SAVE_ERRO(500,"新增商品规格组失败！"),
    SPEC_GROUP_UPDATE_ERRO(406,"修改商品规格组失败！"),
    SPEC_GROUP_DELETE_ERRO(406,"删除商品规格组失败！"),
    SPEC_PARAM_NOT_FOUND(404,"商品规格参数不存在！"),
    SPEC_PARAM_SAVE_ERRO(500,"新增商品规格参数失败！"),
    SPEC_PARAM_UPDATE_ERRO(406,"修改商品规格参数失败！"),
    SPEC_PARAM_DELETE_ERRO(406,"删除商品规格组失败！"),
    GOODS_NOT_FOUND(404,"商品规格参数不存在！"),
    GOODS_SAVE_ERRO(500,"新增商品失败！"),
    INVALID_USER_DATA_TYPE(400,"用户数据类型无效！"),
    INVALID_VERIFY_CODE(400,"无效验证码！"),
    INVALID_USERNAME_PASSWORD(400,"用户名或密码错误！"),
    CREATE_TOKEN_ERROR(500,"用户凭证生成失败！"),
    UNAUTHORIZED(403, "未授权"),
    CONFIG_ERROR(500,"配置文件加载失败！"),
    CART_NOT_FOUND(404,"购物车为空！"),
    CREATE_ORDER_ERROR(500,"配置文件加载失败！"),
    STOCK_NOT_ENOUGH(500,"库存不足！"),
    ORDER_NOT_FOUND(404,"订单不存在！"),
    ORDER_DETAIL_NOT_FOUND(404,"订单详情不存在！"),
    ORDER_STATUS_NOT_FOUND(404,"订单状态不存在！"),
    WX_PAY_ORDER_FAIL(500,"微信下单失败！"),
    ORDER_STATUS_ERROR(400, "订单状态异常"),
    INVALID_SIGN_ERRO(400,"无效的签名异常！"),
    INVALID_ORDER_PARAM(400,"订单参数异常！"),
    UPLOAD_ORDER_STATUS_ERRO(500,"更新订单状态失败！"),
    ;
    private int code; //顺序不能颠倒，这个和400， 价格空一致
    private String msg;




}

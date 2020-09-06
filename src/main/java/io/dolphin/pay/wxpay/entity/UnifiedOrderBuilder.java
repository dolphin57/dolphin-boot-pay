package io.dolphin.pay.wxpay.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 构造统一下单
 * @Author: qianliang
 * @Since: 2019-10-10 10:21
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UnifiedOrderBuilder extends BaseBuilder {
    /**
     * 公众账号id: 必传
     */
    @NotBlank(message = "公众账号id不能为空")
    private String appid;

    /**
     * 商户号: 必传
     */
    private String mch_id;

    /**
     * 子账号id
     */
    private String sub_appid;

    /**
     * 子商户号
     */
    private String sub_mch_id;

    /**
     * 设备号
     */
    private String device_info;

    /**
     * 随机字符串,保证签名不可预测: 必传
     */
    private String nonce_str;

    /**
     * 签名: 必传
     */
    private String sign;

    /**
     * 签名类型
     */
    private String sign_type;

    /**
     * 商品描述: 必传
     */
    private String body;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * 商户订单号: 必传
     */
    private String out_trade_no;

    /**
     * 标价币种
     */
    private String fee_type;

    /**
     * 标价金额: 必传
     */
    private String total_fee;

    /**
     * 终端IP: 必传
     */
    private String spbill_create_ip;

    /**
     * 交易起始时间
     */
    private String time_start;

    /**
     * 交易结束时间
     */
    private String time_expire;

    /**
     * 订单优惠标记
     */
    private String goods_tag;

    /**
     * 通知地址: 必传
     */
    private String notify_url;

    /**
     * 交易类型: 必传
     */
    private String trade_type;

    /**
     * 商品ID
     */
    private String product_id;

    /**
     * 指定支付方式
     */
    private String limit_pay;

    /**
     * 用户标识
     */
    private String openid;

    /**
     * 子用户标识
     */
    private String sub_openid;

    /**
     * 电子发票入口开放标识
     */
    private String receipt;

    /**
     * 场景信息
     */
    private SceneInfo scene_info;
}

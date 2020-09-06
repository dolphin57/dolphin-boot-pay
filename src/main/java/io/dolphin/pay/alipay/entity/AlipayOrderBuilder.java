package io.dolphin.pay.alipay.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Description: 构建支付宝的统一下单
 * @Author: qianliang
 * @Since: 2019-10-13 22:25
 */
@Data
@Builder
@AllArgsConstructor
public class AlipayOrderBuilder {
    /**
     * 一笔交易的具体描述信息
     */
    private String body;

    /**
     * 商品的标题
     */
    private String subject;

    /**
     * 商户网站唯一订单号
     */
    private String outTradeNo;

    /**
     * 订单总金额，单位为元
     */
    private String totalAmount;

    /**
     * 最晚付款时间，逾期将关闭交易
     */
    private String timeoutExpress;

    /**
     * 销售产品码，商家和支付宝签约的产品码，为固定值 QUICK_MSECURITY_PAY
     */
    private String productCode;

    /**
     * 公用回传参数，如果请求时传递了该参数，则返回给商户时会回传该参数
     */
    private String passbackParams;

    /**
     * 支付宝服务器主动通知商户服务器里指定的页面 http/https 路径
     */
    private String notifyUrl;

    /**
     * 支付宝服务器主动通知商户服务器里指定的页面 http/https 路径
     */
    private String returnUrl;
}

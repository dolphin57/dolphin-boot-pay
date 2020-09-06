package io.dolphin.pay.wxpay.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Description: 构建退款查询
 * @Author: qianliang
 * @Since: 2019-10-12 14:28
 */
@Data
@Builder
@AllArgsConstructor
public class RefundQueryBuilder extends BaseBuilder {
    /**
     * 应用id: 必传
     */
    private String appid;

    /**
     * 商户号: 必传
     */
    private String mch_id;

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
     * 微信订单号: 以下四选一
     */
    private String transaction_id;

    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 商户退款单号
     */
    private String out_refund_no;

    /**
     * 微信退款单号
     */
    private String refund_id;

    /**
     * 偏移量,用作分页
     * 当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录
     */
    private String offset;
}

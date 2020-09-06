package io.dolphin.pay.wxpay.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 构建退款订单
 * @Author: qianliang
 * @Since: 2019-10-11 16:30
 */
@Data
@Builder
@AllArgsConstructor
public class RefundOrderBuilder extends BaseBuilder {
    /**
     * 应用id: 必传
     */
    @NotBlank(message = "公众账号id不能为空")
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
     * 商户订单号: 必传
     */
    private String out_trade_no;

    /**
     * 商户退款单号
     */
    private String out_refund_no;

    /**
     * 订单金额: 必传
     */
    private String total_fee;

    /**
     * 退款金额: 必传
     */
    private String refund_fee;
}

package io.dolphin.pay.wxpay.config;

/**
 * @Description: 微信支付配置
 * @Author: qianliang
 * @Since: 2019-10-9 19:01
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = WxPayProperties.PREFIX)
public class WxPayProperties {
    public static final String PREFIX = "pay.wxpay";

    /**
     * 商户的appid
     */
    private String appid;

    /**
     * appid对应的接口密码
     */
    private String appSecret;

    /**
     * 商户id
     */
    private String mchid;

    /**
     * 证书环境的密钥
     */
    private String partnerKey;

    /**
     * API请求携带证书的绝对地址
     */
    private String certPath;

    /**
     * 是否启用沙箱功能
     */
    private boolean useSandbox;

    /**
     * 沙箱环境的密钥
     */
    private String sandboxKey;

    /**
     * 小程序端的appid
     */
    private String miniAppid;

    /**
     * 小程序端的appSecret
     */
    private String miniAppSecret;

    public String getPartnerKey() {
        if (useSandbox) {
            return sandboxKey;
        }
        return partnerKey;
    }
}

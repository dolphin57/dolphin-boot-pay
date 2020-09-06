package io.dolphin.pay.alipay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Author: qianliang
 * @Since: 2019-10-13 16:09
 */
@Data
@ConfigurationProperties(prefix = AliPayProperties.PREFIX)
public class AliPayProperties {
    public static final String PREFIX = "pay.alipay";

    /**
     * 商户的appId
     */
    private String appId;

    /**
     * 应用私钥
     */
    private String privateKey;

    /**
     * 支付宝公钥
     */
    private String publicKey;

    /**
     * 支付宝支付网关
     */
    private String serverUrl;
}

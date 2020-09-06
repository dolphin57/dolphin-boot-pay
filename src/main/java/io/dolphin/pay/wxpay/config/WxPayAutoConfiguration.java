package io.dolphin.pay.wxpay.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 微信支付自动装配
 * @Author: qianliang
 * @Since: 2019-10-9 19:26
 */
@Configuration
@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayAutoConfiguration {
    @Autowired
    private WxPayProperties wxPayProperties;

}

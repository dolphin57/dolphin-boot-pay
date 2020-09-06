package io.dolphin.pay.alipay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 支付宝配置自动装配
 * @Author: qianliang
 * @Since: 2019-10-13 16:20
 */
@Configuration
@EnableConfigurationProperties(AliPayProperties.class)
public class AliPayAutoConfiguration {

    @Autowired
    private AliPayProperties aliPayProperties;

    @Bean(name = "alipayClient")
    @ConditionalOnMissingBean(name = "alipayClient")
    public AlipayClient clientProducer(AliPayProperties aliPayProperties) {
        return new DefaultAlipayClient(aliPayProperties.getServerUrl(), aliPayProperties.getAppId(),
            aliPayProperties.getPrivateKey(), "JSON", "UTF-8", aliPayProperties.getPublicKey(), "RSA2");
    }
}

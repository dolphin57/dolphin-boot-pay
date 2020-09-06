package io.dolphin.pay.unionpay.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: qianliang
 * @Since: 2019-10-17 11:33
 */
@Configuration
@EnableConfigurationProperties(UnionPayProperties.class)
public class UnionPayAutoConfiguration {
}

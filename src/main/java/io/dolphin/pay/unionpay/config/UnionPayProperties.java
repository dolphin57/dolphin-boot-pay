package io.dolphin.pay.unionpay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Author: qianliang
 * @Since: 2019-10-17 11:31
 */
@Data
@ConfigurationProperties(prefix = UnionPayProperties.PREFIX)
public class UnionPayProperties {
    public static final String PREFIX = "pay.unionpay";

    private boolean ifValidateCNName;

    private boolean ifValidateRemoteCert;

    private String backUrl;

    private String frontUrl;

    private String encryptCertPath;

    private String middleCertPath;

    private String rootCertPath;

    private SignCert signCert;

    private class SignCert {
        private String path;

        private String pwd;

        private String type;
    }
}

package io.dolphin.pay.unionpay.util;

import lombok.extern.slf4j.Slf4j;

import java.security.KeyStore;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: qianliang
 * @Since: 2019-10-17 9:06
 */
@Slf4j
public class CertUtil {
    /** 证书容器，存储对商户请求报文签名私钥证书. */
    private static KeyStore keyStore = null;
    /** 敏感信息加密公钥证书 */
    private static X509Certificate encryptCert = null;
    /** 磁道加密公钥 */
    private static PublicKey encryptTrackKey = null;
    /** 验证银联返回报文签名证书. */
    private static X509Certificate validateCert = null;
    /** 验签中级证书 */
    private static X509Certificate middleCert = null;
    /** 验签根证书 */
    private static X509Certificate rootCert = null;
    /** 验证银联返回报文签名的公钥证书存储Map. */
    private static Map<String, X509Certificate> certMap = new HashMap<String, X509Certificate>();
    /** 商户私钥存储Map */
    private final static Map<String, KeyStore> keyStoreMap = new ConcurrentHashMap<String, KeyStore>();

    static {
        init();
    }

    private static void init() {
        try {
            addProvider();//向系统添加BC provider
            //initSignCert();//初始化签名私钥证书
            //initMiddleCert();//初始化验签证书的中级证书
            //initRootCert();//初始化验签证书的根证书
            //initEncryptCert();//初始化加密公钥
            //initTrackKey();//构建磁道加密公钥
            //initValidateCertFromDir();//初始化所有的验签证书
        } catch (Exception e) {
            log.error("init失败。（如果是用对称密钥签名的可无视此异常。）", e);
        }
    }

    private static void addProvider() {
        if (Security.getProvider("BC") == null) {
            log.info("add BC provider");
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        } else {
            Security.removeProvider("BC"); //解决eclipse调试时tomcat自动重新加载时，BC存在不明原因异常的问题。
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            log.info("re-add BC provider");
        }
        printSysInfo();
    }

    private static void printSysInfo() {
        log.info("================= SYS INFO begin====================");
        log.info("os_name:" + System.getProperty("os.name"));
        log.info("os_arch:" + System.getProperty("os.arch"));
        log.info("os_version:" + System.getProperty("os.version"));
        log.info("java_vm_specification_version:"
                + System.getProperty("java.vm.specification.version"));
        log.info("java_vm_specification_vendor:"
                + System.getProperty("java.vm.specification.vendor"));
        log.info("java_vm_specification_name:"
                + System.getProperty("java.vm.specification.name"));
        log.info("java_vm_version:"
                + System.getProperty("java.vm.version"));
        log.info("java_vm_name:" + System.getProperty("java.vm.name"));
        log.info("java.version:" + System.getProperty("java.version"));
        log.info("java.vm.vendor=[" + System.getProperty("java.vm.vendor") + "]");
        log.info("java.version=[" + System.getProperty("java.version") + "]");
        printProviders();
        log.info("================= SYS INFO end=====================");
    }

    private static void printProviders() {
        log.info("Providers List:");
        Provider[] providers = Security.getProviders();
        for (int i = 0; i < providers.length; i++) {
            log.info(i + 1 + "." + providers[i].getName());
        }
    }
}

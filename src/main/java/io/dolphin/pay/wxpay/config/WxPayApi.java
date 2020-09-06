package io.dolphin.pay.wxpay.config;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.ssl.SSLSocketFactoryBuilder;
import io.dolphin.pay.wxpay.util.WxPayUtil;
import lombok.NoArgsConstructor;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Map;

import static io.dolphin.pay.wxpay.constants.WxPayConstants.*;

/**
 * @Description: 微信端接口封装
 * @Author: qianliang
 * @Since: 2019-10-10 11:07
 */
@NoArgsConstructor
public class WxPayApi {

    /**
     * 封装统一下单接口,兼容沙箱环境
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    统一下单请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String unifiedOrder(boolean isSandbox, Map<String, String> params) {
        if (isSandbox) {
            return doPost(SANDBOX_UNIFIED_ORDER_URL, params);
        }
        return doPost(UNIFIED_ORDER_URL, params);
    }

    /**
     * 微信支付通用查询接口
     * @param isSandbox 是否是沙盒环境
     * @param params 统一下单请求参数
     * @return
     */
    public static String orderQuery(boolean isSandbox, Map<String, String> params) {
        if (isSandbox) {
            return doPost(SANDBOX_ORDER_QUERY_URL, params);
        }
        return doPost(ORDER_QUERY_URL, params);
    }

    /**
     * 申请退款
     * @param isSandbox 是否是沙盒环境
     * @param params 请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return
     */
    public static String orderRefund(boolean isSandbox, Map<String, String> params, String certPath, String certPass) {
        if (isSandbox) {
            return doPostSSL(SANDBOX_REFUND_URL, params, certPath, certPass);
        }
        return doPostSSL(REFUND_URL, params, certPath, certPass);
    }

    private static String doPostSSL(String url, Map<String, String> params, String certPath, String certPass) {
        try {
            return HttpRequest.post(url)
                    .setSSLSocketFactory(SSLSocketFactoryBuilder.create()
                        .setProtocol(SSLSocketFactoryBuilder.TLSv1)
                        .setKeyManagers(getKeyManager(certPass, certPath, null))
                        .setSecureRandom(new SecureRandom())
                        .build())
                    .body(WxPayUtil.toXml(params))
                    .execute()
                    .body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询退款
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String orderRefundQuery(boolean isSandbox, Map<String, String> params) {
        if (isSandbox) {
            return doPost(SANDBOX_REFUND_QUERY_URL, params);
        }
        return doPost(REFUND_QUERY_URL, params);
    }

    public static String doPost(String url, Map<String, String> params) {
        return HttpUtil.post(url, WxPayUtil.toXml(params));
    }

    private static KeyManager[] getKeyManager(String certPass, String certPath, InputStream certFile) throws Exception {
        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        if (certFile != null) {
            clientStore.load(certFile, certPass.toCharArray());
        } else {
            clientStore.load(new FileInputStream(certPath), certPass.toCharArray());
        }
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, certPass.toCharArray());
        return kmf.getKeyManagers();
    }
}

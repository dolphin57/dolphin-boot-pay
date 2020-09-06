package io.dolphin.pay.unionpay.constants;

/**
 * @Description: 银联支付常量
 * @Author: qianliang
 * @Since: 2019-10-16 19:06
 */
public class UnionPayConstants {
    public static final String VERSION_1_0_0 = "1.0.0";
    public static final String VERSION_5_0_0 = "5.0.0";
    public static final String VERSION_5_0_1 = "5.0.1";
    public static final String VERSION_5_1_0 = "5.1.0";

    public static final String SIGNMETHOD_RSA = "01";

    /******************************************** 5.0报文接口定义 ********************************************/
    /**
     * 版本号.
     */
    public static final String param_version = "version";

    /**
     * 证书ID.
     */
    public static final String param_certId = "certId";

    /**
     * 签名.
     */
    public static final String param_signature = "signature";

    /**
     * 签名方法.
     */
    public static final String param_signMethod = "signMethod";


    /**
     * 报文版本号，固定5.1.0，请勿改动
     */
    public static final String version = "5.1.0";

    /**
     * 签名方式，证书方式固定01，请勿改动
     */
    public static final String signMethod = "01";

    /**
     * 前台URL
     */
    public static final String FRONT_TRANS_URL = "https://gateway.test.95516.com/gateway/api/frontTransReq.do";
    /**
     * 后台URL
     */
    public static final String BACK_TRANS_URL = "https://gateway.test.95516.com/gateway/api/backTransReq.do";
    /**
     * 单笔交易查询URL
     */
    public static final String SINGLE_QUERY_URL = "https://gateway.test.95516.com/gateway/api/queryTrans.do";
    /**
     * 批量交易URL
     */
    public static final String BATCH_TRANS_URL = "https://gateway.test.95516.com/gateway/api/batchTrans.do";
    /**
     * 文件类交易URL
     */
    public static final String FILE_TRANS_URL = "https://filedownload.test.95516.com/";
    /**
     * app交易URL
     */
    public static final String APP_TRANS_URL = "https://gateway.test.95516.com/gateway/api/appTransReq.do";
    /**
     * 有卡交易URL
     */
    public static final String CARD_TRANS_URL = "https://gateway.test.95516.com/gateway/api/cardTransReq.do";

    /**
     * 以下缴费产品使用，其余产品用不到
     */

    /**
     * 前台请求地址
     */
    public static final String JF_FRONT_TRANS_URL = "https://gateway.test.95516.com/jiaofei/api/frontTransReq.do";
    /**
     * 后台请求地址
     */
    public static final String JF_BACK_TRANS_URL = "https://gateway.test.95516.com/jiaofei/api/backTransReq.do";
    /**
     * 单笔查询请求地址
     */
    public static final String JF_SINGLE_QUERY_URL = "https://gateway.test.95516.com/jiaofei/api/queryTrans.do";
    /**
     * 有卡交易地址
     */
    public static final String JF_CARD_TRANS_URL = "https://gateway.test.95516.com/jiaofei/api/cardTransReq.do";
    /**
     * App交易地址
     */
    public static final String JF_APP_TRANS_URL = "https://gateway.test.95516.com/jiaofei/api/appTransReq.do";
}

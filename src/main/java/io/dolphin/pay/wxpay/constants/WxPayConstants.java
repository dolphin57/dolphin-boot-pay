package io.dolphin.pay.wxpay.constants;

/**
 * @Description: 微信支付常量
 * @Author: qianliang
 * @Since: 2019-10-9 19:32
 */
public class WxPayConstants {
    public static final String FAIL     = "FAIL";
    public static final String SUCCESS  = "SUCCESS";
    public static final String HMACSHA256 = "HMAC-SHA256";
    public static final String MD5 = "MD5";

    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_SIGN_TYPE = "sign_type";

    /**
     * 统一下单接口
     */
    public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /**
     * 订单查询
     */
    public static final String ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    /**
     * 关闭订单
     */
    public static final String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
    /**
     * 撤销订单
     */
    public static final String REVERSE_URL = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
    /**
     * 申请退款
     */
    public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    /**
     * 查询退款
     */
    public static final String REFUND_QUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
    /**
     * 下载对账单
     */
    public static final String DOWNLOAD_BILLY_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
    /**
     * 下载资金账单
     */
    public static final String DOWNLOAD_FUND_FLOW_URL = "https://api.mch.weixin.qq.com/pay/downloadfundflow";
    /**
     * 交易保障
     */
    public static final String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";
    /**
     * 转换短链接
     */
    public static final String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
    /**
     * 授权码查询openId接口
     */
    public static final String AUTH_CODE_TO_OPENID_URL = "https://api.mch.weixin.qq.com/tools/authcodetoopenid";

    /**
     * 小程序code获取openId接口
     */
    public static final String MINI_CODE_TO_OPENID_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 刷卡支付
     */
    public static final String MICRO_PAY_URL = "https://api.mch.weixin.qq.com/pay/micropay";


    /**
     * sandbox
     */
    public static final String SANDBOX_UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder";

    public static final String SANDBOX_ORDER_QUERY_URL   = "https://api.mch.weixin.qq.com/sandboxnew/pay/orderquery";

    public static final String SANDBOX_REFUND_URL       = "https://api.mch.weixin.qq.com/sandboxnew/secapi/pay/refund";

    public static final String SANDBOX_REFUND_QUERY_URL  = "https://api.mch.weixin.qq.com/sandboxnew/pay/refundquery";
    public static final String SANDBOX_MICROPAY_URL     = "https://api.mch.weixin.qq.com/sandboxnew/pay/micropay";
    public static final String SANDBOX_REVERSE_URL      = "https://api.mch.weixin.qq.com/sandboxnew/secapi/pay/reverse";
    public static final String SANDBOX_CLOSEORDER_URL   = "https://api.mch.weixin.qq.com/sandboxnew/pay/closeorder";
    public static final String SANDBOX_DOWNLOADBILL_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/downloadbill";
    public static final String SANDBOX_REPORT_URL       = "https://api.mch.weixin.qq.com/sandboxnew/payitil/report";
    public static final String SANDBOX_SHORTURL_URL     = "https://api.mch.weixin.qq.com/sandboxnew/tools/shorturl";
    public static final String SANDBOX_AUTHCODETOOPENID_URL = "https://api.mch.weixin.qq.com/sandboxnew/tools/authcodetoopenid";
}

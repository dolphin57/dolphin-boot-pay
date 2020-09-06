package io.dolphin.pay.wxpay.enums;

/**
 * @Description:
 * @Author: qianliang
 * @Since: 2019-10-10 17:57
 */
public enum SignType {
    /**
     * HMAC-SHA256 加密
     */
    HMACSHA256("HMAC-SHA256"),
    /**
     *  MD5 加密
     */
    MD5("MD5");

    SignType(String type) {
        this.type = type;
    }

    private final String type;

    public String getType() {
        return type;
    }
}

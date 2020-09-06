package io.dolphin.pay.unionpay.config;

import cn.hutool.core.date.DateUtil;
import io.dolphin.pay.unionpay.constants.UnionPayConstants;
import io.dolphin.pay.unionpay.util.UnionPayUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: qianliang
 * @Since: 2019-10-16 18:56
 */
@Data
@Builder
@AllArgsConstructor
public class UnionPayApiBuilder {
    private String version;
    private String encoding;
    private String signMethod;
    private String txnType;
    private String txnSubType;
    private String bizType;
    private String channelType;
    private String accessType;
    private String merId;
    private String frontUrl;
    private String backUrl;
    private String orderId;
    private String currencyCode;
    private String txnAmt;
    private String txnTime;
    private String payTimeout;
    private String accNo;
    private String reqReserved;
    private String orderDesc;
    private String acqInsCode;
    private String merCatCode;
    private String merName;
    private String merAbbr;
    private String origQryId;
    private String settleDate;
    private String fileType;
    private String bussCode;
    private String billQueryInfo;
    private String qrNo;
    private String termId;
    private String accType;
    private String encryptCertId;
    private String customerInfo;

    public Map<String, String> createMap() {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isBlank(version)) {
            version = "5.1.0";
        }
        if (StringUtils.isBlank(encoding)) {
            encoding = "UTF-8";
        }
        if (StringUtils.isBlank(signMethod)) {
            signMethod = "01";
        }
        if (StringUtils.isBlank(txnType)) {
            txnType = "01";
        }
        if (StringUtils.isBlank(txnSubType)) {
            txnSubType = "01";
        }
        if (StringUtils.isBlank(bizType)) {
            bizType = "000201";
        }
        if (StringUtils.isBlank(channelType)) {
            channelType = "07";
        }
        if (StringUtils.isBlank(accessType)) {
            accessType = "0";
        }
        if (StringUtils.isBlank(merId)) {
            throw new IllegalArgumentException("merId 值不能为 null");
        }
        if (StringUtils.isBlank(backUrl)) {
            backUrl = UnionPayConstants.BACK_TRANS_URL;
        }
        if (StringUtils.isBlank(frontUrl)) {
            frontUrl = UnionPayConstants.FRONT_TRANS_URL;
        }

        if (StringUtils.isBlank(orderId)) {
            orderId = String.valueOf(System.currentTimeMillis());
        }
        if (orderId.contains("_") || orderId.contains("-")) {
            throw new IllegalArgumentException("orderId 值不应含“-”或“_”");
        }
        if (StringUtils.isBlank(currencyCode)) {
            currencyCode = "156";
        }
        if (StringUtils.isBlank(txnAmt)) {
            txnAmt = "1";
        }
        if (StringUtils.isBlank(txnTime)) {
            txnTime = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        }
        if (StringUtils.isBlank(payTimeout)) {
            payTimeout = DateUtil.format(new Date(System.currentTimeMillis() + 15 * 60 * 1000), "YYYYMMddHHmmss");
        }


        map.put("version", version);
        map.put("encoding", encoding);
        map.put("signMethod", signMethod);
        map.put("txnType", txnType);
        map.put("txnSubType", txnSubType);
        map.put("bizType", bizType);
        map.put("channelType", channelType);
        map.put("accessType", accessType);
        map.put("merId", merId);
        map.put("frontUrl", frontUrl);
        map.put("backUrl", backUrl);
        map.put("orderId", orderId);
        map.put("currencyCode", currencyCode);
        map.put("txnAmt", txnAmt);
        map.put("txnTime", txnTime);
        map.put("payTimeout", payTimeout);
        map.put("accNo", accNo);
        map.put("reqReserved", reqReserved);
        map.put("orderDesc", orderDesc);
        map.put("acqInsCode", acqInsCode);
        map.put("merCatCode", merCatCode);
        map.put("merName", merName);
        map.put("merAbbr", merAbbr);
        map.put("origQryId", origQryId);
        map.put("settleDate", settleDate);
        map.put("fileType", fileType);
        map.put("bussCode", bussCode);
        map.put("billQueryInfo", billQueryInfo);
        map.put("qrNo", qrNo);
        map.put("termId", termId);
        map.put("accType", accType);
        map.put("encryptCertId", encryptCertId);
        map.put("customerInfo", customerInfo);

        return setSignMap(map);
    }

    public Map<String, String> setSignMap(Map<String, String> map) {
        // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        return UnionPayUtil.sign(map, encoding);
    }
}

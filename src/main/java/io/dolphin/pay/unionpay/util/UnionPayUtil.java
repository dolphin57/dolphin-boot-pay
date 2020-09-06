package io.dolphin.pay.unionpay.util;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Author: qianliang
 * @Since: 2019-10-16 19:39
 */
@Slf4j
public class UnionPayUtil {
    public static Map<String, String> sign(Map<String, String> reqData, String encoding) {
        reqData = filterBlank(reqData);
        //boolean signFlag = createSign(reqData, encoding);
        return reqData;
    }

    public static Map<String, String> filterBlank(Map<String, String> contentData) {
        Map<String, String> submitFromData = new HashMap<String, String>();
        Set<String> keySet = contentData.keySet();

        for (String key : keySet) {
            String value = contentData.get(key);
            if (value != null && !"".equals(value.trim())) {
                // 对value值进行去除前后空处理
                submitFromData.put(key, value.trim());
            }
        }
        return submitFromData;
    }
}

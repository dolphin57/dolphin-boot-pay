package io.dolphin.pay.wxpay.api.controller.v1;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.dolphin.pay.dto.ResponseDto;
import io.dolphin.pay.dto.Results;
import io.dolphin.pay.util.IPUtil;
import io.dolphin.pay.wxpay.config.WxPayApi;
import io.dolphin.pay.wxpay.config.WxPayProperties;
import io.dolphin.pay.wxpay.constants.WxPayConstants;
import io.dolphin.pay.wxpay.entity.OrderQueryBuilder;
import io.dolphin.pay.wxpay.entity.RefundOrderBuilder;
import io.dolphin.pay.wxpay.entity.RefundQueryBuilder;
import io.dolphin.pay.wxpay.entity.UnifiedOrderBuilder;
import io.dolphin.pay.wxpay.enums.SignType;
import io.dolphin.pay.wxpay.enums.TradeType;
import io.dolphin.pay.wxpay.util.WxPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 微信支付controller
 * @Author: qianliang
 * @Since: 2019-10-10 8:22
 */
@Slf4j
@Controller
@RequestMapping("/wxPay")
public class WxPayController {
    @Autowired
    private WxPayProperties wxPayProperties;

    /**
     * 封装微信扫码支付：此处采用支付模式二
     * @return
     */
    @RequestMapping(value = "/scanCode", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity scanCode(@RequestBody UnifiedOrderBuilder builder) throws Exception {
        // 1.拼装统一下单参数
        String ip = IPUtil.getIp();

        Map<String, String> params = UnifiedOrderBuilder.builder()
                .appid(wxPayProperties.getAppid())
                .mch_id(wxPayProperties.getMchid())
                .nonce_str(WxPayUtil.generateNonceStr())
                .body(builder.getBody())
                .out_trade_no(builder.getOut_trade_no())
                .total_fee(builder.getTotal_fee())
                .spbill_create_ip(ip)
                .notify_url(builder.getNotify_url())
                .trade_type(TradeType.NATIVE.getTradeType())
                .build()
                .createSign(wxPayProperties.getPartnerKey(), SignType.MD5);

        // 2.调用微信封装接口进行下单
        log.info("扫码统一下单参数:{}", params);
        String xmlResult = WxPayApi.unifiedOrder(false, params);
        Map<String, String> result = WxPayUtil.xmlToMap(xmlResult);
        log.info("扫码统一下单结果:{}", result);

        // 3.返回结果的判断
        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        System.out.println(returnMsg);

        if (!WxPayUtil.verifyResultOk(returnCode)) {
            return Results.error("wx0001", "error:"+returnMsg);
        }

        // 4.扫码方式生成对应的图片输出
        return Results.success(result);
    }

    /**
     * 微信APP支付
     */
    @RequestMapping(value = "/appPay", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity appPay(@RequestBody UnifiedOrderBuilder builder) throws Exception {

        Map<String, String> params = UnifiedOrderBuilder.builder()
                .appid(wxPayProperties.getAppid())
                .mch_id(wxPayProperties.getMchid())
                .nonce_str(WxPayUtil.generateNonceStr())
                .body(builder.getBody())
                .out_trade_no(builder.getOut_trade_no())
                .total_fee(builder.getTotal_fee())
                .spbill_create_ip(IPUtil.getIp())
                .notify_url(builder.getNotify_url())
                .trade_type(TradeType.APP.getTradeType())
                .build()
                .createSign(wxPayProperties.getPartnerKey(), SignType.MD5);

        String xmlResult = WxPayApi.unifiedOrder(false, params);
        Map<String, String> result = WxPayUtil.xmlToMap(xmlResult);
        log.info("app支付结果：" + result);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayUtil.verifyResultOk(returnCode)) {
            return Results.error("wx0002", "error:"+returnMsg);
        }

        String resultCode = result.get("result_code");
        if (!WxPayUtil.verifyResultOk(resultCode)) {
            return Results.error("wx0002", "error:"+returnMsg);
        }

        // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
        String prepayId = result.get("prepay_id");
        Map<String, String> packageParams = WxPayUtil.appPrepayIdCreateSign(wxPayProperties.getAppid(), wxPayProperties.getMchid(),
                prepayId, wxPayProperties.getPartnerKey(), SignType.MD5);
        String jsonStr = JSON.toJSONString(packageParams);
        log.info("待返回给apk的json串:" + jsonStr);

        return Results.success(new ResponseDto(WxPayConstants.SUCCESS, jsonStr));
    }

    /**
     * 微信H5 支付
     * 注意：必须再web页面中发起支付且域名已添加到开发配置中
     */
    @RequestMapping(value = "/wapPay", method = {RequestMethod.POST, RequestMethod.GET})
    public void wapPay(@RequestBody UnifiedOrderBuilder builder, HttpServletResponse response) throws Exception {
        Map<String, String> params = UnifiedOrderBuilder.builder()
                .appid(wxPayProperties.getAppid())
                .mch_id(wxPayProperties.getMchid())
                .nonce_str(WxPayUtil.generateNonceStr())
                .body(builder.getBody())
                .out_trade_no(builder.getOut_trade_no())
                .total_fee(builder.getTotal_fee())
                .spbill_create_ip(IPUtil.getIp())
                .notify_url(builder.getNotify_url())
                .trade_type(TradeType.MWEB.getTradeType())
                .scene_info(builder.getScene_info())
                .build()
                .createSign(wxPayProperties.getPartnerKey(), SignType.MD5);

        String xmlResult = WxPayApi.unifiedOrder(false, params);
        Map<String, String> result = WxPayUtil.xmlToMap(xmlResult);
        log.info("H5支付结果：" + result);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayUtil.verifyResultOk(returnCode)) {
            throw new RuntimeException(returnMsg);
        }

        String resultCode = result.get("result_code");
        if (!WxPayUtil.verifyResultOk(resultCode)) {
            throw new RuntimeException(returnMsg);
        }

        // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
        String prepayId = result.get("prepay_id");
        String webUrl = result.get("mweb_url");
        log.info("prepay_id:" + prepayId + " mweb_url:" + webUrl);
        response.sendRedirect(webUrl);
    }

    /**
     * 公众号支付
     */
    @RequestMapping(value = "/mpPay", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity webPay(HttpServletRequest request, @RequestBody UnifiedOrderBuilder builder) throws Exception {
        // openId，采用 网页授权获取 access_token API：SnsAccessTokenApi获取
        String openId = (String) request.getSession().getAttribute("openId");
        if (StringUtils.isBlank(openId)) {
            return Results.error("wx0004", "未获取到openId");
        }

        Map<String, String> params = UnifiedOrderBuilder.builder()
                .appid(wxPayProperties.getAppid())
                .mch_id(wxPayProperties.getMchid())
                .nonce_str(WxPayUtil.generateNonceStr())
                .body(builder.getBody())
                .out_trade_no(builder.getOut_trade_no())
                .total_fee(builder.getTotal_fee())
                .spbill_create_ip(IPUtil.getIp())
                .notify_url(builder.getNotify_url())
                .trade_type(TradeType.JSAPI.getTradeType())
                .openid(openId)
                .build()
                .createSign(wxPayProperties.getPartnerKey(), SignType.MD5);

        String xmlResult = WxPayApi.unifiedOrder(false, params);
        Map<String, String> result = WxPayUtil.xmlToMap(xmlResult);
        log.info("公众号支付结果：" + result);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayUtil.verifyResultOk(returnCode)) {
            return Results.error("wx0003", "error:"+returnMsg);
        }

        String resultCode = result.get("result_code");
        if (!WxPayUtil.verifyResultOk(resultCode)) {
            return Results.error("wx0003", "error:"+returnMsg);
        }

        // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
        String prepayId = result.get("prepay_id");
        Map<String, String> packageParams = WxPayUtil.prepayIdCreateSign(wxPayProperties.getAppid(), prepayId,
                wxPayProperties.getPartnerKey(), SignType.MD5);
        String jsonStr = JSON.toJSONString(packageParams);
        log.info("公众号支付的参数:" + jsonStr);
        return Results.success(new ResponseDto(WxPayConstants.SUCCESS, jsonStr));
    }

    /**
     * 微信小程序支付
     */
    @RequestMapping(value = "/miniAppPay", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity miniAppPay(HttpServletRequest request, @RequestBody UnifiedOrderBuilder builder) throws Exception {
        // 需要通过授权来获取openId
        String openId = (String) request.getSession().getAttribute("openId");

        Map<String, String> params = UnifiedOrderBuilder.builder()
                .appid(wxPayProperties.getAppid())
                .mch_id(wxPayProperties.getMchid())
                .nonce_str(WxPayUtil.generateNonceStr())
                .out_trade_no(builder.getOut_trade_no())
                .total_fee(builder.getTotal_fee())
                .spbill_create_ip(IPUtil.getIp())
                .notify_url(builder.getNotify_url())
                .trade_type(TradeType.JSAPI.getTradeType())
                .openid(openId)
                .build()
                .createSign(wxPayProperties.getPartnerKey(), SignType.MD5);

        String xmlResult = WxPayApi.unifiedOrder(false, params);
        Map<String, String> result = WxPayUtil.xmlToMap(xmlResult);
        log.info("小程序支付结果：" + result);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayUtil.verifyResultOk(returnCode)) {
            return Results.error("wx0003", "error:"+returnMsg);
        }

        String resultCode = result.get("result_code");
        if (!WxPayUtil.verifyResultOk(resultCode)) {
            return Results.error("wx0003", "error:"+returnMsg);
        }

        // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
        String prepayId = result.get("prepay_id");
        Map<String, String> packageParams = WxPayUtil.miniAppPrepayIdCreateSign(wxPayProperties.getAppid(), prepayId,
                wxPayProperties.getPartnerKey(), SignType.MD5);
        String jsonStr = JSON.toJSONString(packageParams);
        log.info("小程序支付的参数:" + jsonStr);
        return Results.success(new ResponseDto(WxPayConstants.SUCCESS, jsonStr));
    }

    /**
     * 微信小程序端获取openId
     */
    public String miniGetOpenId(String code) {
        Map<String, Object> params = new HashMap<>(3);
        String result = HttpUtil.get(WxPayConstants.MINI_CODE_TO_OPENID_URL, params);

        JSONObject respJson = JSONObject.parseObject(result);
        return (String)respJson.get("openid");
    }

    /**
     * 该链接是通过【统一下单API】中提交的参数notify_url设置，如果链接无法访问，商户将无法接收到微信通知。
     * 通知url必须为直接可访问的url，不能携带参数。示例：notify_url：“https://pay.weixin.qq.com/wxpay/pay.action”
     * <p>
     * 支付完成后，微信会把相关支付结果和用户信息发送给商户，商户需要接收处理，并返回应答。
     * 对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败，微信会通过一定的策略定期重新发起通知，
     * 尽可能提高通知的成功率，但微信不保证通知最终能成功。(通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒)
     * 注意：同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。
     * 推荐的做法是，当收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过，如果没有处理过再进行处理，
     * 如果处理过直接返回结果成功。在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。
     * 特别提醒：商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/payNotify", method = {RequestMethod.POST, RequestMethod.GET})
    public String payNotify(HttpServletRequest request) throws Exception {
        String xmlMsg = WxPayUtil.readData(request);
        Map<String, String> params = WxPayUtil.xmlToMap(xmlMsg);
        log.info("支付通知参数=" + xmlMsg);

        String resultCode = params.get("result_code");
        // 签名验证,并校验返回的订单金额是否与商户侧的订单金额一致
        if (WxPayUtil.verifySign(params, wxPayProperties.getPartnerKey())) {
            // 注意重复通知的情况,同一订单号可能收到多次通知,请注意一定先判断订单状态
            if (WxPayUtil.verifyResultOk(resultCode)) {
                // 进行更新订单信息或转发到下级回调(此时得根据下级回调结果处理)
                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                return WxPayUtil.toXml(xml);
            }
        }

        return null;
    }

    /**
     * 所有微信支付的订单查询接口
     * @return
     */
    @RequestMapping(value = "/orderQuery", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity orderQuery(HttpServletRequest request, @RequestBody OrderQueryBuilder builder) throws Exception {
        Map<String, String> params = OrderQueryBuilder.builder()
                .appid(wxPayProperties.getAppid())
                .mch_id(wxPayProperties.getMchid())
                .out_trade_no(builder.getOut_trade_no())
                .nonce_str(WxPayUtil.generateNonceStr())
                .build()
                .createSign(wxPayProperties.getPartnerKey(), SignType.MD5);

        String result = WxPayApi.orderQuery(false, params);
        return Results.success(WxPayUtil.xmlToMap(result));
    }

    /**
     * 订单退款接口
     * @return
     */
    @RequestMapping(value = "/refund", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity refund(HttpServletRequest request,@RequestBody RefundOrderBuilder builder) throws Exception {
        Map<String, String> params = RefundOrderBuilder.builder()
                .appid(wxPayProperties.getAppid())
                .mch_id(wxPayProperties.getMchid())
                .nonce_str(WxPayUtil.generateNonceStr())
                .out_trade_no(builder.getOut_trade_no())
                .out_refund_no(builder.getOut_refund_no())
                .total_fee(builder.getTotal_fee())
                .refund_fee(builder.getRefund_fee())
                .build()
                .createSign(wxPayProperties.getPartnerKey(), SignType.MD5);

        String result = WxPayApi.orderRefund(false, params, wxPayProperties.getCertPath(), wxPayProperties.getMchid());
        return Results.success(WxPayUtil.xmlToMap(result));
    }

    /**
     * 微信退款查询
     */
    @RequestMapping(value = "/refundQuery", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity refundQuery(@RequestBody RefundQueryBuilder builder) throws Exception {
        Map<String, String> params = RefundQueryBuilder.builder()
                .appid(wxPayProperties.getAppid())
                .mch_id(wxPayProperties.getMchid())
                .nonce_str(WxPayUtil.generateNonceStr())
                .transaction_id(builder.getTransaction_id())
                .out_trade_no(builder.getOut_trade_no())
                .out_refund_no(builder.getOut_refund_no())
                .refund_id(builder.getRefund_id())
                .build()
                .createSign(wxPayProperties.getPartnerKey(), SignType.MD5);

        String result = WxPayApi.orderRefundQuery(false, params);
        return Results.success(WxPayUtil.xmlToMap(result));
    }
}

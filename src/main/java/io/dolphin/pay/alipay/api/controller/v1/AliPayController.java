package io.dolphin.pay.alipay.api.controller.v1;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradeCreateResponse;
import io.dolphin.pay.alipay.config.AliPayApi;
import io.dolphin.pay.alipay.config.AliPayProperties;
import io.dolphin.pay.alipay.entity.AlipayOrderBuilder;
import io.dolphin.pay.alipay.util.AliPayUtil;
import io.dolphin.pay.dto.ResponseDto;
import io.dolphin.pay.dto.Results;
import io.dolphin.pay.wxpay.constants.WxPayConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description: 支付宝支付控制器
 * @Author: qianliang
 * @Since: 2019-10-13 16:17
 */
@Slf4j
@Controller
@RequestMapping("/aliPay")
public class AliPayController {
    @Autowired
    private AliPayProperties aliPayProperties;

    /**
     * 支付宝app支付
     *
     * @param builder
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/appPay", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity appPay(@RequestBody AlipayOrderBuilder builder) throws Exception {
        try {
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            BeanCopier beanCopier = BeanCopier.create(AlipayOrderBuilder.class, AlipayTradeAppPayModel.class, false);
            beanCopier.copy(builder, model, null);
            String orderInfo = AliPayApi.appPayToResponse(model, builder.getNotifyUrl()).getBody();
            return Results.success(new ResponseDto(WxPayConstants.SUCCESS, orderInfo));
        } catch (AlipayApiException e) {
            return Results.error("ali0001", "system error:" + e.getMessage());
        }
    }

    @PostMapping(value = "/payNotify")
    @ResponseBody
    public String payNotify(HttpServletRequest request) {
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = AliPayUtil.toMap(request);
            boolean verifyResult = AlipaySignature.rsaCheckV1(params, aliPayProperties.getPublicKey(), "UTF-8", "RSA2");
            if (verifyResult) {
                // TODO: 2019-10-15 此处加上商户业务逻辑代码 异步通知可能出现订单重复通知,需要做去重处理
                System.out.println("notify_url 验证成功success");
                return "SUCCESS";
            } else {
                System.out.println("notify_url 验证失败");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            return "failure";
        }
    }

    /**
     * 手机网站支付
     *
     * @param builder
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/wapPay", method = {RequestMethod.POST, RequestMethod.GET})
    public void wapPay(@RequestBody AlipayOrderBuilder builder, HttpServletResponse response) throws Exception {
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        BeanCopier beanCopier = BeanCopier.create(AlipayOrderBuilder.class, AlipayTradeWapPayModel.class, false);
        beanCopier.copy(builder, model, null);
        AliPayApi.wapPay(response, model, builder.getReturnUrl(), builder.getNotifyUrl());
    }

    /**
     * PC网站支付
     *
     * @param builder
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pcPay", method = {RequestMethod.POST, RequestMethod.GET})
    public void pcPay(@RequestBody AlipayOrderBuilder builder, HttpServletResponse response) {
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        BeanCopier beanCopier = BeanCopier.create(AlipayOrderBuilder.class, AlipayTradePagePayModel.class, false);
        beanCopier.copy(builder, model, null);
        /**
         * 花呗分期相关的设置,测试环境不支持花呗分期的测试
         * hb_fq_num代表花呗分期数，仅支持传入3、6、12，其他期数暂不支持，传入会报错；
         * hb_fq_seller_percent代表卖家承担收费比例，商家承担手续费传入100，用户承担手续费传入0，仅支持传入100、0两种，其他比例暂不支持，传入会报错。
         */
        ExtendParams extendParams = new ExtendParams();
        extendParams.setHbFqNum("3");
        extendParams.setHbFqSellerPercent("0");
        model.setExtendParams(extendParams);
        try {
            AliPayApi.tradePage(response, model, builder.getNotifyUrl(), builder.getReturnUrl());
        } catch (Exception e) {

        }
    }

    /**
     * 扫码支付
     *
     * @param builder
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/scanPay", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity tradePrecreatePay(@RequestBody AlipayOrderBuilder builder) {
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        BeanCopier beanCopier = BeanCopier.create(AlipayOrderBuilder.class, AlipayTradePrecreateModel.class, false);
        beanCopier.copy(builder, model, null);
        try {
            String resultStr = AliPayApi.tradePrecreatePayToResponse(model, builder.getNotifyUrl()).getBody();
            JSONObject jsonObject = JSONObject.parseObject(resultStr);
            String qrCode = jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
            return Results.success(qrCode);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 交易退款
     * @param model
     * @return
     */
    @RequestMapping(value = "/tradeRefund", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity tradeRefund(@RequestBody AlipayTradeRefundModel model) {
        try {
            String result = AliPayApi.tradeRefundToResponse(model).getBody();
            return Results.success(result);
        } catch (AlipayApiException e) {
            return Results.error("ali0002", e.getMessage());
        }
    }

    /**
     * 交易查询
     * @param model
     * @return
     */
    @RequestMapping(value = "/tradeQuery", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity tradeQuery(@RequestBody AlipayTradeQueryModel model) {
        boolean isSuccess = false;
        try {
            isSuccess = AliPayApi.tradeQueryToResponse(model).isSuccess();
            return Results.success(isSuccess);
        } catch (AlipayApiException e) {
            return Results.error("ali0002", e.getMessage());
        }
    }

    /**
     * 交易查询
     * @param outTradeNo
     * @return
     */
    @RequestMapping(value = "/tradeQueryByStr", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity tradeQueryByStr(@RequestParam("out_trade_no") String outTradeNo) {
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(outTradeNo);
        try {
            String result = AliPayApi.tradeQueryToResponse(model).getBody();
            return Results.success(result);
        } catch (AlipayApiException e) {
            return Results.error("ali0002", e.getMessage());
        }
    }

    /**
     * 创建订单
     * {"alipay_trade_create_response":{"code":"10000","msg":"Success","out_trade_no":"081014283315033","trade_no":"2017081021001004200200274066"},
     * "sign":"/+/+/+AhKGUpK+/+++jUmeOuXOA=="}
     * @param model
     * @return
     */
    @RequestMapping(value = "/tradeCreate", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity tradeCreate(@RequestBody AlipayTradeCreateModel model, String notifyUrl) {
        try {
            AlipayTradeCreateResponse response = AliPayApi.tradeCreateToResponse(model, notifyUrl);
            return Results.success(response.getBody());
        } catch (AlipayApiException e) {
            return Results.error("ali0002", e.getMessage());
        }
    }

    /**
     * 撤销订单
     * @param model
     * @return
     */
    @RequestMapping(value = "/tradeCancel", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity tradeCancel(@RequestBody AlipayTradeCancelModel model) {
        boolean isSuccess = false;
        try {
            isSuccess = AliPayApi.tradeCancelToResponse(model).isSuccess();
            return Results.success(isSuccess);
        } catch (AlipayApiException e) {
            return Results.error("ali0002", e.getMessage());
        }
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/tradeClose", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity tradeClose(@RequestParam("out_trade_no") String outTradeNo, @RequestParam("trade_no") String tradeNo) {
        try {
            AlipayTradeCloseModel model = new AlipayTradeCloseModel();
            model.setOutTradeNo(outTradeNo);
            model.setTradeNo(tradeNo);
            String result = AliPayApi.tradeCloseToResponse(model).getBody();
            return Results.success(result);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/return_url")
    public String returnUrl(HttpServletRequest request) {
        try {
            // 获取支付宝GET过来反馈信息
            Map<String, String> map = AliPayUtil.toMap(request);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }

            boolean verifyResult = AlipaySignature.rsaCheckV1(map, aliPayProperties.getPublicKey(), "UTF-8", "RSA2");
            if (verifyResult) {
                // TODO 请在这里加上商户的业务逻辑程序代码
                System.out.println("return_url 验证成功");
                return "success";
            } else {
                System.out.println("return_url 验证失败");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
    }
}
package io.dolphin.pay.alipay.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @Description: 支付宝支付相关接口
 * @Author: qianliang
 * @Since: 2019-10-13 17:50
 */
@Component
public class AliPayApi {
    private static AlipayClient alipayClient;

    @Autowired
    private void setAliPayApi(AlipayClient alipayClient) {
        AliPayApi.alipayClient = alipayClient;
    }

    /**
     * APP支付
     *
     * @param model     {@link AlipayTradeAppPayModel}
     * @param notifyUrl 异步通知 URL
     * @return {@link AlipayTradeAppPayResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeAppPayResponse appPayToResponse(AlipayTradeAppPayModel model,
                                                             String notifyUrl) throws AlipayApiException {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        return alipayClient.sdkExecute(request);
    }

    /**
     * WAP支付
     *
     * @param response  {@link HttpServletResponse}
     * @param model     {@link AlipayTradeWapPayModel}
     * @param returnUrl 异步通知URL
     * @param notifyUrl 同步通知URL
     * @throws AlipayApiException 支付宝 Api 异常
     * @throws IOException        IO 异常
     */
    public static void wapPay(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws IOException, AlipayApiException {
        String form = wapPayStr(model, returnUrl, notifyUrl);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(form);
        out.flush();
        out.close();
    }

    /**
     * <p>WAP支付</p>
     *
     * <p>为了解决 Filter 中使用 OutputStream getOutputStream() 和 PrintWriter getWriter() 冲突异常问题</p>
     *
     * @param response  {@link HttpServletResponse}
     * @param model     {@link AlipayTradeWapPayModel}
     * @param returnUrl 异步通知URL
     * @param notifyUrl 同步通知URL
     * @throws AlipayApiException 支付宝 Api 异常
     * @throws IOException        IO 异常
     */
    public static void wapPayByOutputStream(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
        String form = wapPayStr(model, returnUrl, notifyUrl);
        response.setContentType("text/html;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        out.write(form.getBytes("UTF-8"));
        response.getOutputStream().flush();
    }

    /**
     * WAP支付
     *
     * @param model     {@link AlipayTradeWapPayModel}
     * @param returnUrl 异步通知URL
     * @param notifyUrl 同步通知URL
     * @return {String}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static String wapPayStr(AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException {
        AlipayTradeWapPayRequest aliPayRequest = new AlipayTradeWapPayRequest();
        aliPayRequest.setReturnUrl(returnUrl);
        aliPayRequest.setNotifyUrl(notifyUrl);
        aliPayRequest.setBizModel(model);
        return alipayClient.pageExecute(aliPayRequest).getBody();
    }

    /**
     * 电脑网站支付(PC支付)
     *
     * @param response  {@link HttpServletResponse}
     * @param model     {@link AlipayTradePagePayModel}
     * @param notifyUrl 异步通知URL
     * @param returnUrl 同步通知URL
     * @throws AlipayApiException 支付宝 Api 异常
     * @throws IOException        IO 异常
     */
    public static void tradePage(HttpServletResponse response, AlipayTradePagePayModel model, String notifyUrl, String returnUrl) throws AlipayApiException, IOException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);
        // 调用SDK生成表单
        String form = alipayClient.pageExecute(request).getBody();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(form);
        out.flush();
        out.close();
    }

    /**
     * 统一收单线下交易预创建 <br>
     * 适用于：扫码支付等 <br>
     *
     * @param model     {@link AlipayTradePrecreateModel}
     * @param notifyUrl 异步通知URL
     * @return {@link AlipayTradePrecreateResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayTradePrecreateModel model, String notifyUrl) throws AlipayApiException {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        return alipayClient.execute(request);
    }

    /**
     * 统一收单线下交易预创建 <br>
     * 适用于：扫码支付等 <br>
     *
     * @param model        {@link AlipayTradePrecreateModel}
     * @param notifyUrl    异步通知URL
     * @param appAuthToken 应用授权token
     * @return {@link AlipayTradePrecreateResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayTradePrecreateModel model,
                                                                           String notifyUrl, String appAuthToken) throws AlipayApiException {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        return alipayClient.execute(request, null, appAuthToken);
    }

    /**
     * 统一收单交易退款接口
     *
     * @param model {@link AlipayTradeRefundModel}
     * @return {@link AlipayTradeRefundResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeRefundResponse tradeRefundToResponse(AlipayTradeRefundModel model) throws AlipayApiException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizModel(model);
        return alipayClient.execute(request);
    }

    /**
     * 统一收单交易退款接口
     *
     * @param model        {@link AlipayTradeRefundModel}
     * @param appAuthToken 应用授权token
     * @return {@link AlipayTradeRefundResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeRefundResponse tradeRefundToResponse(AlipayTradeRefundModel model, String appAuthToken) throws AlipayApiException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizModel(model);
        return alipayClient.execute(request, null, appAuthToken);
    }

    /**
     * 统一收单线下交易查询接口
     *
     * @param model {@link AlipayTradeQueryModel}
     * @return {@link AlipayTradeQueryResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeQueryResponse tradeQueryToResponse(AlipayTradeQueryModel model) throws AlipayApiException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizModel(model);
        return alipayClient.execute(request);
    }

    /**
     * 统一收单线下交易查询接口
     *
     * @param model        {@link AlipayTradeQueryModel}
     * @param appAuthToken 应用授权token
     * @return {@link AlipayTradeQueryResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeQueryResponse tradeQueryToResponse(AlipayTradeQueryModel model, String appAuthToken) throws AlipayApiException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizModel(model);
        return alipayClient.execute(request, null, appAuthToken);
    }

    /**
     * 统一收单交易创建接口
     *
     * @param model     {@link AlipayTradeCreateModel}
     * @param notifyUrl 异步通知URL
     * @return {@link AlipayTradeCreateResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeCreateResponse tradeCreateToResponse(AlipayTradeCreateModel model, String notifyUrl) throws AlipayApiException {
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        return alipayClient.execute(request);
    }

    /**
     * 统一收单交易创建接口
     *
     * @param model        {@link AlipayTradeCreateModel}
     * @param notifyUrl    异步通知URL
     * @param appAuthToken 应用授权token
     * @return {@link AlipayTradeCreateResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeCreateResponse tradeCreateToResponse(AlipayTradeCreateModel model, String notifyUrl, String appAuthToken) throws AlipayApiException {
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        return alipayClient.execute(request, null, appAuthToken);
    }

    /**
     * 统一收单交易撤销接口
     *
     * @param model {@link AlipayTradeCancelModel}
     * @return {@link AlipayTradeCancelResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeCancelResponse tradeCancelToResponse(AlipayTradeCancelModel model) throws AlipayApiException {
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.setBizModel(model);
        return alipayClient.execute(request);
    }

    /**
     * 统一收单交易撤销接口
     *
     * @param model        {@link AlipayTradeCancelModel}
     * @param appAuthToken 应用授权token
     * @return {@link AlipayTradeCancelResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeCancelResponse tradeCancelToResponse(AlipayTradeCancelModel model, String appAuthToken) throws AlipayApiException {
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.setBizModel(model);
        return alipayClient.execute(request, null, appAuthToken);
    }

    /**
     * 统一收单交易关闭接口
     *
     * @param model {@link AlipayTradeCloseModel}
     * @return {@link AlipayTradeCloseResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeCloseResponse tradeCloseToResponse(AlipayTradeCloseModel model) throws AlipayApiException {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        request.setBizModel(model);
        return alipayClient.execute(request);
    }

    /**
     * 统一收单交易关闭接口
     *
     * @param model        {@link AlipayTradeCloseModel}
     * @param appAuthToken 应用授权token
     * @return {@link AlipayTradeCloseResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeCloseResponse tradeCloseToResponse(AlipayTradeCloseModel model, String appAuthToken) throws AlipayApiException {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        request.setBizModel(model);
        return alipayClient.execute(request, null, appAuthToken);
    }
}

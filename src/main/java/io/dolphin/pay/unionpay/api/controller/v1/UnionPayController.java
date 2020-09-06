package io.dolphin.pay.unionpay.api.controller.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 银联支付控制器
 * @Author: qianliang
 * @Since: 2019-10-16 18:51
 */
@Controller
@RequestMapping("/unionPay")
public class UnionPayController {
    private static final Logger logger = LoggerFactory.getLogger(UnionPayController.class);

    /**
     * PC网关支付
     * B2C跟B2B查询区别就在于bizType的不同
     */
    @RequestMapping(value = "/frontConsume",method={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public void frontConsume(HttpServletResponse response) {

    }
}

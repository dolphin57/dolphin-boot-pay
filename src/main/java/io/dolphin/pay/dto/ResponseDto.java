package io.dolphin.pay.dto;

import io.dolphin.pay.wxpay.constants.WxPayConstants;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: qianliang
 * @Since: 2019-10-11 15:04
 */
@Data
//@ApiModel("通用响应体")
public class ResponseDto<T> implements Serializable {

    //@ApiModelProperty(value = "错误码", required = true, extensions = @Extension(name = "since", properties = {@ExtensionProperty(name = "1.0", value = "add")}))
    private String code = WxPayConstants.SUCCESS;
    //@ApiModelProperty(value = "错误信息", extensions = @Extension(name = "since", properties = {@ExtensionProperty(name = "1.0", value = "add")}))
    private String message;
    //@ApiModelProperty(value = "响应结果", extensions = @Extension(name = "since", properties = {@ExtensionProperty(name = "1.0", value = "add")}))
    private T data;


    public ResponseDto() {

    }

    public ResponseDto(String message) {
        this.code = WxPayConstants.SUCCESS;
        this.message = message;
    }

    public ResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseDto(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseDto(T data) {
        this.data = data;
    }

    public ResponseDto(String code, T data) {
        this.code = code;
        this.data = data;
    }
}

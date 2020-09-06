package io.dolphin.pay.dto;

import io.dolphin.pay.wxpay.constants.WxPayConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 封装返回 ResponseEntity
 *
 * @author AARON.YI
 */
public class Results {

    private static final ResponseEntity NO_CONTENT = new ResponseEntity<>(new ResponseDto(WxPayConstants.SUCCESS, null), HttpStatus.NO_CONTENT);
    private static final ResponseEntity ERROR = new ResponseEntity<>(new ResponseDto("error"), HttpStatus.INTERNAL_SERVER_ERROR);
    private static final ResponseEntity INVALID = new ResponseEntity<>(new ResponseDto("invalid"), HttpStatus.BAD_REQUEST);

    private Results() {
    }

    public static ResponseEntity success(ResponseDto respDTO) {
        if (respDTO == null) {
            return NO_CONTENT;
        }
        return new ResponseEntity<>(respDTO, HttpStatus.OK);
    }

    /**
     * 请求成功
     *
     * @param data 返回值
     * @return HttpStatus 200
     */
    public static <T> ResponseEntity success(T data) {
        if (data == null) {
            return NO_CONTENT;
        }
        return new ResponseEntity<>(new ResponseDto<>(WxPayConstants.SUCCESS, data), HttpStatus.OK);
    }

    /**
     * 业务异常
     *
     * @param message 返回值
     * @return HttpStatus 200
     */
    public static ResponseEntity success(String message) {
        return new ResponseEntity<>(new ResponseDto(message), HttpStatus.OK);
    }

    /**
     * 业务异常
     *
     * @param message 返回值
     * @return HttpStatus 200
     */
    public static ResponseEntity failed(String code, String message) {
        return new ResponseEntity<>(new ResponseDto(code, message), HttpStatus.OK);
    }


    /**
     * 请求成功, 已创建
     *
     * @param data 返回值
     * @return HttpStatus 201
     */
    public static ResponseEntity created(String code, Object data) {
        return new ResponseEntity<>(new ResponseDto(code, data), HttpStatus.CREATED);
    }

    /**
     * 请求成功,无返回值
     *
     * @return HttpStatus 204
     */
    public static ResponseEntity success() {
        return NO_CONTENT;
    }

    /**
     * 请求失败 请求参数错误
     *
     * @return HttpStatus 400
     */
    public static ResponseEntity invalid() {
        return INVALID;
    }

    /**
     * 请求失败 请求参数错误
     *
     * @param data 返回值
     * @return HttpStatus 400
     */
    public static ResponseEntity invalid(String code, Object data) {
        return new ResponseEntity<>(new ResponseDto(code, data), HttpStatus.BAD_REQUEST);
    }

    /**
     * 请求失败 服务端错误
     *
     * @return HttpStatus 500
     */
    public static ResponseEntity error() {
        return ERROR;
    }

    /**
     * 请求失败 服务端错误
     *
     * @param message 返回值
     * @return HttpStatus 500
     */
    public static ResponseEntity error(String code, String message) {
        return new ResponseEntity<>(new ResponseDto(code, message), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * 自定义Http返回
     *
     * @param code    HttpStatus code
     * @param message 是否为失败响应
     * @param data    返回值
     * @return ResponseEntity
     */
    public static <T> ResponseEntity newResult(String code, String message, T data) {
        return new ResponseEntity<>(new ResponseDto(code, message, data), HttpStatus.OK);
    }

}

package io.dolphin.pay.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * @Description:
 * @Author: qianliang
 * @Since: 2019-10-10 18:01
 */
public class PayUtil {
    /**
     * 把所有元素排序
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            // 拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                content.append(key + "=" + value);
            } else {
                content.append(key + "=" + value + "&");
            }
        }
        return content.toString();
    }

    public static String hmacSha256(String data, String key) {
        return SecureUtil.hmac(HmacAlgorithm.HmacSHA256, key).digestHex(data, CharsetUtil.UTF_8);
    }

    public static String md5(String data) {
        return SecureUtil.md5(data);
    }

    /**
     * 图形码生成工具
     *
     * @param contents        内容
     * @param barcodeFormat   BarcodeFormat对象
     * @param format          图片格式，可选[png,jpg,bmp]
     * @param width           宽
     * @param height          高
     * @param margin          边框间距px
     * @param saveImgFilePath 存储图片的完整位置，包含文件名
     * @return {boolean}
     */
    public static boolean encode(String contents, BarcodeFormat barcodeFormat, Integer margin,
                                 ErrorCorrectionLevel errorLevel, String format, int width, int height, String saveImgFilePath) {
        Boolean bool = false;
        BufferedImage bufImg;
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>(3);
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, errorLevel);
        hints.put(EncodeHintType.MARGIN, margin);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, barcodeFormat, width, height, hints);
            MatrixToImageConfig config = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
            bufImg = MatrixToImageWriter.toBufferedImage(bitMatrix, config);
            bool = writeToFile(bufImg, format, saveImgFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 将BufferedImage对象写入文件
     *
     * @param bufImg          BufferedImage对象
     * @param format          图片格式，可选[png,jpg,bmp]
     * @param saveImgFilePath 存储图片的完整位置，包含文件名
     * @return {boolean}
     */
    public static boolean writeToFile(BufferedImage bufImg, String format, String saveImgFilePath) {
        Boolean bool = false;
        try {
            bool = ImageIO.write(bufImg, format, new File(saveImgFilePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }
}

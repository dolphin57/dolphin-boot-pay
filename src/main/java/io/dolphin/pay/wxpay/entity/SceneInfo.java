package io.dolphin.pay.wxpay.entity;

import lombok.Data;

/**
 * @Description: 场景信息(包含所有支付方式)
 * @Author: qianliang
 * @Since: 2019-10-12 8:34
 */
@Data
public class SceneInfo {
    private H5 h5_info;

    private MiniScan store_info;

    public static class MiniScan {
        private String id;
        private String name;
        private String area_code;
        private String address;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getArea_code() {
            return area_code;
        }

        public void setArea_code(String area_code) {
            this.area_code = area_code;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public static class H5 {
        private String type;
        // IOS、安卓
        private String app_name;
        // IOS端特有
        private String bundle_id;
        // 安卓端独有
        private String package_name;
        // wap网站
        private String wap_url;
        private String wap_name;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getApp_name() {
            return app_name;
        }

        public void setApp_name(String app_name) {
            this.app_name = app_name;
        }

        public String getBundle_id() {
            return bundle_id;
        }

        public void setBundle_id(String bundle_id) {
            this.bundle_id = bundle_id;
        }

        public String getPackage_name() {
            return package_name;
        }

        public void setPackage_name(String package_name) {
            this.package_name = package_name;
        }

        public String getWap_url() {
            return wap_url;
        }

        public void setWap_url(String wap_url) {
            this.wap_url = wap_url;
        }

        public String getWap_name() {
            return wap_name;
        }

        public void setWap_name(String wap_name) {
            this.wap_name = wap_name;
        }
    }
}

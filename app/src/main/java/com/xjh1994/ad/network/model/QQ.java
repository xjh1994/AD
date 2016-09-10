package com.xjh1994.ad.network.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/25.
 */

public class QQ implements Serializable {


    /**
     * {"qq":{"openid":"8C6B1B2904ABE080D227CB9649972E83","access_token":"6921A4AB01F4D8E61B6E7B3B7D053FB9","expires_in":7776000}}
     * openid : 8C6B1B2904ABE080D227CB9649972E83
     * access_token : 6921A4AB01F4D8E61B6E7B3B7D053FB9
     * expires_in : 7776000
     */

    private QqBean qq;

    public QqBean getQq() {
        return qq;
    }

    public void setQq(QqBean qq) {
        this.qq = qq;
    }

    public static class QqBean implements Serializable {
        private String openid;
        private String access_token;
        private int expires_in;

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }
    }
}

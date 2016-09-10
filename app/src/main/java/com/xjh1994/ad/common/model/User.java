package com.xjh1994.ad.common.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/8/24.
 */

public class User extends BmobUser {

    private String nick;
    private String avatar;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

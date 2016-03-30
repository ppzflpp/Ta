package com.dragon.ta.model;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/3/10 0010.
 */
public class User extends BmobUser implements Serializable {


    private boolean isLogin;
    private boolean sex;
    private String nick;

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }


    public boolean getSex() {
        return this.sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

}

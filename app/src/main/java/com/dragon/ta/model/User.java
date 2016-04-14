package com.dragon.ta.model;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/3/10 0010.
 */
public class User extends BmobUser implements Serializable {


    private boolean isLogin;
    private boolean sex;
    private String nick;
    private String address;
    private String phone;
    private String zoneCode;
    private String iconPath;

    public void setIconPath(String iconPath){
        this.iconPath = iconPath;
    }

    public String getIconPath(){
        return iconPath;
    }

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

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getZoneCode() {
        return this.zoneCode;
    }

}

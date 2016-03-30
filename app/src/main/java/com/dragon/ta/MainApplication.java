package com.dragon.ta;

import android.app.Application;
import android.content.SharedPreferences;

import com.dragon.ta.model.User;
import com.facebook.drawee.backends.pipeline.Fresco;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/3/10 0010.
 */
public class MainApplication extends Application {

    private User mUser = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        Bmob.initialize(this,"258351f2c64ca79bd1d6a70e5d89f17f");
        mUser = BmobUser.getCurrentUser(this,User.class);
        if(mUser == null){
            mUser = new User();
        }else{
            mUser.setIsLogin(true);
        }
    }

   public User getUser(){
       return mUser;
   }

    public void setUser(User user){
        mUser = user;
    }
}

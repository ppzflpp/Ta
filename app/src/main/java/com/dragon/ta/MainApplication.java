package com.dragon.ta;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.dragon.ta.model.User;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import c.b.BP;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/3/10 0010.
 */
public class MainApplication extends Application {

    private final static String BOMB_APP_ID = "258351f2c64ca79bd1d6a70e5d89f17f";

    private User mUser = null;

    private ArrayList<DataChangeListener> mDataChangesListenerList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        Bmob.initialize(this,BOMB_APP_ID);
        BP.init(this,BOMB_APP_ID);
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

    public void logout(){
        mUser.setIsLogin(false);
        refleshUI();
    }

    public void refleshUI(){
        for(DataChangeListener listener : mDataChangesListenerList){
            listener.onDataChange();
        }
    }

    public void registerDataListener(DataChangeListener listener){
        if(mDataChangesListenerList.contains(listener)){
            return;
        }else {
            mDataChangesListenerList.add(listener);
            listener.onDataChange();
        }
    }

    public void unregisterDataListener(DataChangeListener listener){
        if(!mDataChangesListenerList.contains(listener)){
            return;
        }else{
            mDataChangesListenerList.remove(listener);
        }
    }

    public interface DataChangeListener{
        void onDataChange();
    }
}

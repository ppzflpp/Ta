package com.dragon.ta.manager;

import android.util.Log;

import com.dragon.ta.model.CartGood;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/30.
 */
public class CartManager {
    private final static String TAG = "CartManager";

    private static CartManager mInstance;
    private ArrayList<CartGood> mCartGoodsArray;

    private ArrayList<CartManagerStateChangeListener> mListeners = new ArrayList();

    private Object obj = new Object();

    private CartManager() {
        mCartGoodsArray = new ArrayList();
    }

    private void checkGoodExist(CartGood good) {
        synchronized (obj) {
            for (CartGood tmpGood : mCartGoodsArray) {
                if (tmpGood.getGood().getId() == good.getGood().getId()) {
                    //exist,remove old
                    mCartGoodsArray.remove(tmpGood);
                    break;
                }
            }
        }
    }

    public static CartManager getInstance() {
        if (mInstance == null)
            mInstance = new CartManager();
        return mInstance;
    }

    public void addGood(CartGood good) {
        checkGoodExist(good);
        synchronized (obj) {
            mCartGoodsArray.add(good);
        }
    }

    public void deleteGood(CartGood good) {
        synchronized (obj) {
            mCartGoodsArray.remove(good);
        }
    }

    public ArrayList<CartGood> getCartGoods(){
        return mCartGoodsArray;
    }

    public void pay(){

    }

    public void updateCartGoodsState(CartGood cartGood,boolean checked){
        if(cartGood != null){
            cartGood.setChecked(checked);
        }

        for(CartManagerStateChangeListener listener : mListeners){
            listener.onCartManagerStateChangeListener();
        }
    }

    public float getAllCheckedMoney(){
        float money = 0;
        synchronized (obj) {
            for(CartGood cartGood : mCartGoodsArray){
                if(cartGood.isChecked()) {
                    money += cartGood.getCount() * Integer.valueOf(cartGood.getGood().getPrice());
                }
            }
        }
        return money;
    }


    public void registerCartManagerStateChangeListener(CartManagerStateChangeListener listener){
        if(mListeners.contains(listener)){
            Log.w(TAG, "listener has registered");
            return ;
        }else{
            mListeners.add(listener);
        }
    }

    public void unRegisterCartManagerStateChangeListener(CartManagerStateChangeListener listener){
        if(mListeners.contains(listener)){
            mListeners.remove(listener);
            return ;
        }else{
            Log.w(TAG, "listener has unRegistered");
        }
    }

    public interface CartManagerStateChangeListener{
        void onCartManagerStateChangeListener();
    }

}

package com.dragon.ta.manager;

import com.dragon.ta.model.CartGood;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/30.
 */
public class CartManager {

    private static CartManager mInstance;
    private ArrayList<CartGood> mCartGoodsArray;

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

}

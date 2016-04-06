package com.dragon.ta.model;

import android.util.SparseArray;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/30.
 */
public class CartGood {

    private Good good;
    private int count;
    private boolean checked;

    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setChecked(boolean checked){
        this.checked = checked;
    }

    public boolean isChecked(){
        return this.checked;
    }
}

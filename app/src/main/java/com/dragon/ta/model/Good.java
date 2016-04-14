package com.dragon.ta.model;

import java.io.Serializable;
import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/3/30.
 */
public class Good extends BmobObject implements Serializable {
    private int id;
    private String name;
    private String price;
    private BmobFile thumb;
    private String info;
    private ArrayList<String> images = new ArrayList<>();

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public BmobFile getThumb() {
        return thumb;
    }

    public void setThumb(BmobFile thumb) {
        this.thumb = thumb;
    }

    public ArrayList<String> getImages() {
        return images;
    }
    public void setImages(ArrayList<String> list){
        this.images = list;
    }
}

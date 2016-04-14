package com.dragon.ta.model;

import android.content.Context;

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
    private BmobFile image1;
    private BmobFile image2;
    private BmobFile image3;
    private BmobFile image4;
    private BmobFile image5;

    public BmobFile getImage1() {
        return image1;
    }

    public void setImage1(BmobFile image1) {
        this.image1 = image1;
    }

    public BmobFile getImage2() {
        return image2;
    }

    public void setImage2(BmobFile image2) {
        this.image2 = image2;
    }

    public BmobFile getImage3() {
        return image3;
    }

    public void setImage3(BmobFile image3) {
        this.image3 = image3;
    }

    public BmobFile getImage4() {
        return image4;
    }

    public void setImage4(BmobFile image4) {
        this.image4 = image4;
    }

    public BmobFile getImage5() {
        return image5;
    }

    public void setImage5(BmobFile image5) {
        this.image5 = image5;
    }

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

    public ArrayList<String> getImages(Context context){
        ArrayList<String> list = new ArrayList<>();
        if(image1 != null) {
            list.add(image1.getFileUrl(context));
        }
        if(image2 != null) {
            list.add(image2.getFileUrl(context));
        }
        if(image3 != null) {
            list.add(image3.getFileUrl(context));
        }
        if(image4 != null) {
            list.add(image4.getFileUrl(context));
        }
        if(image5 != null) {
            list.add(image5.getFileUrl(context));
        }
        return list;
    }
}

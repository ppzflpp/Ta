package com.dragon.ta.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/30.
 */
public class Good implements Serializable{
    private int id;
    private String name;
    private String price;
    private String thumb;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    private String info;
    private ArrayList<String> images = new ArrayList<>();

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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void addImage(String image) {
        this.images.add(image);
    }

    public void clearImages(){
        images.clear();
    }

    public static final List<Good> ITEMS = new ArrayList<Good>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Good> ITEM_MAP = new HashMap<String, Good>();

    private static final int COUNT = 10;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Good item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.getId()), item);
    }

    private static Good createDummyItem(int position) {
        Good good = new Good();
        good.setId(position);
        good.setInfo("韩国进口产品，非常好用");
        good.setName("Item " + position);
        good.setPrice(String.valueOf(20 + position));
        good.setThumb("http://pic1.dididadidi.com/201306/63bf0135dcdbba93a7da3d9cded854ad.jpg");
        good.addImage("http://banbao.chazidian.com/uploadfile/2016-01-25/s145368924044608.jpg");
        good.addImage("http://pic13.nipic.com/20110415/1369025_121513630398_2.jpg");
        return good;
    }
}

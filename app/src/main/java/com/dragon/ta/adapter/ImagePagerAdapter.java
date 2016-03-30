package com.dragon.ta.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dragon.ta.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/30.
 */
public class ImagePagerAdapter extends PagerAdapter {

    private Context mContext;

    private ArrayList<String> mArrayList;
    private ArrayList<View> mViews = new ArrayList<View>();

    public ImagePagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);
        container.addView(view);
        Log.d("TAG", "----------urlString = " + position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = mViews.get(position);
        container.removeView(view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setImageList(ArrayList<String> list) {
        mArrayList = list;
        initViews();
    }

    private void initViews() {
        if (mArrayList != null) {
            for (String urlString : mArrayList) {
                if (urlString != null && !urlString.equals("")) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.good_detail_image, null, false);
                    SimpleDraweeView simpleView = (SimpleDraweeView)view.findViewById(R.id.good_detail_image);
                    simpleView.setImageURI(Uri.parse(urlString));
                    Log.d("TAG", "----------urlString = " + urlString);
                    mViews.add(view);
                }
            }
        }
    }
}

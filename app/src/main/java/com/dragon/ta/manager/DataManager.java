package com.dragon.ta.manager;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.dragon.ta.model.Good;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/4/14.
 */
public class DataManager {
    public final static int MSG_LOAD_DATA_SUCCESS = 1;

    private final static String TAG = "DataManager";
    private static DataManager sInstance;
    private List<Good> mGoodsList;
    private Thread mLoadThread;
    private Context mContext;

    private DataManager(Context context) {
        mContext = context;
    }

    public static DataManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataManager(context);
        }
        return sInstance;
    }

    public void loadData(boolean force, Handler handler) {
        if (!force && mGoodsList != null && mGoodsList.size() > 0) {
            handler.sendEmptyMessage(MSG_LOAD_DATA_SUCCESS);
        }
        loadData(handler);
    }

    private void loadData(final Handler handler) {
        if (mLoadThread != null && mLoadThread.isAlive()) {
            return;
        }

        mLoadThread = new Thread() {
            public void run() {
                super.run();
                BmobQuery<Good> query = new BmobQuery<Good>();
                query.addWhereEqualTo("show", true);
                query.setLimit(50);
                query.findObjects(mContext, new FindListener<Good>() {
                            @Override
                            public void onSuccess(List<Good> object) {
                                // TODO Auto-generated method stub
                                mGoodsList = object;
                                Log.d(TAG, "onSuccess,size = " + object.size());
                                //notify client,data is ready
                                handler.sendEmptyMessage(MSG_LOAD_DATA_SUCCESS);
                            }

                            @Override
                            public void onError(int code, String msg) {
                                // TODO Auto-generated method stub
                                Log.d(TAG, "load data error," + msg);
                            }
                        }

                );
            }
        };
        mLoadThread.start();
    }

    public List<Good> getData() {
        return mGoodsList;
    }
}

package com.dragon.ta.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.dragon.ta.MainApplication;
import com.dragon.ta.R;
import com.dragon.ta.fragment.OrderItemRecyclerViewAdapter;
import com.dragon.ta.model.Order;

import java.util.List;
import java.util.logging.LogRecord;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

public class OrderActivity extends AppCompatActivity {

    private List<Order> mOrderList ;
    private RecyclerView mOrderRecycleView;
    private  OrderItemRecyclerViewAdapter mOrderAdapter;
    private TextView mEmptyView;

    private final static int MSG_LOAD_DATA_SUCCESS = 0;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_LOAD_DATA_SUCCESS:
                    loadData();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        mEmptyView = (TextView)findViewById(R.id.order_empty_view);
        mOrderRecycleView = (RecyclerView)findViewById(R.id.order_list);
        mOrderAdapter = new OrderItemRecyclerViewAdapter(this,mOrderList,mEmptyView);
        mOrderRecycleView.setAdapter(mOrderAdapter);

        BmobQuery<Order> query = new BmobQuery<>();
        query.addWhereEqualTo("userName", ((MainApplication) getApplication()).getUser().getUsername());
        query.findObjects(this, new FindListener<Order>() {
            @Override
            public void onSuccess(List<Order> object) {
                // TODO Auto-generated method stub
                toast(getString(R.string.query_order_success));
                mOrderList = object;
                mHandler.sendEmptyMessage(MSG_LOAD_DATA_SUCCESS);
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                toast(getString(R.string.query_order_fail));
            }
        });


    }

    private void loadData(){
        mOrderAdapter.updateList(mOrderList);
    }

    private void toast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}

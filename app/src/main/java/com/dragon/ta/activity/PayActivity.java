package com.dragon.ta.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragon.ta.R;
import com.dragon.ta.manager.CartManager;
import com.dragon.ta.model.CartGood;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class PayActivity extends AppCompatActivity {

    private View mWeiXinPayView;
    private View mZhiFuBaoPayView;
    private LinearLayout mCartGoodsContainer;
    private TextView mPayCount ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initViews();
    }

    private void initViews() {
        mCartGoodsContainer = (LinearLayout) findViewById(R.id.activity_pay_good_container);
        addCartGoodsViews(mCartGoodsContainer);

        ((TextView)findViewById(R.id.activity_pay_count)).setText(String.valueOf(CartManager.getInstance().getAllCheckedMoney()));

        mWeiXinPayView = findViewById(R.id.activity_pay_by_weixin);
        mWeiXinPayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payByWeiXin();
            }
        });

        mZhiFuBaoPayView = findViewById(R.id.activity_pay_by_zhifubao);
        mZhiFuBaoPayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payByZhiFuBao();
            }
        });


    }

    private void addCartGoodsViews(LinearLayout container) {
        ArrayList<CartGood> arrayList = CartManager.getInstance().getCartGoods();
        for (CartGood cartGood : arrayList) {
            if (cartGood.isChecked()) {
                View view = generateItemView(cartGood);
                container.addView(view);
            }
        }
    }

    private View generateItemView(CartGood cartGood) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_pay_item, null);
        ((SimpleDraweeView) view.findViewById(R.id.activity_pay_good_thumb)).setImageURI(Uri.parse(cartGood.getGood().getThumb()));
        ((TextView) view.findViewById(R.id.activity_pay_good_name)).setText(cartGood.getGood().getName());
        ((TextView) view.findViewById(R.id.activity_pay_good_price)).setText(String.valueOf(cartGood.getGood().getPrice()));
        ((TextView) view.findViewById(R.id.activity_pay_good_count)).setText(String.valueOf(cartGood.getCount()));
        return view;
    }

    private void payByWeiXin() {

    }

    private void payByZhiFuBao() {

    }
}

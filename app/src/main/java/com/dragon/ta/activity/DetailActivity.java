package com.dragon.ta.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dragon.ta.R;
import com.dragon.ta.adapter.ImagePagerAdapter;
import com.dragon.ta.manager.CartManager;
import com.dragon.ta.model.CartGood;
import com.dragon.ta.model.Good;

public class DetailActivity extends AppCompatActivity {

    private Good mGood;
    private int mGoodsCount = 1;
    private CartGood mCartGood;

    //view
    private ViewPager mViewPager;
    private ImagePagerAdapter mViewPagerAdapter;
    private TextView mAddCartView;
    private ImageView mIncView;
    private ImageView mDecView;
    private TextView mGoodsCountView;
    private TextView mAllMoneyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mGood = (Good) getIntent().getSerializableExtra("good");

        createViews();
    }

    private void updateViews(int goodsCount) {
        mGoodsCountView.setText(String.valueOf(goodsCount));
        mAllMoneyView.setText(getString(R.string.all_money) + goodsCount * Integer.valueOf(mGood.getPrice()));
    }

    private void createViews() {
        ((TextView) findViewById(R.id.detail_layout_name)).setText(mGood.getName());
        ((TextView) findViewById(R.id.detail_layout_price)).setText("￥" + mGood.getPrice());
        ((TextView) findViewById(R.id.detail_layout_info)).setText(mGood.getInfo());

        mIncView = (ImageView) findViewById(R.id.detail_layout_inc);
        mIncView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++mGoodsCount;
                updateViews(mGoodsCount);
            }
        });

        mDecView = (ImageView) findViewById(R.id.detail_layout_dec);
        mDecView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoodsCount <= 1) {
                    return;
                }
                --mGoodsCount;
                updateViews(mGoodsCount);
            }
        });

        mAllMoneyView = (TextView) findViewById(R.id.money_all);

        mGoodsCountView = (TextView) findViewById(R.id.detail_layout_count);
        mGoodsCountView.setText(String.valueOf(mGoodsCount));

        updateViews(mGoodsCount);

        mAddCartView = (TextView) findViewById(R.id.detail_layout_add_cart);
        mAddCartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart();
                showUserTip();
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.detail_layout_view_pager);
        mViewPagerAdapter = new ImagePagerAdapter(this.getApplicationContext());
        mViewPagerAdapter.setImageList(mGood.getImages(getApplicationContext()));
        mViewPager.setAdapter(mViewPagerAdapter);

    }

    private void addCart() {
        if (mCartGood == null) {
            mCartGood = new CartGood();
        }
        mCartGood.setGood(mGood);
        mCartGood.setCount(mGoodsCount);
        updateCartGood(mCartGood);
    }

    private void showUserTip() {
        Toast.makeText(this, R.string.add_cart_success, Toast.LENGTH_SHORT).show();
    }


    private void updateCartGood(CartGood cartGood) {
        CartManager.getInstance().addGood(cartGood);
    }
}

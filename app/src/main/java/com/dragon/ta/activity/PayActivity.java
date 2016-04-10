package com.dragon.ta.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dragon.ta.MainApplication;
import com.dragon.ta.R;
import com.dragon.ta.manager.CartManager;
import com.dragon.ta.model.CartGood;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import c.b.BP;
import c.b.PListener;

public class PayActivity extends AppCompatActivity {

    private View mWeiXinPayView;
    private View mZhiFuBaoPayView;
    private LinearLayout mCartGoodsContainer;
    private TextView mPayCount ;
    private ProgressDialog mProgressDialog;

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


    private void showDialog(String message) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setCancelable(true);
            }
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void payByWeiXin() {
        showDialog(getString(R.string.getting_order));
        final String orderName = getString(R.string.app_name);
        final String orderInfo = "";
        final float orderPrice = CartManager.getInstance().getAllCheckedMoney();

        BP.pay(this, orderName,orderInfo,orderPrice, false, new PListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(PayActivity.this,getString(R.string.pay_unknown),
                        Toast.LENGTH_SHORT).show();
                hideDialog();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(PayActivity.this,getString(R.string.pay_succeed), Toast.LENGTH_SHORT)
                        .show();
                hideDialog();
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                showDialog(getString(R.string.get_order_success_redirect_pay_page));
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {

                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    new AlertDialog.Builder(PayActivity.this)
                            .setMessage(
                                    "监测到你尚未安装支付插件,无法进行微信支付,请选择安装插件(已打包在本地,无流量消耗)还是用支付宝支付")
                            .setPositiveButton(getString(R.string.install),
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            installBmobPayPlugin("bp_wx.db");
                                        }
                                    })
                            .setNegativeButton(getString(R.string.pay_by_zhifubao),
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            payByZhiFuBao();
                                        }
                                    }).create().show();
                } else {
                    Toast.makeText(PayActivity.this,getString(R.string.pay_fail),
                            Toast.LENGTH_SHORT).show();
                }
                hideDialog();
            }
        });
    }

    private void payByZhiFuBao() {
        showDialog(getString(R.string.getting_order));
        final String orderName = getString(R.string.app_name);
        final String orderInfo = "";
        final float orderPrice = CartManager.getInstance().getAllCheckedMoney();

        BP.pay(this, orderName,orderInfo,orderPrice, true, new PListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(PayActivity.this,getString(R.string.pay_unknown),
                        Toast.LENGTH_SHORT).show();
                hideDialog();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(PayActivity.this,getString(R.string.pay_succeed), Toast.LENGTH_SHORT)
                        .show();
                hideDialog();
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                saveOrder(orderId,orderName,orderInfo,String.valueOf(orderPrice));
                 //"获取订单成功!请等待跳转到支付页面~"
                showDialog(getString(R.string.get_order_success_redirect_pay_page));
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {
                Toast.makeText(PayActivity.this,getString(R.string.pay_fail), Toast.LENGTH_SHORT)
                        .show();
                hideDialog();
            }
        });
    }

    private void saveOrder(String orderId,String orderName,String orderInfo,String orderPrice){
        CartManager.getInstance().saveOrder(getApplicationContext(),((MainApplication)getApplication()).getUser().getUsername(),orderId,orderName,orderInfo,orderPrice);
    }

    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

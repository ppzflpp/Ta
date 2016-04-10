package com.dragon.ta.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragon.ta.R;

public class ProfileActivity extends AppCompatActivity {

    private LinearLayout mHeadContainer;
    private ImageView mHeadIconView;

    private LinearLayout mNickContainer;
    private TextView mNickView;

    private TextView mUserNameView;

    private LinearLayout mGoodAddressContainer;
    private TextView mGoodAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
    }

    private void initViews(){
        mHeadContainer = (LinearLayout)findViewById(R.id.head_container);
        mHeadContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                startActivityForResult(intent, 10);
            }
        });
        mHeadIconView = (ImageView)findViewById(R.id.head_view);

        mNickContainer = (LinearLayout)findViewById(R.id.nick_container);
        mNickContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,ProfileEditActivity.class);
                intent.putExtra("edit_style",0);
                startActivityForResult(intent, 0);
            }
        });
        mNickView = (TextView)findViewById(R.id.nick_view);

        mGoodAddressContainer = (LinearLayout)findViewById(R.id.good_address_container);
        mGoodAddressContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                intent.putExtra("edit_style", 1);
                startActivityForResult(intent, 1);
            }
        });
        mGoodAddress = (TextView)findViewById(R.id.good_address_view);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case 0:
                String nickString = data.getStringExtra("nick");
                mNickView.setText(nickString);
                break;
            case 1:
                String goodAddressString = data.getStringExtra("good_address");
                mGoodAddress.setText(goodAddressString);
                break;
            case 10:
                Bitmap bitmap = null;
                //mHeadIconView.setImageBitmap();
                break;
        }
    }

}

package com.dragon.ta.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragon.ta.MainApplication;
import com.dragon.ta.R;
import com.dragon.ta.model.User;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

public class ProfileActivity extends AppCompatActivity {

    private final static String TAG = "ProfileActivity";

    private View mHeadContainer;
    private SimpleDraweeView mHeadIconView;

    private View mNickContainer;
    private TextView mNickView;

    private TextView mUserNameView;

    private View mGoodAddressContainer;
    private TextView mGoodAddress;
    private User mUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mUser = ((MainApplication) getApplication()).getUser();
        initViews();

    }

    private void initViews() {
        mHeadContainer = findViewById(R.id.head_container);
        mHeadContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                startActivityForResult(intent, 10);
            }
        });
        mHeadIconView = (SimpleDraweeView) findViewById(R.id.head_view);
        if(mUser.getIcon() != null){
            BmobFile file = mUser.getIcon();
            Log.d("TAG","url = " + file.getFileUrl(getApplicationContext()));
            Uri uri = Uri.parse(file.getFileUrl(getApplicationContext()));
            mHeadIconView.setImageURI(uri);
        }

        mNickContainer = findViewById(R.id.nick_container);
        mNickContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                intent.putExtra("edit_style", 0);
                startActivityForResult(intent, 0);
            }
        });
        mNickView = (TextView) findViewById(R.id.nick_view);
        if (mUser.getNick() != null) {
            mNickView.setText(mUser.getNick());
        }

        mUserNameView = (TextView) findViewById(R.id.user_name_view);
        mUserNameView.setText(mUser.getUsername());

        mGoodAddressContainer = findViewById(R.id.good_address_container);
        mGoodAddressContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
                intent.putExtra("edit_style", 1);
                startActivityForResult(intent, 1);
            }
        });
        mGoodAddress = (TextView) findViewById(R.id.good_address_view);
        if (mUser.getAddress() != null) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(mUser.getAddress())
                    .append("\n")
                    .append(mUser.getPhone())
                    .append("\n")
                    .append(mUser.getZoneCode());

            String goodAddressString = buffer.toString();
            mGoodAddress.setText(goodAddressString);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "requestCode = " + requestCode + ",resultCode = " + resultCode);
        if (data == null) {
            Log.i(TAG, "edit cancel by user");
            return;
        }
        switch (requestCode) {
            case 0:
                String nickString = data.getStringExtra("nick");
                mNickView.setText(nickString);
                break;
            case 1:
                StringBuffer buffer = new StringBuffer();
                buffer.append(data.getStringExtra("address"))
                        .append("\n")
                        .append(data.getStringExtra("phone"))
                        .append("\n")
                        .append(data.getStringExtra("zone_code"));

                String goodAddressString = buffer.toString();
                mGoodAddress.setText(goodAddressString);
                break;
            case 10:
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                try {
                    final Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                     /* 将Bitmap设定到ImageView */
                    mHeadIconView.setImageBitmap(bitmap);

                    //save icon to server
                    new Thread() {
                        public void run() {
                            super.run();

                            Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
                            int quality = 100;
                            OutputStream stream = null;
                            try {
                                stream = new FileOutputStream(getCacheDir() + "tmp_icon.jpg");
                                bitmap.compress(format, quality, stream);
                                stream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            BmobFile bmobFile = new BmobFile(new File(getCacheDir() + "tmp_icon.jpg"));
                            bmobFile.uploadblock(getApplicationContext(), new UploadFileListener() {

                                @Override
                                public void onSuccess() {
                                    //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                                    Log.d(TAG,"onSuccess");
                                }

                                @Override
                                public void onProgress(Integer value) {
                                    // 返回的上传进度（百分比）
                                }

                                @Override
                                public void onFailure(int code, String msg) {
                                    Log.d(TAG,"onFailure,msg = " + msg);
                                }
                            });
                        }
                    }.start();
                } catch (FileNotFoundException e) {
                    Log.e("Exception", e.getMessage(), e);
                }
                break;
        }
    }

}

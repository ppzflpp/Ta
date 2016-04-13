package com.dragon.ta.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dragon.ta.MainApplication;
import com.dragon.ta.R;
import com.dragon.ta.model.User;

public class ProfileEditActivity extends AppCompatActivity {

    private EditText mNickEditText;
    private EditText mGoodAddressEditText;
    private EditText mPhoneEditText;
    private EditText mZoneCodeEditText;
    private Button mSaveButton;
    private ProgressDialog mProgressDialog;
    private int mEditStyle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        initViews();
    }

    private void initViews(){
        mNickEditText = (EditText)findViewById(R.id.nick_edit_text);
        mGoodAddressEditText = (EditText)findViewById(R.id.good_address_edit_text);
        mPhoneEditText = (EditText)findViewById(R.id.good_address_phone_edit_text);
        mZoneCodeEditText = (EditText)findViewById(R.id.good_address_zone_code_edit_text);
        mSaveButton = (Button)findViewById(R.id.profile_save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        mEditStyle = getIntent().getIntExtra("edit_style",0);
        if(mEditStyle == 0){
            mGoodAddressEditText.setVisibility(View.GONE);
            mPhoneEditText.setVisibility(View.GONE);
            mZoneCodeEditText.setVisibility(View.GONE);
        }else{
            mNickEditText.setVisibility(View.GONE);
        }
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

    private boolean checkData(){
        if(mEditStyle == 0){
            if(mNickEditText.getText() == null ||
                    mNickEditText.getText().toString().length() < 1){
                Toast.makeText(getApplicationContext(),getString(R.string.warning_nick_empty),Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            if(mGoodAddressEditText.getText() == null
                    || mGoodAddressEditText.getText().toString().length() < 1
                    || mPhoneEditText.getText() == null
                    || mPhoneEditText.getText().toString().length() < 1
                    || mZoneCodeEditText.getText() == null
                    || mZoneCodeEditText.getText().toString().length() < 1){
                Toast.makeText(getApplicationContext(),getString(R.string.warning_address_empty),Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void saveData(){
        if(!checkData()){
            return;
        }

        if(mEditStyle == 0) {
            String nickName = mNickEditText.getText().toString();
            new SaveTask(nickName,null,null,null).execute();
        }else{
            String address = mGoodAddressEditText.getText().toString();
            String phone = mPhoneEditText.getText().toString();
            String zoneCode = mZoneCodeEditText.getText().toString();
            new SaveTask(null,address,phone,zoneCode).execute();
        }
    }

    class SaveTask extends AsyncTask<String,String,Integer> {
        private String nickName;
        private String goodAddress;
        private String phone;
        private String zoneCode;
        public SaveTask(String nickName,String goodAddress,String phone,String zoneCode){
            this.nickName = nickName;
            this.goodAddress = goodAddress;
            this.phone = phone;
            this.zoneCode = zoneCode;
        }

        @Override
        protected Integer doInBackground(String... strings) {

            User user = ((MainApplication)getApplication()).getUser();

            if(nickName != null){
                user.setNick(nickName);
            }else if (goodAddress != null) {
                user.setAddress(goodAddress);
                user.setPhone(phone);
                user.setZoneCode(zoneCode);
            }

            user.update(getApplicationContext());

            return 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(getString(R.string.saving));
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            hideDialog();
            if(result == 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                if(mEditStyle == 0) {
                    intent.putExtra("nick", nickName);
                }else{
                    intent.putExtra("address",goodAddress);
                    intent.putExtra("phone",phone);
                    intent.putExtra("zone_code", zoneCode);
                }
                setResult(1, intent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), getString(R.string.save_fail), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

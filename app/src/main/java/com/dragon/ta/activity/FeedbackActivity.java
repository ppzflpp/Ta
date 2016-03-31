package com.dragon.ta.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dragon.ta.R;
import com.dragon.ta.utils.MailSender;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class FeedbackActivity extends AppCompatActivity {

    private static final String SERVER = "smtp.qq.com";
    private static final String USER_NAME = "499360256";
    private static final String PASSWORD = "365zfl592";

    private EditText mPhoneView;
    private EditText mMsgView;
    private Button mSubmitButton;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mPhoneView = (EditText)findViewById(R.id.feed_phone);
        mMsgView = (EditText)findViewById(R.id.feed_message);
        mSubmitButton = (Button)findViewById(R.id.feed_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMsgView.getText() != null) {
                    String msg = mMsgView.getText().toString() + "|||" + mPhoneView.getText();
                    showProgressDialog(true);
                    submit(msg);
                } else {
                    Toast.makeText(FeedbackActivity.this, getString(R.string.msg_empty_warning), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showProgressDialog(boolean show){
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }

        if(!show){
            return;
        }

        mDialog = new ProgressDialog(this);
        mDialog.setTitle(this.getString(R.string.submitting));
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void resetViews(){
        mPhoneView.setText("");
        mMsgView.setText("");
    }


    private void submit(String msg){
        StringBuilder builder = new StringBuilder();
        new SubmitAsyncTask(msg).execute("");
    }



    class SubmitAsyncTask extends AsyncTask<String,String,Boolean>{

        private String msg;

        public SubmitAsyncTask(String msg) {
            super();
            this.msg = msg;
        }


        @Override
        protected Boolean doInBackground(String... params) {
            return MailSender.sendMail("FeedBack",msg);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            showProgressDialog(false);
            if(result) {
                Toast.makeText(FeedbackActivity.this, getString(R.string.submit_success), Toast.LENGTH_SHORT).show();
                resetViews();
            }else{
                Toast.makeText(FeedbackActivity.this, getString(R.string.submit_fail), Toast.LENGTH_SHORT).show();
            }
        }
    }

}

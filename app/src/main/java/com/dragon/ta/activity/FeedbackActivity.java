package com.dragon.ta.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dragon.ta.R;
import com.dragon.ta.model.ApplicationData;
import com.dragon.ta.utils.MailSenderInfo;
import com.dragon.ta.utils.SimpleMailSender;

public class FeedbackActivity extends AppCompatActivity {

    private final static String TAG = "FeedbackActivity";

    private EditText mPhoneView;
    private EditText mMsgView;
    private Button mSubmitButton;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mPhoneView = (EditText) findViewById(R.id.feed_phone);
        mMsgView = (EditText) findViewById(R.id.feed_message);
        mSubmitButton = (Button) findViewById(R.id.feed_submit);
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

    private void showProgressDialog(boolean show) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        if (!show) {
            return;
        }

        mDialog = new ProgressDialog(this);
        mDialog.setTitle(this.getString(R.string.submitting));
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void resetViews() {
        mPhoneView.setText("");
        mMsgView.setText("");
    }


    private void submit(String msg) {
        new SubmitAsyncTask(msg).execute("");
    }


    class SubmitAsyncTask extends AsyncTask<String, String, Boolean> {

        private String msg;

        public SubmitAsyncTask(String msg) {
            super();
            this.msg = msg;
        }


        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;
            try {
                MailSenderInfo mailInfo = new MailSenderInfo();
                mailInfo.setMailServerHost("smtp.qq.com");
                mailInfo.setMailServerPort("25");
                mailInfo.setValidate(true);
                mailInfo.setUserName(ApplicationData.SEND_USER_NAME);  //你的邮箱地址
                mailInfo.setPassword(ApplicationData.SEND_USER_PASSWORD);//您的邮箱密码
                mailInfo.setFromAddress(ApplicationData.SEND_USER_ADDRESS);
                mailInfo.setToAddress(ApplicationData.RECEIVER_USER_ADDRESS);
                mailInfo.setSubject("FeedBack");
                mailInfo.setContent(msg);

                //这个类主要来发送邮件
                SimpleMailSender sms = new SimpleMailSender();
                result = sms.sendTextMail(mailInfo);//发送文体格式
                //sms.sendHtmlMail(mailInfo);//发送html格式

            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            showProgressDialog(false);
            if (result) {
                Toast.makeText(FeedbackActivity.this, getString(R.string.submit_success), Toast.LENGTH_LONG).show();
                resetViews();
            } else {
                Toast.makeText(FeedbackActivity.this, getString(R.string.submit_fail), Toast.LENGTH_SHORT).show();
            }
        }
    }

}

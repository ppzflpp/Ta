package com.dragon.ta.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dragon.ta.R;
import com.dragon.ta.model.User;

import cn.bmob.v3.listener.SaveListener;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {
    private final static String TAG = "RegisterActivity";

    private UserRegisterTask mAuthTask = null;

    // UI references.
    private EditText mPhoneView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private View mProgressView;
    private View mRegisterFormView;

    private static final int MSG_SAVE_SUCCESS = 0;
    private static final int MSG_SAVE_FAIL = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_SAVE_FAIL:
                    Toast.makeText(getApplicationContext(),getString(R.string.save_fail),Toast.LENGTH_SHORT).show();
                    showProgress(false);
                    break;
                case MSG_SAVE_SUCCESS:
                    Toast.makeText(getApplicationContext(),getString(R.string.save_success),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    showProgress(false);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mPhoneView = (EditText) findViewById(R.id.phone);

        mPasswordView = (EditText) findViewById(R.id.register_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.register_confirm_password);


        Button registerButton = (Button) findViewById(R.id.activity_register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);


    }

    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mPhoneView.setError(null);
        mPasswordView.setError(null);
        mConfirmPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mPhoneView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmPassword = mConfirmPasswordView.getText().toString();

        boolean cancel = false;

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_field_required), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isPhoneValid(phone)) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_phone), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isPasswordValid(password)) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_password), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isSameString(password, confirmPassword)) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_invalid_password), Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress(true);
        mAuthTask = new UserRegisterTask(phone, password);
        mAuthTask.execute((Void) null);

    }

    private boolean isPhoneValid(String phone) {
        return phone.length() == 11;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    private boolean isSameString(String str1, String str2) {
        if (str1 != null) {
            return str1.equals(str2);
        }
        return false;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPhone;
        private final String mPassword;

        UserRegisterTask(String phone, String password) {
            mPhone = phone;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            User user = new User();
            user.setUsername(mPhone);
            user.setPhone(mPhone);
            user.setPassword(mPassword);
            user.signUp(getApplicationContext(), new SaveListener() {
                @Override
                public void onSuccess() {
                    mHandler.sendEmptyMessage(MSG_SAVE_SUCCESS);
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.d(TAG, "register,onFailuer,msg is " + s);
                    mHandler.sendEmptyMessage(MSG_SAVE_FAIL);
                }
            });

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}


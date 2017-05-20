package com.wings.intelligentclass.user;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.wings.intelligentclass.utils.GlobalPara;
import com.wings.intelligentclass.api.IUserBiz;
import com.wings.intelligentclass.MainActivity;
import com.wings.intelligentclass.R;
import com.wings.intelligentclass.utils.RetrofitManager;
import com.wings.intelligentclass.domain.LoginInfo;
import com.wings.intelligentclass.domain.User;
import com.wings.intelligentclass.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.account)
    AutoCompleteTextView mAccountView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.email_sign_in_button)
    Button mEmailSignInButton;
    @BindView(R.id.register_button)
    Button mRegisterButton;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        // Set up the login form.
        setTitle("登录");
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mAccountView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String account = mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid account address.
        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        } else if (!isUsernameValid(account)) {
            mAccountView.setError(getString(R.string.error_invalid_username));
            focusView = mAccountView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            IUserBiz iUserBiz = RetrofitManager.getInstance().getIUserBiz();
            Call<LoginInfo> resultCall = iUserBiz.Login(new User(account, password));
            resultCall.enqueue(new Callback<LoginInfo>() {
                @Override
                public void onResponse(Call<LoginInfo> call, Response<LoginInfo> response) {
                    if (response.body() == null || response.code() / 100 != 2) {
                        ToastUtils.showToast(LoginActivity.this, "登录失败");
                        return;
                    }
                    if (response.body().code == 302) {
                        ToastUtils.showToast(LoginActivity.this, response.body().message);
                        return;
                    }
                    ToastUtils.showToast(LoginActivity.this, "login success");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    initGlobalPara(response);
                    showProgress(false);
                    LoginActivity.this.finish();
                }

                @Override
                public void onFailure(Call<LoginInfo> call, Throwable t) {
                    showProgress(false);
                    ToastUtils.showToast(LoginActivity.this, "login failed,please check account and password");
                }
            });
        }
    }

    private void initGlobalPara(Response<LoginInfo> response) {
        GlobalPara.getInstance().account = response.body().account;
        GlobalPara.getInstance().name = response.body().name;
        GlobalPara.getInstance().number = response.body().number;
        GlobalPara.getInstance().description = response.body().description;
        GlobalPara.getInstance().email = response.body().email;

    }

    private boolean isUsernameValid(String email) {
        //TODO: Replace this with your own logic
        return email.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @OnClick(R.id.email_sign_in_button)
    public void onViewClicked() {
    }

    @OnClick({R.id.email_sign_in_button, R.id.register_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.email_sign_in_button:
                attemptLogin();
                break;
            case R.id.register_button:
                enterRegisterActivity();
                break;
        }
    }

    private void enterRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        this.startActivity(intent);
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}


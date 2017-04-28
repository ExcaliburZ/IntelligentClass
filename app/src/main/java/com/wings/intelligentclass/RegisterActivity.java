package com.wings.intelligentclass;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.wings.intelligentclass.domain.Result;
import com.wings.intelligentclass.domain.User;
import com.wings.intelligentclass.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.login_progress)
    ProgressBar mProgressView;
    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;
    @BindView(R.id.username)
    AutoCompleteTextView mUsernameView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.phone)
    AutoCompleteTextView mPhoneView;
    @BindView(R.id.description)
    MultiAutoCompleteTextView mDescriptionView;
    @BindView(R.id.email_sign_in_button)
    Button mEmailSignInButton;
    @BindView(R.id.email_login_form)
    LinearLayout mEmailLoginForm;
    @BindView(R.id.login_form)
    ScrollView mLoginFormView;
    @BindView(R.id.password_repeat)
    EditText mPasswordRepeatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.email_sign_in_button)
    public void onViewClicked() {
        attemptRegister();
    }

    private void attemptRegister() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mPhoneView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordRepeat = mPasswordRepeatView.getText().toString();
        String phone = mPhoneView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(phone) || !isPhoneValid(phone)) {
            mPhoneView.setError(getString(R.string.error_invalid_phone));
            focusView = mPhoneView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!TextUtils.equals(password, passwordRepeat)) {
            mPasswordView.setError(getString(R.string.error_invalid_password_repeat));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user register attempt.
            showProgress(true);
            User user = new User(username,
                    password,
                    mEmailView.getText().toString(),
                    phone,
                    mDescriptionView.getText().toString());

            IUserBiz iUserBiz = RetrofitManager.getInstance().getIUserBiz();
            Call<Result> resultCall = iUserBiz.registerUser(user);
            resultCall.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Result result = response.body();
                    if (result.code / 100 == 2) {
                        showProgress(false);
                        ToastUtils.showToast(RegisterActivity.this, "Register success");
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        RegisterActivity.this.startActivity(intent);
                        RegisterActivity.this.finish();
                    } else {
                        showProgress(false);
                        ToastUtils.showToast(RegisterActivity.this, "Register failed");
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    ToastUtils.showToast(RegisterActivity.this, "Register failed");
                }
            });

        }
    }

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

    private boolean isPhoneValid(String phone) {
        return phone.length() > 4;
    }

    private boolean isUsernameValid(String email) {
        //TODO: Replace this with your own logic
        return email.length() > 6;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}

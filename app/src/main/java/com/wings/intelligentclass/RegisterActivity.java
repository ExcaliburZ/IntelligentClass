package com.wings.intelligentclass;

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

import com.google.gson.Gson;
import com.wings.intelligentclass.domain.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.login_progress)
    ProgressBar mLoginProgress;
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
    ScrollView mLoginForm;
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
            //showProgress(true);
            //TODO
            User user = new User(username,
                    password,
                    mEmailView.getText().toString(),
                    phone,
                    mDescriptionView.getText().toString());
            Gson gson = new Gson();
            String userJson = gson.toJson(user);
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
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

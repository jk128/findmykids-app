package com.thesis.findmykidsparents.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.thesis.findmykidsparents.MyApplication;
import com.thesis.findmykidsparents.R;
import com.thesis.findmykidsparents.entity.AuthLogged;
import com.thesis.findmykidsparents.main.MainActivity;
import com.thesis.findmykidsparents.utils.ApiResponse;
import com.thesis.findmykidsparents.utils.Constant;
import com.thesis.findmykidsparents.utils.SessionManager;
import com.thesis.findmykidsparents.utils.ViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.Nullable;

public class LoginActivity extends AppCompatActivity {
    @Inject
    ViewModelFactory viewModelFactory;

    @BindView(R.id.email_layout)
    TextInputLayout emailLayout;

    @BindView(R.id.passWord_layout)
    TextInputLayout passWordLayout;

    @BindView(R.id.email)
    TextInputEditText email;

    @BindView(R.id.passWord)
    TextInputEditText passWord;

    LoginViewModel viewModel;

    ProgressDialog progressDialog;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        progressDialog = Constant.getProgressDialog(this, getString(R.string.process));

        // Session Manager
        session = new SessionManager(getApplicationContext());

        ButterKnife.bind(this);
        ((MyApplication) getApplication()).getAppComponent().doInjection(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);

        viewModel.loginResponse().observe(this, this::consumeResponse);
    }

    /**
     * Đăng nhập
     */
    @OnClick(R.id.login)
    void onLoginClicked() {
        if (isValid()) {
            if (!Constant.checkInternetConnection(this)) {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            } else {
                viewModel.hitLoginApi(email.getText().toString(), passWord.getText().toString());
            }
        }
    }

    /*
     * method to validate $(mobile number) and $(password)
     * */
    private boolean isValid() {
        Boolean isValid = true;

        if (this.email.getText().toString().trim().isEmpty()) {
            String error =
                    !TextUtils.isEmpty(email.getText())
                            ? String.valueOf(email.getText())
                            : getResources().getString(R.string.required_email_error);
            emailLayout.setError(error);
            emailLayout.hasFocus();
            isValid = false;
        } else {
            emailLayout.setError("");
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            String error = getResources().getString(R.string.valid_email_error);
            emailLayout.setError(error);
            emailLayout.hasFocus();
            isValid = false;
        } else {
            emailLayout.setError("");
        }

        if (this.passWord.getText().toString().trim().isEmpty()) {
            String error =
                    !TextUtils.isEmpty(passWord.getText())
                            ? String.valueOf(passWord.getText())
                            : getResources().getString(R.string.required_password_error);
            passWordLayout.setError(error);
            passWordLayout.hasFocus();
            isValid = false;
        } else {
            passWordLayout.setError("");
        }

        return isValid;
    }

    /*
     * method to handle response
     * */
    private void consumeResponse(ApiResponse apiResponse) {

        switch (apiResponse.status) {

            case LOADING:
                progressDialog.show();
                break;

            case SUCCESS:
                progressDialog.dismiss();
                renderSuccessResponse((AuthLogged) apiResponse.data);
                break;

            case ERROR:
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    private void renderSuccessResponse(AuthLogged auth) {
        if (auth.authenticated) {
            String welcome = "Xin chào, " + auth.name;
            // TODO : initiate successful logged in experience
            Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
            session.createLoginSession(auth);

            // Staring MainActivity
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
            finish();
        } else {
            String welcome = "Đăng nhập không thành công!";
            // TODO : initiate successful logged in experience
            Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }
    }
}

package com.thesis.findmykidsparents.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
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

    @BindView(R.id.txtShowEmail)
    TextView txtShowEmail;

    @BindView(R.id.txtShowPassword)
    TextView txtShowPassword;

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
        session = SessionManager.getInstance(null);

        ButterKnife.bind(this);
        ((MyApplication) getApplication()).getAppComponent().doInjection(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);

        viewModel.loginResponse().observe(this, this::consumeResponse);

        passWord.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(4)});
    }

    /**
     * Đăng nhập
     */
    @OnClick(R.id.login)
    void onLoginClicked() {
        if (!Constant.checkInternetConnection(this)) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        } else {

            if (passWordLayout.getVisibility() == View.GONE) {
                if (isValid()) {
                    viewModel.hitRegisterLoginApi(email.getText().toString(), passWord.getText().toString());
                }
            }
            else {
                if (isValidPassword()) {
                    viewModel.hitLoginFinishApi(email.getText().toString(), passWord.getText().toString());
                }
            }
        }
    }

    @OnClick(R.id.reEmail)
    void OnReEmail() {
        emailLayout.setError("");
        passWordLayout.setError("");
        txtShowEmail.setVisibility(View.VISIBLE);
        txtShowPassword.setVisibility(View.GONE);
        emailLayout.setVisibility(View.VISIBLE);
        passWordLayout.setVisibility(View.GONE);
        email.setText("");
        passWord.setText("");
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

        return isValid;
    }


    /*
     * method to validate $(mobile number) and $(password)
     * */
    private boolean isValidPassword() {
        Boolean isValid = true;

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
                if (passWord.getText().toString().equals("")) {
                    passWordLayout.setVisibility(View.VISIBLE);
                    emailLayout.setVisibility(View.GONE);
                    txtShowEmail.setVisibility(View.GONE);
                    txtShowPassword.setVisibility(View.VISIBLE);
                } else {
                    renderSuccessResponse((AuthLogged) apiResponse.data);
                }
                break;

            case ERROR:
                progressDialog.dismiss();
                Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
                break;

            default:
                break;
        }
    }

    private void renderSuccessResponse(AuthLogged auth) {
        if (auth.authenticated) {
            String welcome = "Xin chào, " + auth.id;
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
            Toast toast = Toast.makeText(LoginActivity.this, welcome, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }
}

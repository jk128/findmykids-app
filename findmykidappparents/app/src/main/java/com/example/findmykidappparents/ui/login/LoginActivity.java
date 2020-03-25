package com.example.findmykidappparents.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.findmykidappparents.MainActivity;
import com.example.findmykidappparents.R;
import com.example.findmykidappparents.data.Result;
import com.example.findmykidappparents.data.model.LoggedInUser;
import com.example.findmykidappparents.sessions.SessionManager;
import com.example.findmykidappparents.ui.login.LoginViewModel;
import com.example.findmykidappparents.ui.login.LoginViewModelFactory;
import com.example.findmykidappparents.utils.API;
import com.example.findmykidappparents.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    // Session Manager Class
    SessionManager session;


    EditText usernameEditText;
    EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

//        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
//            @Override
//            public void onChanged(@Nullable LoginFormState loginFormState) {
//                if (loginFormState == null) {
//                    return;
//                }
//                loginButton.setEnabled(loginFormState.isDataValid());
//                if (loginFormState.getUsernameError() != null) {
//                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
//                }
//                if (loginFormState.getPasswordError() != null) {
//                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
//                }
//            }
//        });

//        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
//            @Override
//            public void onChanged(@Nullable LoginResult loginResult) {
//                if (loginResult == null) {
//                    return;
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//                if (loginResult.getError() != null) {
//                    showLoginFailed(loginResult.getError());
//                }
//                if (loginResult.getSuccess() != null) {
//                    updateUiWithUser(loginResult.getSuccess());
//                }
//                setResult(Activity.RESULT_OK);
//
//                //Complete and destroy login activity once successful
//                finish();
//            }
//        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
//                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    loginViewModel.login(usernameEditText.getText().toString(),
//                            passwordEditText.getText().toString());
//                }
//                return false;
//            }
//        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                if (usernameEditText.getText().toString().trim().equals("") ||
                        passwordEditText.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Hãy điền tên đăng nhập và mật khẩu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Login();
            }
        });
    }

    LoggedInUser loggedInUser = null;
    private void Login() {
        try {
            String url = API.HOST_LOGIN + "members/login";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean authenticated = (Boolean) response.get("authenticated");

                                if (authenticated) {
                                    String Id = (String) response.get("id");
                                    String name = (String) response.get("name");
                                    String AccessToken = (String) response.get("accessToken");
                                    String RefreshToken = (String) response.get("refreshToken");
                                    loggedInUser = new LoggedInUser(Id, name, authenticated, AccessToken, RefreshToken);


                                    String welcome = getString(R.string.welcome) + name;
                                    // TODO : initiate successful logged in experience
                                    Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

                                    // Creating user login session
                                    // For testing i am stroing name, email as follow
                                    // Use user real data
                                    session.createLoginSession(Id,
                                            name, true,
                                            AccessToken,
                                            RefreshToken);

                                    // Staring MainActivity
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    // Creating user login session
                                    // For testing i am stroing name, email as follow
                                    // Use user real data
                                    session.createLoginSession("Id",
                                            "name", false,
                                            "AccessToken",
                                            "RefreshToken");
                                    Toast.makeText(getApplicationContext(), "Thông tin đăng nhập không đúng!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                        }
                    }) {
                @Override
                public byte[] getBody() {
                    JSONObject jsonBodyObj = new JSONObject();
                    try {
                        jsonBodyObj.put("UserName", usernameEditText.getText().toString());
                        jsonBodyObj.put("PassWord", passwordEditText.getText().toString());
                    } catch (
                            JSONException e) {
                        e.printStackTrace();
                    }
                    String requestBody = jsonBodyObj.toString();  //The request body goes in here.

                    try {
                        return requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };
            jsonObjectRequest.setTag("LOGIN");
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().cancelAll("LOGIN");
    }

    //    private void updateUiWithUser(LoggedInUserView model) {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
//        // TODO : initiate successful logged in experience
//        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//
//        // Creating user login session
//        // For testing i am stroing name, email as follow
//        // Use user real data
//        session.createLoginSession(model.getUserId(),
//                model.getDisplayName(), model.getAuthenticated(),
//                model.getAccessToken(), model.getRefreshToken());
//
//        // Staring MainActivity
//        Intent i = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(i);
//        finish();
//    }

//    private void showLoginFailed(@StringRes Integer errorString) {
//        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
//    }
}

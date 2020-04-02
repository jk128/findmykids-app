package com.thesis.findmykidschildren.connect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.thesis.findmykidschildren.R;
import com.thesis.findmykidschildren.entity.MyParents;
import com.thesis.findmykidschildren.entity.Parent;
import com.thesis.findmykidschildren.utils.ApiResponse;
import com.thesis.findmykidschildren.utils.Constant;
import com.thesis.findmykidschildren.utils.DataStore;
import com.thesis.findmykidschildren.utils.Repository;
import com.thesis.findmykidschildren.utils.SessionManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ConnectViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> responseLiveDataReport = new MutableLiveData<>();
    TextInputEditText email;
    TextInputEditText passWord;
    TextInputEditText name;
    Button btn_connect;
    TextInputLayout email_layout;
    TextInputLayout passWord_layout;
    TextInputLayout name_layout;
    // Session Manager Class
    SessionManager session;
    private Repository repository;
    private Context context;

    public ConnectViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> connectResponse() {
        return responseLiveData;
    }

    public MutableLiveData<ApiResponse> reportLocationResponse() {
        return responseLiveDataReport;
    }

    public void setContext(Context context) {
        this.context = context;
        // Session Manager
        session = new SessionManager(context);
    }

    public void setEmail(TextInputEditText email) {
        this.email = email;
    }

    public void setPassWord(TextInputEditText passWord) {
        this.passWord = passWord;
    }

    public void setEmail_layout(TextInputLayout email_layout) {
        this.email_layout = email_layout;
    }

    public void setPassWord_layout(TextInputLayout passWord_layout) {
        this.passWord_layout = passWord_layout;
    }

    public void setName(TextInputEditText name) {
        this.name = name;
    }

    public void setName_layout(TextInputLayout name_layout) {
        this.name_layout = name_layout;
    }

    public void setBtn_connect(Button btn_connect) {
        this.btn_connect = btn_connect;

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage("Thêm mới Ba/Mẹ?")
                            .setTitle("Xác nhận");

                    // Add the buttons
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (!Constant.checkInternetConnection(context)) {
                                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            } else {
                                hitConnectApi();
                            }
                        }
                    });

                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.cancel();
                        }
                    });

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    public void SaveParent() {
        Gson gson = new Gson();
        DataStore dataStore = new DataStore(context);
        MyParents myParents;
        String my_parents = dataStore.GetData("my_parents");
        if (my_parents == null) {
            myParents = new MyParents();
        } else {
            myParents = gson.fromJson(my_parents, MyParents.class);
        }

        myParents.parents.add(new Parent(name.getText().toString(),
                email.getText().toString()));

        dataStore.StoreData("my_parents", gson.toJson(myParents));
    }

    /*
     * method to validate $(mobile number) and $(password)
     * */
    private boolean isValid() {
        Boolean isValid = true;

        if (this.name.getText().toString().trim().isEmpty()) {
            String error =
                    !TextUtils.isEmpty(name.getText())
                            ? String.valueOf(name.getText())
                            : context.getResources().getString(R.string.required_name_error);
            name_layout.setError(error);
            name_layout.hasFocus();
            isValid = false;
        } else {
            name_layout.setError("");
        }

        if (this.email.getText().toString().trim().isEmpty()) {
            String error =
                    !TextUtils.isEmpty(email.getText())
                            ? String.valueOf(email.getText())
                            : context.getResources().getString(R.string.required_email_error);
            email_layout.setError(error);
            email_layout.hasFocus();
            isValid = false;
        } else {
            email_layout.setError("");
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            String error = context.getResources().getString(R.string.valid_email_error);
            email_layout.setError(error);
            email_layout.hasFocus();
            isValid = false;
        } else {
            email_layout.setError("");
        }

        if (this.passWord.getText().toString().trim().isEmpty()) {
            String error =
                    !TextUtils.isEmpty(passWord.getText())
                            ? String.valueOf(passWord.getText())
                            : context.getResources().getString(R.string.required_password_error);
            passWord_layout.setError(error);
            passWord_layout.hasFocus();
            isValid = false;
        } else {
            passWord_layout.setError("");
        }

        return isValid;
    }

    /*
     * method to call normal login api with $(email + password)
     * */
    public void hitConnectApi() {
        String email = this.email.getText().toString();
        String passWord = this.passWord.getText().toString();

        Gson gson = new Gson();
        DataStore dataStore = new DataStore(context);
        MyParents myParents;
        double lat = -1.0;
        double lon = -1.0;
        String strMyLocationInit = dataStore.GetData(context.getResources().getString(R.string.myLocationInit));
        if (strMyLocationInit != null) {
            Location location = gson.fromJson(strMyLocationInit, Location.class);
            lat = location.getLatitude();
            lon = location.getLongitude();
        }

        disposables.add(repository.executeConncect(email, passWord, lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}

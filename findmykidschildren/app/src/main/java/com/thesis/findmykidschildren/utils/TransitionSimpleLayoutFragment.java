package com.thesis.findmykidschildren.utils;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.thesis.findmykidschildren.MyApplication;
import com.thesis.findmykidschildren.Parent.ParentViewModel;
import com.thesis.findmykidschildren.R;
import com.thesis.findmykidschildren.connect.ConnectViewModel;
import com.thesis.findmykidschildren.entity.AuthLogged;
import com.thesis.findmykidschildren.entity.MyParents;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class TransitionSimpleLayoutFragment extends Fragment {
    private static final String KEY_LAYOUT_RES_ID = "KEY_LAYOUT_RES_ID";
    @Inject
    ViewModelFactory viewModelFactory;
    ProgressDialog progressDialog;
    int layoutResId;

    ConnectViewModel connectViewModel;
    ParentViewModel parentViewModel;

    // Session Manager Class
    SessionManager session;

    public static TransitionSimpleLayoutFragment newInstance(@LayoutRes int layoutResId) {
        TransitionSimpleLayoutFragment fragment = new TransitionSimpleLayoutFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT_RES_ID, layoutResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Bundle args = getArguments();
        if (args != null) {
            layoutResId = args.getInt(KEY_LAYOUT_RES_ID);
        }

        progressDialog = Constant.getProgressDialog(getContext(), getString(R.string.process));
        ButterKnife.bind(getActivity());
        ((MyApplication) getActivity().getApplication()).getAppComponent().doInjection(this);

        switch (layoutResId) {
            case R.layout.connect_activity:
                connectViewModel = ViewModelProviders.of(this, viewModelFactory).get(ConnectViewModel.class);
                connectViewModel.connectResponse().observe(this, this::consumeResponse);
                connectViewModel.reportLocationResponse().observe(this, this::reportLocationResponse);
                break;
            case R.layout.parent_activity:
                parentViewModel = ViewModelProviders.of(this, viewModelFactory).get(ParentViewModel.class);
                parentViewModel.parentResponse().observe(this, this::consumeResponse);
                break;
        }
    }

    /**
     * Nhận thông báo trả về khi gọi API
     *
     * @param apiResponse
     */
    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {

            case LOADING:
                progressDialog.show();
                break;

            case SUCCESS:
                progressDialog.dismiss();
                renderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                progressDialog.dismiss();
                break;

            default:
                break;
        }
    }

    private void reportLocationResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {

            case LOADING:
                progressDialog.show();
                break;

            case SUCCESS:
                progressDialog.dismiss();
                break;

            case ERROR:
                progressDialog.dismiss();
                break;

            default:
                break;
        }
    }

    /**
     * Nhận kết quả trả về của API
     *
     * @param response
     * @param <T>
     */
    private <T> void renderSuccessResponse(T response) {
        switch (layoutResId) {
            case R.layout.connect_activity:
                if (!session.isLoggedIn()) {
                    session.createLoginSession((AuthLogged) response);
                }
                // Lưu thông tin cha mẹ vào danh sách
                connectViewModel.SaveParent();
                break;
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View view = layoutInflater.inflate(layoutResId, viewGroup, false);
        // Session Manager
        session = new SessionManager(view.getContext());
        switch (layoutResId) {
            case R.layout.connect_activity:
                connectViewModel.setContext(view.getContext());
                connectViewModel.setBtn_connect(view.findViewById(R.id.btn_connect));
                connectViewModel.setEmail(view.findViewById(R.id.email));
                connectViewModel.setPassWord(view.findViewById(R.id.passWord));
                connectViewModel.setEmail_layout(view.findViewById(R.id.email_layout));
                connectViewModel.setPassWord_layout(view.findViewById(R.id.passWord_layout));
                connectViewModel.setName(view.findViewById(R.id.name));
                connectViewModel.setName_layout(view.findViewById(R.id.name_layout));
                break;
            case R.layout.parent_activity:
                parentViewModel.setContext(view.getContext());
                break;
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (layoutResId) {
            case R.layout.parent_activity:
                LoadParentRunnable runnable = new LoadParentRunnable(view);
                new Thread(runnable).start();
                break;
        }
    }

    class LoadParentRunnable implements Runnable {
        View view;

        LoadParentRunnable(View view) {
            this.view = view;
        }

        @Override
        public void run() {
            Handler threadHandler = new Handler(Looper.getMainLooper());
            threadHandler.post(new Runnable() {
                @Override
                public void run() {
                    RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
                    if (mRecyclerView != null) {
                        mRecyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity()));
                        MyParents myParents = parentViewModel.GetMyParents();
                        if (myParents != null) {
                            mRecyclerView.setAdapter(new RVAdapter(myParents.parents, getContext()));
                        }
                    }
                }
            });
        }
    }
}
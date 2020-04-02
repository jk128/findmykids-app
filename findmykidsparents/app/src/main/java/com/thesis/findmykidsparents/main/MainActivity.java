package com.thesis.findmykidsparents.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.transition.MaterialFadeThrough;
import com.thesis.findmykidsparents.MyApplication;
import com.thesis.findmykidsparents.R;
import com.thesis.findmykidsparents.entity.Auth;
import com.thesis.findmykidsparents.utils.ApiResponse;
import com.thesis.findmykidsparents.utils.ChildrenListener;
import com.thesis.findmykidsparents.utils.Constant;
import com.thesis.findmykidsparents.utils.SessionManager;
import com.thesis.findmykidsparents.utils.TransitionSimpleLayoutFragment;
import com.thesis.findmykidsparents.utils.ViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class MainActivity extends AppCompatActivity implements ChildrenListener {
    private static final SparseIntArray LAYOUT_RES_MAP = new SparseIntArray();

    static {
        LAYOUT_RES_MAP.append(R.id.action_childrens, R.layout.children_activity);
        LAYOUT_RES_MAP.append(R.id.action_mapDetail, R.layout.detail_activity);
        LAYOUT_RES_MAP.append(R.id.action_addChildren, R.layout.password_activity);
        LAYOUT_RES_MAP.append(R.id.action_search, R.layout.cat_transition_fade_through_search_fragment);
    }

    @Inject
    ViewModelFactory viewModelFactory;
    MainViewModel viewModel;
    ProgressDialog progressDialog;
    @BindView(R.id.bottomnavigation)
    BottomNavigationView bottomNavigationView;

    // Session Manager Class
    SessionManager session;

    @LayoutRes
    private static int getLayoutForItemId(@IdRes int itemId) {
        return LAYOUT_RES_MAP.get(itemId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        progressDialog = Constant.getProgressDialog(this, getString(R.string.process));

        ButterKnife.bind(this);
        ((MyApplication) getApplication()).getAppComponent().doInjection(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        viewModel.loginResponse().observe(this, this::consumeResponse);

        replaceFragment(R.id.action_childrens);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        progressDialog.show();
                        final int previousItem = bottomNavigationView.getSelectedItemId();
                        final int nextItem = item.getItemId();
                        if (previousItem != nextItem) {
                            replaceFragment(item.getItemId());
                        }
                        progressDialog.dismiss();
                        return true;
                    }
                }
        );
        // #02
        session.checkLogin();
    }

    private void replaceFragment(@IdRes int itemId) {
        Fragment fragment = TransitionSimpleLayoutFragment.newInstance(getLayoutForItemId(itemId));
        ((TransitionSimpleLayoutFragment) fragment).RegisterChildrenListener(this);
        fragment.setEnterTransition(createTransition());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private MaterialFadeThrough createTransition() {
        MaterialFadeThrough fadeThrough = MaterialFadeThrough.create(this);
        fadeThrough.addTarget(R.id.children_activity);
        fadeThrough.addTarget(R.id.detail_activity);
        fadeThrough.addTarget(R.id.password_activity);
        fadeThrough.addTarget(R.id.search_fragment);

        return fadeThrough;
    }

    private void consumeResponse(ApiResponse apiResponse) {

        switch (apiResponse.status) {

            case LOADING:
                progressDialog.show();
                break;

            case SUCCESS:
                progressDialog.dismiss();
                renderSuccessResponse((Auth) apiResponse.data);
                break;

            case ERROR:
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    private void renderSuccessResponse(Auth response) {

    }

    @Override
    public void ChooseChildren() {
        replaceFragment(R.id.action_mapDetail);
        bottomNavigationView.setSelectedItemId(R.id.action_mapDetail);
    }
}

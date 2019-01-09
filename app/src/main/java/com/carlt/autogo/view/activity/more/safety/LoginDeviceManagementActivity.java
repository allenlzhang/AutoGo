package com.carlt.autogo.view.activity.more.safety;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.LoginLogItemAdapter;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.entry.user.LoginLogInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.presenter.safety.ILoginLogView;
import com.carlt.autogo.presenter.safety.LoginLogPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2018/9/13.
 */
@CreatePresenter(presenter = LoginLogPresenter.class)
public class LoginDeviceManagementActivity extends BaseMvpActivity<LoginLogPresenter> implements ILoginLogView{
    @BindView(R.id.rvList)
    RecyclerView       rvList;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;

    private int limit = 20;

    private int offset = 0;

    private boolean isLoadMore = false;

    LoginLogItemAdapter itemAdapter;
    @Override
    protected int getContentView() {
        return R.layout.activity_login_device_management;
    }

    @SuppressLint("CheckResult")
    @Override
    public void init() {
        rvList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        setTitleText(getResources().getString(R.string.management_login_device));
        getPresenter().loginLog(20,0);
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isLoadMore = true;
                loadMoreLoginLog();
            }
        });
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isLoadMore = false;
                onRefreshLoginLog();
            }
        });
    }

    @Override
    public void loginLogSuccess(LoginLogInfo info) {
        if (info.logs == null){
            mSmartRefreshLayout.finishRefresh();
            mSmartRefreshLayout.finishLoadMore();
            return;
        }
        if (itemAdapter == null){
            itemAdapter = new LoginLogItemAdapter(info.logs);
            rvList.setAdapter(itemAdapter);
            mSmartRefreshLayout.finishRefresh();
        }else {
            if (isLoadMore){
                offset += info.logs.size();
                itemAdapter.addData(info.logs);
                mSmartRefreshLayout.finishLoadMore();
            }else {
                offset = info.logs.size();
                itemAdapter.setNewData(info.logs);
                mSmartRefreshLayout.finishRefresh();
            }
        }

    }
    private void onRefreshLoginLog(){
        offset = 0;
        getPresenter().loginLog(limit,offset);
    }
    private void loadMoreLoginLog(){
        getPresenter().loginLog(limit,offset);
    }
}

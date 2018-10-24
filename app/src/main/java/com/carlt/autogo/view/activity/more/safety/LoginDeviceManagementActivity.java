package com.carlt.autogo.view.activity.more.safety;

import android.annotation.SuppressLint;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.LoginLogItemAdapter;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.user.LoginLogInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2018/9/13.
 */
public class LoginDeviceManagementActivity extends BaseMvpActivity {
    @BindView(R.id.rvList)
    RecyclerView       rvList;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;

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
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", 100);
        map.put("offset", 0);
        dialog.show();
        ClientFactory.def(UserService.class).loginLog(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginLogInfo>() {
                    @Override
                    public void accept(LoginLogInfo info) throws Exception {
                        dialog.dismiss();
                        LogUtils.e("----" + info);
                        LoginLogItemAdapter itemAdapter = new LoginLogItemAdapter(info.logs);
                        rvList.setAdapter(itemAdapter);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.show();
                        LogUtils.e("----" + throwable);
                    }
                });
    }
}

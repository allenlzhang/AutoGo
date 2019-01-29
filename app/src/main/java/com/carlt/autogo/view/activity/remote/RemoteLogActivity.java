package com.carlt.autogo.view.activity.remote;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.carlt.autogo.R;
import com.carlt.autogo.adapter.RemoteLogAdapter;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.entry.remote.RemoteLogInfo;
import com.carlt.autogo.presenter.remote.IRemoteLogView;
import com.carlt.autogo.presenter.remote.RemoteLogPresenter;

import butterknife.BindView;

/**
 * Created by Marlon on 2019/1/17.
 */
@CreatePresenter(presenter = RemoteLogPresenter.class)
public class RemoteLogActivity extends BaseMvpActivity<RemoteLogPresenter> implements IRemoteLogView {
    @BindView(R.id.rvRemoteLog)
    RecyclerView rvRemoteLog;
    RemoteLogAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_remote_log;
    }

    @Override
    public void init() {
        setTitleText("远程操作记录");
        rvRemoteLog.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RemoteLogActivity.this);
        rvRemoteLog.setLayoutManager(linearLayoutManager);
        getPresenter().remoteLog();
    }

    @Override
    public void getRemoteLogSuccess(RemoteLogInfo info) {
        if (info != null) {
            adapter = new RemoteLogAdapter(info.getData().getList());
            rvRemoteLog.setAdapter(adapter);
        }
    }

}

package com.carlt.autogo.view.activity.more.safety;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LogoutNoticeActivity extends BaseMvpActivity {

    @BindView(R.id.cb_law)
    CheckBox cbLaw;

    @Override
    protected int getContentView() {
        return R.layout.activity_loginout_notice;
    }

    @Override
    public void init() {
        setTitleText("注销须知");
    }

    @OnClick({R.id.tv_law, R.id.btnNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_law:
                break;
            case R.id.btnNext:
                if (!cbLaw.isChecked()) {
                    showToast("请同意注销协议");
                    return;
                }
                startActivity(new Intent(this, LogoutAccountActivityFirst.class));
                finish();
                break;
        }
    }
}

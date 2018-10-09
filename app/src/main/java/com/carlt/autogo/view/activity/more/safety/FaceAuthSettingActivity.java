package com.carlt.autogo.view.activity.more.safety;

import android.content.Intent;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.view.activity.login.FaceLoginActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class FaceAuthSettingActivity extends BaseMvpActivity {


    @BindView(R.id.tvCollectFace)
    TextView tvCollectFace;

    @Override
    protected int getContentView() {
        return R.layout.activity_face_auth_setting;
    }

    @Override
    public void init() {
        setTitleText("身份认证");
    }


    @OnClick(R.id.tvCollectFace)
    public void onViewClicked() {
        Intent intent = new Intent(this, FaceLoginActivity.class);
        intent.putExtra(GlobalKey.FROM_ACTIVITY, FaceLoginActivity.FROM_ALIPAY_AUTH);
        startActivity(intent);
    }
}

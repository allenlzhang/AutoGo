package com.carlt.autogo.view.activity.more.safety;

import android.content.Intent;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.view.activity.login.FaceLiveCheckActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class FaceAuthSettingActivity extends BaseMvpActivity {
    public static final int From_ID_Card     = 11;
    public static final int From_ALiPay_Auth = 12;

    @BindView(R.id.tvCollectFace)
    TextView tvCollectFace;
    private int isFrom;

    @Override
    protected int getContentView() {
        return R.layout.activity_face_auth_setting;
    }

    @Override
    public void init() {
        setTitleText("身份认证");
        isFrom = getIntent().getIntExtra(GlobalKey.FROM_ACTIVITY, 0);
    }


    @OnClick(R.id.tvCollectFace)
    public void onViewClicked() {
        switch (isFrom) {
            case From_ID_Card:
                String hideName = getIntent().getStringExtra("hideName");
                Intent intent1 = new Intent(this, FaceLiveCheckActivity.class);
                intent1.putExtra(GlobalKey.FROM_ACTIVITY, FaceLiveCheckActivity.FROM_ID_CARDACCEPT_ACTIVITY);
                intent1.putExtra("hideName", hideName);
                startActivity(intent1);
                break;
            case From_ALiPay_Auth:
                Intent intent = new Intent(this, FaceLiveCheckActivity.class);
                intent.putExtra(GlobalKey.FROM_ACTIVITY, FaceLiveCheckActivity.FROM_ALIPAY_AUTH);
                startActivity(intent);
                break;
            default:
                String hideName3 = getIntent().getStringExtra("hideName");
                Intent intent3 = new Intent(this, FaceLiveCheckActivity.class);
                intent3.putExtra(GlobalKey.FROM_ACTIVITY, FaceLiveCheckActivity.FROM_ID_CARDACCEPT_ACTIVITY);
                intent3.putExtra("hideName", hideName3);
                startActivity(intent3);
                break;
        }

        finish();
    }
}

package com.carlt.autogo.view.activity.user.accept;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.utils.ActivityControl;
import com.carlt.autogo.utils.SharepUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wsq
 */
public class IdfCompleteActivity extends BaseMvpActivity {


    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_user_name)
    TextView  tvUserName;
    @BindView(R.id.btn_back)
    Button    btnBack;
    @BindView(R.id.tv)
    TextView  tv;
    private boolean isByIdcard = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_idf_complete;
    }

    @Override
    public void init() {
        setTitleText("身份认证");
        isByIdcard = getIntent().getBooleanExtra("idcard", false);

        if (isByIdcard) {
            img.setImageDrawable(getResources().getDrawable(R.mipmap.accepted_by_idcard));
            String name = SharepUtil.getPreferences().getString(GlobalKey.ID_CARD_NAME, "");
            tvUserName.setText(name);
            tvUserName.setVisibility(View.VISIBLE);
        } else {
            img.setImageDrawable(getResources().getDrawable(R.mipmap.accepted_by_payment));
            tvUserName.setVisibility(View.GONE);
        }

    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        //        Intent intent =new Intent(this, SafetyActivity.class);
        //        startActivity(intent);

        finish();

    }

    @Override
    public void finish() {
        for (Activity activity : ActivityControl.mActivityList) {
            if (activity instanceof UserIdChooseActivity) {
                activity.finish();
            } else if (activity instanceof UploadIdCardPhotoActivity2) {
                activity.finish();
            }

        }
        super.finish();
    }
}

package com.carlt.autogo.view.activity.user.accept;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.entry.user.UserIdentity;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.presenter.safety.IIdentityView;
import com.carlt.autogo.presenter.safety.IdentityPresenter;
import com.carlt.autogo.utils.ActivityControl;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * @author wsq
 */
@CreatePresenter(presenter = IdentityPresenter.class)
public class IdfCompleteActivity extends BaseMvpActivity<IdentityPresenter> implements IIdentityView{


    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_user_name)
    TextView  tvUserName;
    @BindView(R.id.btn_back)
    Button    btnBack;
    @BindView(R.id.tv)
    TextView  tv;
    private boolean isByIdcard;

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
//            String name = SharepUtil.getPreferences().getString(GlobalKey.ID_CARD_NAME, "");
//            tvUserName.setText(name);
            tvUserName.setVisibility(View.VISIBLE);
        } else {
            img.setImageDrawable(getResources().getDrawable(R.mipmap.accepted_by_payment));
            tvUserName.setVisibility(View.GONE);
        }
        getPresenter().getIdentity();
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

    private String encrypt(String str) {
        StringBuilder n = new StringBuilder();
        int len = str.length();
        if (len <= 2) {
            n.append( str.charAt(0)+"*");
            return n.toString();
        }
        n.append(str.charAt(0));
        for (int i = 1; i <= len - 2; i++) {
            n.append("*");
        }
        n.append(str.charAt(len - 1));
        return n.toString();
    }

    @Override
    public void getIdentity(UserIdentity userIdentity) {
        if (userIdentity.err!=null){
            ToastUtils.showShort(userIdentity.err.msg);
        }else {
            tvUserName.setText(encrypt(userIdentity.name));
        }
    }
}

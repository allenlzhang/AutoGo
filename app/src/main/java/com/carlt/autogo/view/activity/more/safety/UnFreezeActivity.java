package com.carlt.autogo.view.activity.more.safety;

import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.presenter.safety.FreezePresenter;
import com.carlt.autogo.presenter.safety.IFreezeView;
import com.carlt.autogo.utils.ActivityControl;
import com.carlt.autogo.utils.SharepUtil;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wsq
 * @time 16:52  2018/9/25/025
 * @describe 解冻账户 操作Activity
 */
@CreatePresenter(presenter = FreezePresenter.class)
public class UnFreezeActivity extends BaseMvpActivity<FreezePresenter> implements IFreezeView{

    /**
     * 用户名
     */
    @BindView(R.id.tv_user_unfreeze)
    TextView tvUserUnfreeze;
    /**
     * 密码
     */
    @BindView(R.id.ed_unfreeze_pwd)
    EditText edUnfreezePwd;
    /**
     * 密码显示隐藏
     */
    @BindView(R.id.img_passwd_toggle)
    ImageView imgPasswdToggle;
    /**
     * 下一步
     */
    @BindView(R.id.btn_unfreeze_next)
    Button btnUnfreezeNext;

    @BindView(R.id.rl_user_unfreeze)
    RelativeLayout rlUserUnfreeze;
    /**
     * 完成
     */
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.rl_head2)
    RelativeLayout rlHead2;

    private boolean fromMain = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_un_freeeze;
    }

    @Override
    public void init() {
        setTitleText("解冻账户");
        fromMain = getIntent().getBooleanExtra("fromMain", false);

        rlUserUnfreeze.setVisibility(View.VISIBLE);
        rlHead2.setVisibility(View.GONE);
        String mobile = SharepUtil.<UserInfo>getBeanFromSp("user").mobile;
        StringBuilder builder=new StringBuilder(mobile);
        tvUserUnfreeze.setText("当前账号:" + builder.replace(3, 7, "****"));
//        tvBaseRight.setText("退出");
//        tvUserUnfreeze.setText("当前账号:" + SharepUtil.<UserInfo>getBeanFromSp("user").mobile + "");
    }


    @OnClick({R.id.img_passwd_toggle, R.id.btn_unfreeze_next, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_passwd_toggle:
                passwdToggle(view.isSelected(), edUnfreezePwd, (ImageView) view);
                view.setSelected(!view.isSelected());
                break;
            case R.id.btn_unfreeze_next:
                String pwd = edUnfreezePwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showShort("密码为空");
                    return;
                }
                getPresenter().freeze(1,pwd);
                break;
            case R.id.btn_commit:
                finish();
                break;
        }
    }

    private void passwdToggle(boolean selected, EditText editText, ImageView imageView) {

        if (selected) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_login_pwd_hide));

        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_login_pwd_show));

        }

    }

    @Override
    public void freezeSuccess(BaseError baseError) {
        if (baseError.msg == null) {
            ActivityControl.removeFreezeActivity();
            UserInfo userInfo = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
            userInfo.userFreeze = 1;
            SharepUtil.putByBean(GlobalKey.USER_INFO,userInfo);
            rlUserUnfreeze.setVisibility(View.GONE);
            rlHead2.setVisibility(View.VISIBLE);
        } else {
            ToastUtils.showShort(baseError.msg);
        }
    }
}

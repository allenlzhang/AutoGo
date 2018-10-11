package com.carlt.autogo.view.activity.more.safety;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.user.accept.IdfCompleteActivity;
import com.carlt.autogo.view.activity.user.accept.UserIdChooseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Marlon on 2018/9/12.
 */
public class SafetyActivity extends BaseMvpActivity {
    /**
     * 身份认证
     */
    @BindView(R.id.ll_safety_identity_authentication)
    LinearLayout mLlIdentityAuthentication;

    @BindView(R.id.tv_safety_identity_authentication_status)
    TextView mTvIdentityAuthentication;

    /**
     * 人脸识别
     */
    @BindView(R.id.ll_safety_face_recognition)
    LinearLayout mLlFaceRecognition;

    @BindView(R.id.tv_safety_face_recognition_status)
    TextView mTvFaceRecognition;

    @BindView(R.id.iv_safety_face_recognition_status)
    ImageView mIvFaceRecognition;

    /**
     * 绑定手机
     */
    @BindView(R.id.ll_safety_binding_phone)
    LinearLayout mLlBindingPhone;

    @BindView(R.id.tv_safety_binding_phone)
    TextView mTvBindingPhone;

    /**
     * 登录密码管理
     */
    @BindView(R.id.ll_safety_login_pwd_management)
    LinearLayout mLlLoginPwdManagement;

    /**
     * 远程控制密码管理
     */
    @BindView(R.id.ll_safety_remote_pwd_management)
    LinearLayout mLlRemotePwdManagement;

    /**
     * 登录设备管理
     */
    @BindView(R.id.ll_safety_login_device_management)
    LinearLayout mLlLoginDeviceManagement;

    /**
     * 冻结账户
     */
    @BindView(R.id.ll_safety_frozen_account)
    LinearLayout mLlFrozenAccount;

    /**
     * 注销账户
     */
    @BindView(R.id.ll_safety_cancellation_account)
    LinearLayout mLlCancellationAccount;
//    private LogoutTipDialog mTipDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_safety;
    }

    @Override
    public void init() {
        setTitleText(getResources().getString(R.string.more_accounts_and_security));
//        mTipDialog = new LogoutTipDialog(this, R.style.DialogCommon);
//        mTipDialog.setLisniter(new LogoutTipDialog.LogoutTipDialogClickLisniter() {
//            @Override
//            public void commit() {
//                startActivity(new Intent(SafetyActivity.this, LogoutAccountActivityFirst.class));
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //        String isIdentity = SharepUtil.getPreferences().getString(GlobalKey.IDENTITY_AUTH, "");
        UserInfo user = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        LogUtils.e("----" + user.toString());
        if (user.alipayAuth != 0 && user.faceId != 0) {
            mTvIdentityAuthentication.setText("已认证");
            mTvIdentityAuthentication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent safety_identity = new Intent(SafetyActivity.this, IdfCompleteActivity.class);
                    safety_identity.putExtra("idcard", false);
                    startActivity(safety_identity);
                }
            });
        } else {
            mTvIdentityAuthentication.setText("未认证");
            mTvIdentityAuthentication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent safety_identity = new Intent(SafetyActivity.this, UserIdChooseActivity.class);
                    startActivity(safety_identity);
                }
            });
        }
    }

    @OnClick({R.id.ll_safety_identity_authentication, R.id.ll_safety_face_recognition, R.id.ll_safety_binding_phone, R.id.ll_safety_login_pwd_management, R.id.ll_safety_remote_pwd_management, R.id.ll_safety_login_device_management, R.id.ll_safety_frozen_account, R.id.ll_safety_cancellation_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_safety_identity_authentication:

                break;
            case R.id.ll_safety_face_recognition:
                startActivity(new Intent(this, FaceRecognitionSettingActivity.class));
                break;
            case R.id.ll_safety_binding_phone:
                break;
            case R.id.ll_safety_login_pwd_management:
                Intent intent = new Intent(SafetyActivity.this, LoginPwdManagementActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_safety_remote_pwd_management:
                Intent intent1 = new Intent(SafetyActivity.this, RemotePwdManagementActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_safety_login_device_management:
                Intent intentDecide = new Intent(SafetyActivity.this, LoginDeviceManagementActivity.class);
                startActivity(intentDecide);
                break;
            case R.id.ll_safety_frozen_account:

                Intent intentFreeze = new Intent(this, FreezeActivity.class);
                startActivity(intentFreeze);
                break;
            case R.id.ll_safety_cancellation_account:
//                mTipDialog.show();
                startActivity(new Intent(this, LogoutNoticeActivity.class));
                break;
        }
    }
}

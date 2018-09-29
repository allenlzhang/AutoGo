package com.carlt.autogo.view.activity.more.safety;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.view.view.PwdEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Marlon on 2018/9/14.
 */
public class ChangeRemotePwdActivity extends BaseMvpActivity {
    @BindView(R.id.edit_management_remote_phone)
    EditText editManagementRemotePhone;
    @BindView(R.id.edit_management_remote_code)
    EditText editManagementRemoteCode;
    @BindView(R.id.btn_management_remote_code)
    Button btnManagementRemoteCode;
    @BindView(R.id.rl_management_remote_code)
    RelativeLayout rlManagementRemoteCode;
    @BindView(R.id.tv_management_edit_remote_old_pwd)
    TextView tvManagementEditRemoteOldPwd;
    @BindView(R.id.et_management_remote_old_pwd)
    PwdEditText etManagementRemoteOldPwd;
    @BindView(R.id.et_management_remote_new_pwd)
    PwdEditText etManagementRemoteNewPwd;
    @BindView(R.id.et_management_remote_new_pwd_again)
    PwdEditText etManagementRemoteNewPwdAgain;
    @BindView(R.id.edit_management_remote_confirm)
    Button editManagementRemoteConfirm;

    int type = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_change_remote_pwd;
    }

    @Override
    public void init() {
        type = getIntent().getIntExtra("resetRemote", 0);
        loadTypeView(type);
    }


    @OnClick({R.id.btn_management_remote_code, R.id.edit_management_remote_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_management_remote_code:
                break;
            case R.id.edit_management_remote_confirm:
                break;
        }
    }

    private void loadTypeView(int type) {
        switch (type) {
            case RemotePwdManagementActivity.SETREMOTEPWD:
                editManagementRemotePhone.setVisibility(View.GONE);
                rlManagementRemoteCode.setVisibility(View.GONE);
                tvManagementEditRemoteOldPwd.setVisibility(View.GONE);
                etManagementRemoteOldPwd.setVisibility(View.GONE);
                editManagementRemoteConfirm.setText(getResources().getString(R.string.make_sure));
                setTitleText(getResources().getString(R.string.management_set_remote_control_pwd));
                break;
            case RemotePwdManagementActivity.REMEBERPWD:
                editManagementRemotePhone.setVisibility(View.GONE);
                rlManagementRemoteCode.setVisibility(View.GONE);
                setTitleText(getResources().getString(R.string.management_reset_remote_pwd));
                break;
            case RemotePwdManagementActivity.FORGETPWD:
                tvManagementEditRemoteOldPwd.setVisibility(View.GONE);
                etManagementRemoteOldPwd.setVisibility(View.GONE);
                setTitleText(getResources().getString(R.string.management_forget_remote_pwd));
                break;
        }
    }
}

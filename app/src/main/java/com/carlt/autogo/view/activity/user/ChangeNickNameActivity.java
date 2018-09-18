package com.carlt.autogo.view.activity.user;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.UUDialog;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.User;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;
import com.carlt.autogo.utils.SharepUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wsq
 * @time 14:19  2018/9/11/011
 * @describe  修改昵称
 */
public class ChangeNickNameActivity extends BaseMvpActivity {

    @BindView(R.id.ed_nick_name)EditText edNickName;
    @BindView(R.id.btn_nick_commit)Button btnNickCommit;


    private static final String TAG="ChangeNickNameActivity";

    UUDialog dialog ;
    @Override
    protected int getContentView() {
        return R.layout.activity_change_nick_name;
    }

    @Override
    public void init() {
        setTitleText("修改昵称");
        dialog = new UUDialog(this);
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.btn_nick_commit)
    public void onClick(){
        final String nickName =edNickName.getText().toString().trim();
        if(TextUtils.isEmpty(nickName)){
            ToastUtils.showShort("昵称不能为空");
            return;
        }

        Map<String,Object> params = new HashMap<>();
        params.put("token", SharepUtil.getPreferences().getString("token",""));
        params.put("realName",nickName );
        dialog.show();
        ClientFactory.def(UserService.class).userEditInfi(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        dialog.dismiss();
                        if(baseError.msg != null){
                            ToastUtils.showLong(baseError.msg);
                        }else {
                            ToastUtils.showLong("编辑成功");
                            UserInfo userInfo =   SharepUtil.<UserInfo>getBeanFromSp("user");
                            userInfo.realName = nickName ;
                                    SharepUtil.putByBean("user",userInfo);
                            finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showLong("编辑失败");
                        dialog.dismiss();
                    }
                });

    }


}

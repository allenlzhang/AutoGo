package com.carlt.autogo.presenter.safety;

import android.annotation.SuppressLint;

import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.entry.user.UserIdentity;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.UserService;

import java.util.HashMap;

import io.reactivex.functions.Consumer;

/**
 * Created by Marlon on 2019/1/8.
 */
public class IdentityPresenter extends BasePresenter<IIdentityView> {
    @SuppressLint("CheckResult")
    public void getIdentity() {
        ClientFactory.def(UserService.class).getIdentity(new HashMap<String, Object>())
                .subscribe(new Consumer<UserIdentity>() {
                    @Override
                    public void accept(UserIdentity userIdentity) throws Exception {
                        mView.getIdentity(userIdentity);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }
}

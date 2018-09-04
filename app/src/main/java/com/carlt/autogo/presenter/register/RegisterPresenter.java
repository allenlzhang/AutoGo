package com.carlt.autogo.presenter.register;


import com.carlt.autogo.basemvp.BasePresenter;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:53
 */
public class RegisterPresenter extends BasePresenter<IRegisterView> {
    public void register() {
        // TODO: 2018/9/3 注册逻辑 
        mView.onRegisterFinish();
    }
}

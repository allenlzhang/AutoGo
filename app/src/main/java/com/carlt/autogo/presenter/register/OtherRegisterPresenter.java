package com.carlt.autogo.presenter.register;

import com.alipay.sdk.app.AuthTask;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.basemvp.BasePresenter;

public class OtherRegisterPresenter extends BasePresenter<IOtherRegisterView> {
   public void weChatLogin (){

       ToastUtils.showShort("微信登录");

    }

    public void paymentLogin (){


        ToastUtils.showShort("支付宝登录");

    }
}

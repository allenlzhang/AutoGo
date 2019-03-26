package com.carlt.autogo.presenter.login;

import com.carlt.autogo.basemvp.BaseMvpView;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:30
 */
public interface ILoginView extends BaseMvpView {
    void loginFinish(String msg);
}

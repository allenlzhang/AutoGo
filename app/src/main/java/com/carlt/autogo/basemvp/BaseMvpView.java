package com.carlt.autogo.basemvp;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:20
 */
public interface BaseMvpView {
    void showLoading(Boolean isShow);

    void showError(String msg);

    void complete();
}

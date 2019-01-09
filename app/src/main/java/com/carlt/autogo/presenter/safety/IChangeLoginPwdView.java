package com.carlt.autogo.presenter.safety;

import com.carlt.autogo.entry.user.BaseError;

/**
 * Created by Marlon on 2019/1/9.
 */
public interface IChangeLoginPwdView {
    void userResetPwdSuccess(BaseError baseError);
    void userRetrievePwdSuccess(BaseError baseError);
}

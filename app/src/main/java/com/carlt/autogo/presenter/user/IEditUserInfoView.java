package com.carlt.autogo.presenter.user;

import com.carlt.autogo.entry.user.BaseError;

/**
 * Created by Marlon on 2019/1/10.
 */
public interface IEditUserInfoView {
    void userEditInfoSuccess(BaseError baseError,String text,int sex);
}

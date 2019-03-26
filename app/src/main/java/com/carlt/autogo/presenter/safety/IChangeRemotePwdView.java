package com.carlt.autogo.presenter.safety;

import com.carlt.autogo.basemvp.BaseMvpView;
import com.carlt.autogo.entry.user.BaseError;

/**
 * Created by Marlon on 2019/1/10.
 */
public interface IChangeRemotePwdView extends BaseMvpView {
    void setRemotePwdSuccess(BaseError baseError);
    void modifyRemotePwd(BaseError baseError);
    void resetRemotePwd(BaseError baseError);
}

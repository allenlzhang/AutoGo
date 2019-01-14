package com.carlt.autogo.presenter.safety;

import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.entry.user.ShareLoginList;

/**
 * Created by Marlon on 2019/1/14.
 */
public interface ISafetyView {
    void getShareLoginSuccess(ShareLoginList info);
    void shareLoginUnbindSuccess(BaseError baseError,final int type);
}

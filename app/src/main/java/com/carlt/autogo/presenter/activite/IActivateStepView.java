package com.carlt.autogo.presenter.activite;

import com.carlt.autogo.entry.car.ActivateStepInfo;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:30
 */
public interface IActivateStepView {
    void getStepInfoFinish(ActivateStepInfo info);

    void getStepInfoErr(Throwable throwable);
}

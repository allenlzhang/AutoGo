package com.carlt.autogo.presenter.car;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.global.GlobalUrl;
import com.carlt.autogo.utils.SharepUtil;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.HashMap;

/**
 * Created by Marlon on 2019/1/7.
 */
public class CarCertificationPresenter extends BasePresenter<ICarCertificationView> {
    /**
     * 添加车辆
     */
    public void filter() {
        uuDialog.show();
        Gson gson = new Gson();
        if (!NetworkUtils.isConnected() && !NetworkUtils.isAvailableByPing()) {
            ToastUtils.showShort("网络错误，请检查网络");
            uuDialog.dismiss();
        }else {
            OkGo.<String>post(GlobalUrl.TEST_BASE_URL + "BrandProduct/AutoFilter")
                    .headers("Content-Type", "application/json")
                    .headers("Carlt-Access-Id", GlobalKey.TEST_ACCESSID)
                    .headers("Carlt-Token", SharepUtil.getPreferences().getString("token", ""))
                    .upJson(gson.toJson(new HashMap<>()))
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            uuDialog.dismiss();
                            mView.selectCarSuccess(response.body());
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            uuDialog.dismiss();
                            LogUtils.e(response);
                        }


                    });
        }
    }
}

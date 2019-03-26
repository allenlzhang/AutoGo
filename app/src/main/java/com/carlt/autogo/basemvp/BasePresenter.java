package com.carlt.autogo.basemvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;


import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.common.dialog.UUDialog;


import java.util.ArrayList;
import java.util.List;


import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/3 16:22
 */
public class BasePresenter<V> {
    protected Context          mContext;
    protected V                mView;
    public    UUDialog         uuDialog;
    public    List<Disposable> disposables = new ArrayList<>();
    public    String           errorMsg    = "登录失败";

    protected void onCleared() {

    }

    public void attachView(Context context, V view) {
        this.mContext = context;
        this.mView = view;
        uuDialog = new UUDialog(mContext, R.style.DialogCommon);
    }

    public void detachView() {
        this.mView = null;
//        mContext = null;
    }

    public boolean isAttachView() {
        return this.mView != null;
    }

    public void onCreatePresenter(@Nullable Bundle savedState) {

    }


    public void onDestroyPresenter() {
        this.mContext = null;
        detachView();
    }

    public void onSaveInstanceState(Bundle outState) {

    }


    public class CommonThrowable<Throwable> implements Consumer<Throwable> {

        @Override
        public void accept(Throwable t) throws Exception {
            uuDialog.dismiss();
            ToastUtils.showShort(errorMsg);
            LogUtils.e(t.toString());
        }

    }
}

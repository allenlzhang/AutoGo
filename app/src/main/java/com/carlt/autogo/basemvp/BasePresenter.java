package com.carlt.autogo.basemvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;


import com.blankj.utilcode.util.LogUtils;
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
    protected Context mContext;
    protected V       mView;
    public UUDialog uuDialog  ;
    public    List<Disposable> disposables = new ArrayList<>();
    protected void onCleared() {

    }

    public void attachView(Context context, V view) {
        this.mContext = context;
        this.mView = view;
        uuDialog  =new UUDialog(mContext);
    }

    public void detachView() {
        this.mView = null;
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


    public class CommonThrowable<T> implements Consumer<T>{

        @Override
        public void accept(T t) throws Exception {
            uuDialog.dismiss();
            LogUtils.e(t.toString());
        }
    }
}

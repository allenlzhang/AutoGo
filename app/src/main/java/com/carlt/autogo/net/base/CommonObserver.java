package com.carlt.autogo.net.base;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.UUDialog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class CommonObserver<T> implements Observer<T> {

    private UUDialog        uuDialog;
    private BaseMvpActivity base;

    public CommonObserver(Context context) {
        this.uuDialog = new UUDialog(context, R.style.DialogCommon);
        if (context instanceof BaseMvpActivity) {
            this.base = (BaseMvpActivity) context;
        }
        LogUtils.e(this.base + "========context=======");

    }

    @Override
    public void onSubscribe(Disposable d) {
        if (uuDialog != null) {
            uuDialog.show();
        }
        if (this.base != null && this.base.rxLifeImp != null) {
            this.base.rxLifeImp.addDisposable(d);
        }

    }

    @Override
    public void onNext(T t) {

        if (uuDialog != null) {
            uuDialog.dismiss();
        }

        accept(t);
    }


    @Override
    public void onError(Throwable e) {
        if (uuDialog != null) {
            uuDialog.dismiss();
        }
        throwable(e);
    }

    @Override
    public void onComplete() {

    }

    public abstract void accept(T t);

    public abstract void throwable(Throwable e);
}

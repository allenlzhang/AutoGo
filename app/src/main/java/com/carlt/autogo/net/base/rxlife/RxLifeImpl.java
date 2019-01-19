package com.carlt.autogo.net.base.rxlife;


import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class RxLifeImpl implements RxLife {

    public List<Disposable> disposables = new ArrayList<>();

    //也可以不同步
    public synchronized void addDisposable(Disposable d) {
        if (disposables != null) {
            disposables.add(d);
        }

    }

    @Override
    public void onPause() {
        cancleWork();
        LogUtils.e("onPause");

    }

    @Override
    public void onStop() {
        cancleWork();
        LogUtils.e("onStop");
    }

    @Override
    public void onDestroy() {
        cancleWork();
        if (disposables != null) {
            disposables.clear();
        }
        disposables = null;
        LogUtils.e("onDestory");
    }

    private void cancleWork() {
        if (disposables != null && disposables.size() > 0) {
            for (Disposable d : disposables) {
                if (d.isDisposed()) {
                    disposables.remove(d);
                } else {
                    d.dispose();
                }
            }
        }
    }
}

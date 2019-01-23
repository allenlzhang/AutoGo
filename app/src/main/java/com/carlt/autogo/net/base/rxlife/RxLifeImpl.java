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

    private synchronized void cancleWork() {

        if (disposables != null && disposables.size() > 0) {
//            for (Disposable d : disposables) {
//                if (d.isDisposed()) {
//                    disposables.remove(d);
//                } else {
//                    d.dispose();
//                }
//            }

    //遍历List 时候尽量不要做删除操作，改变集合的大小，很容易出现异常,相当于数据读写不同步，必须保证同步。
            for (int i =0 ;i < disposables.size();i++){
                Disposable d = disposables.get(i);
                if(d.isDisposed()){
                    disposables.remove(i);
                    // i -- ,保证集合容量同步
                    i--;
                }else {
                    d.dispose();
                }
            }
        }



    }
}

package com.carlt.autogo.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.basemvp.BaseMvpView;
import com.carlt.autogo.basemvp.BasePresenter;
import com.carlt.autogo.basemvp.PresenterDispatch;
import com.carlt.autogo.basemvp.PresenterProviders;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Description :fragment 基类
 */
public abstract class BaseMvpFragment<P extends BasePresenter> extends Fragment implements BaseMvpView {
    protected View           mRootView;
    protected LayoutInflater inflater;
    // 标志位 标志已经初始化完成。
    protected boolean        isPrepared;
    //标志位 fragment是否可见
    protected boolean        isVisible;

    protected Context  mContext;
    protected Activity mActivity;

    private PresenterProviders mPresenterProviders;
    private PresenterDispatch  mPresenterDispatch;
    private Unbinder           unbinder;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null)
                parent.removeView(mRootView);
        } else {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            unbinder = ButterKnife.bind(this, mRootView);
            mActivity = getActivity();
            mContext = mActivity;
            this.inflater = inflater;
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenterProviders = PresenterProviders.inject(this);
        mPresenterDispatch = new PresenterDispatch(mPresenterProviders);

        mPresenterDispatch.attachView(getActivity(), this);
        mPresenterDispatch.onCreatePresenter(savedInstanceState);
        isPrepared = true;
        init();
        lazyLoad();
    }

    protected void showToast(String txt) {
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
        ToastUtils.showShort(txt);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenterDispatch.onSaveInstanceState(outState);
    }

    protected P getPresenter() {
        return mPresenterProviders.getPresenter(0);
    }

    public PresenterProviders getPresenterProviders() {
        return mPresenterProviders;
    }

    /**
     * 获取布局
     */
    public abstract
    @LayoutRes
    int getLayoutId();


    /**
     * 初始化
     */
    protected abstract void init();

    public View findViewById(@IdRes int id) {
        View view;
        if (mRootView != null) {
            view = mRootView.findViewById(id);
            return view;
        }
        return null;
    }

    /**
     * 懒加载
     */
    private void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        lazyLoadData();
        isPrepared = false;
    }

    /**
     * 懒加载
     */
    protected void lazyLoadData() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showLoading(Boolean isShow) {

    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenterDispatch.detachView();
        //            unbinder.unbind();

    }

    @Override
    public void onDetach() {
        this.mActivity = null;
        super.onDetach();
    }
}

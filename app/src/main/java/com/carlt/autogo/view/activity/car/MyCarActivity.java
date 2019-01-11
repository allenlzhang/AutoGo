package com.carlt.autogo.view.activity.car;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.view.fragment.AuthFragment;
import com.carlt.autogo.view.fragment.MyCarFragment;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by Marlon on 2018/11/22.
 */
public class MyCarActivity extends BaseMvpActivity {
    @BindView(R.id.myCarTabs)
    TabLayout myCarTabs;

    private String[] tabs         = {"我的爱车", "被授权车辆"};
    private int      mCurrentItem = 0;
    private int      currentTab;

    @Override
    protected int getContentView() {
        return R.layout.activity_my_car;
    }

    @Override
    public void init() {
        setTitleText("我的爱车");
        currentTab = getIntent().getIntExtra("currentTab", 0);
        LogUtils.e("currentTab-----" + currentTab);
        initBottomTabs();
    }

    //    @Override
    //    protected void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        initSavedInstanceState(savedInstanceState);
    //        initBottomTabs();
    //    }

    private void initSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //            LogUtils.e("异常关闭----" + savedInstanceState.toString());
            //            int currentIndex = savedInstanceState.getInt("currentItem", 0);
            //            setIndexFragment(currentIndex);
            //            myCarTabs.getTabAt(currentIndex);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentItem", mCurrentItem);
        //        LogUtils.e("异常关闭----" + outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //        int currentIndex = savedInstanceState.getInt("currentItem", 0);
        //        setIndexFragment(currentIndex);
        //        myCarTabs.getTabAt(currentIndex);
    }

    private void initBottomTabs() {
        myCarTabs.addTab(myCarTabs.newTab().setText(tabs[0]));
        myCarTabs.addTab(myCarTabs.newTab().setText(tabs[1]));
        myCarTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mCurrentItem = tab.getPosition();
                setIndexFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        myCarTabs.getTabAt(currentTab).select();
        setIndexFragment(currentTab);
    }

    /**
     * 设置默认的fragment
     */
    private void setIndexFragment(int index) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.flMyCar, getFragment(index));
        ft.commitAllowingStateLoss();
    }

    public static HashMap<Integer, BaseMvpFragment> saveFragment = new HashMap<>();

    public static BaseMvpFragment getFragment(int position) {
        BaseMvpFragment fragment = saveFragment.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new MyCarFragment();
                    break;
                case 1:
                    fragment = new AuthFragment();
                    break;
                default:

            }
            saveFragment.put(position, fragment);
        }
        return fragment;
    }
}

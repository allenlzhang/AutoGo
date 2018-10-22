package com.carlt.autogo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.model.TabEntity;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.view.activity.more.safety.FreezeActivity;
import com.carlt.autogo.view.fragment.FragmentFactory;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Description : 主页面
 */
public class MainActivity extends BaseMvpActivity {

    @BindView(R.id.bottomTabs)
    CommonTabLayout bottomTabs;
    private String[]                   mTabNames        = {"首页", "远程", "更多"};
    private int[]                      mIconUnselectIds = {
            R.drawable.ic_bottom_tab_home_normal, R.drawable.ic_bottom_tab_remote_normal,
            R.drawable.ic_bottom_tab_more_normal};
    private int[]                      mIconSelectIds   = {
            R.drawable.ic_bottom_tab_home_selected, R.drawable.ic_bottom_tab_remote_selected,
            R.drawable.ic_bottom_tab_more_selected};
    private ArrayList<CustomTabEntity> mTabEntities     = new ArrayList<>();
    private int                        mCurrentItem     = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        //        setTitleText("主页");
        rlTitle.setVisibility(View.GONE);
        initBottomTabs();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSavedInstanceState(savedInstanceState);

    }

    private void initSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentItem", mCurrentItem);

    }

    private void initBottomTabs() {
        for (int i = 0; i < mTabNames.length; i++) {
            mTabEntities.add(new TabEntity(mTabNames[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        bottomTabs.setTabData(mTabEntities);
        bottomTabs.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                BaseMvpFragment fragment = null;
                switch (position) {
                    case 0:
                        mCurrentItem = 0;
                        fragment = FragmentFactory.getFragment(0);
                        ft.replace(R.id.fl_content, fragment);
                        break;
                    case 1:
                        mCurrentItem = 1;
                        fragment = FragmentFactory.getFragment(1);
                        ft.replace(R.id.fl_content, fragment);
                        break;
                    case 2:
                        mCurrentItem = 2;
                        fragment = FragmentFactory.getFragment(2);
                        ft.replace(R.id.fl_content, fragment);
                        break;
                    default:
                        break;
                }
                ft.commitAllowingStateLoss();
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        bottomTabs.setCurrentTab(0);
        setDefaultFragment();
    }

    /**
     * 设置默认的fragment
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_content, FragmentFactory.getFragment(0));
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserInfo info = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);

        if (info.userFreeze == 2) {
            Intent intent = new Intent(MainActivity.this, FreezeActivity.class);
            intent.putExtra("fromMain", true);
            startActivity(intent);
        }
    }
}

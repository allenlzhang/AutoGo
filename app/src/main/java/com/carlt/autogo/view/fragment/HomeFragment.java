package com.carlt.autogo.view.fragment;

import android.content.Intent;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.view.activity.car.CarCertificationActivity;

import butterknife.BindView;

/**
 * Description: 首页fragment
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/4 17:42
 */
public class HomeFragment extends BaseMvpFragment {
    @BindView(R.id.tv)
    TextView tv;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init() {
        tv.setText("首页");
        CommonDialog.createDialogNotitle(getActivity(), "你还没有进行车辆认证", "", "稍候再说", "立即认证", new CommonDialog.DialogWithTitleClick() {
            @Override
            public void onRightClick() {
                startActivity(new Intent(getActivity(), CarCertificationActivity.class));
            }
        });
    }


}

package com.carlt.autogo.view.fragment;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpFragment;

/**
 * Description: 首页fragment
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/4 17:42
 */
public class HomeFragment extends BaseMvpFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init() {
//        tv.setText("首页");
//        CommonDialog.createDialogNotitle(getActivity(), "你还没有进行车辆认证", "", "稍候再说", "立即认证", new CommonDialog.DialogWithTitleClick() {
//            @Override
//            public void onRightClick() {
//                startActivity(new Intent(getActivity(), CarCertificationActivity.class));
//            }
//        });
    }


}

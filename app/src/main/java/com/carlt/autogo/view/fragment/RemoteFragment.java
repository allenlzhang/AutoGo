package com.carlt.autogo.view.fragment;

import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpFragment;

import butterknife.BindView;

/**
 * Description:
 * Company    : carlt
 * Author     : zhanglei
 * Date       : 2018/9/4 17:42
 */
public class RemoteFragment extends BaseMvpFragment {
    @BindView(R.id.tv)
    TextView tv;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_remote;
    }

    @Override
    protected void init() {

    }


}

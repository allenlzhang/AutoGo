package com.carlt.autogo.view.activity.more.safety;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

/**
 * @author wsq
 * @time 15:42  2018/9/21/021
 * @describe
 *
 *    账户冻结
 */
public class FreezeActivity extends BaseMvpActivity {


    @Override
    protected int getContentView() {
        return R.layout.activity_freeze;
    }

    @Override
    public void init() {

        setTitleText("冻结账户");
    }
}

package com.carlt.autogo.view.activity.more.transfer;

import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;

/**
 * Description : 扫码结果页面
 * Author     : zhanglei
 * Date       : 2018/11/22
 */
public class ScannerResultActivity extends BaseMvpActivity {

    @BindView(R.id.tvResult)
    TextView tvResult;


    @Override
    protected int getContentView() {
        return R.layout.activity_scanner_result;
    }

    @Override
    public void init() {
        setTitleText("扫描结果");
        String result = getIntent().getStringExtra("result");
        tvResult.setText(result);
    }
}

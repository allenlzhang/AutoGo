package com.carlt.autogo.view.activity.more.transfer;

import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;

public class ScannerResultActivity extends BaseMvpActivity {

    @BindView(R.id.tvResult)
    TextView tvResult;

    //    @Override
    //    protected void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        setContentView(R.layout.activity_scanner_result);
    //        ButterKnife.bind(this);
    //    }

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

package com.carlt.autogo.view.activity.more.transfer;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.utils.QRCodeUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class QRCodeActivity extends BaseMvpActivity {

    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.tvRefreshCode)
    TextView  tvRefreshCode;
    @BindView(R.id.tvCarName)
    TextView  tvCarName;
    @BindView(R.id.tvTime)
    TextView  tvTime;


    @Override
    protected int getContentView() {
        return R.layout.activity_qrcode;
    }

    @Override
    public void init() {
        setTitleText("生成二维码");
        Bitmap codeBit = QRCodeUtils.createQRCode("sfsadfsfsf");
        ivQRCode.setImageBitmap(codeBit);
    }

    @OnClick({R.id.tvRefreshCode, R.id.llCar, R.id.llTime, R.id.llTransfer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvRefreshCode:
                break;
            case R.id.llCar:
                break;
            case R.id.llTime:
                break;
            case R.id.llTransfer:
                break;
        }
    }
}

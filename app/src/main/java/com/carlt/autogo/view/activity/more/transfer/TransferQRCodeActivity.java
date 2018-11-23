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

/**
 * Description : 生成过户二维码
 * Author     : zhanglei
 * Date       : 2018/11/23
 */
public class TransferQRCodeActivity extends BaseMvpActivity {

    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.tvCarName)
    TextView  tvCarName;


    @Override
    protected int getContentView() {
        return R.layout.activity_transfer_qrcode;
    }

    @Override
    public void init() {
        setTitleText("生成二维码");
        Bitmap qrCode = QRCodeUtils.createQRCode("sdfsdf");
        ivQRCode.setImageBitmap(qrCode);
    }

    @OnClick({R.id.tvRefreshCode, R.id.tvCarName})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvRefreshCode:
                break;
            case R.id.tvCarName:
                break;
        }
    }
}

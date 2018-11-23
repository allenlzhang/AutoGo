package com.carlt.autogo.view.activity.more.transfer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
/**
 * Description :自定义扫码 Activity
 * Author     : zhanglei
 * Date       : 2018/11/22
 */
public class ScanActivity extends BaseMvpActivity {
    CaptureManager       capture;
    DecoratedBarcodeView barcodeView;
    //    ImageView            back ;
    //    TextView             tittle , backTv;
    Bundle               mBundle;

    //    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_scan);
            mBundle = savedInstanceState;
            initView();

        }

    private void initView() {
        //        barcodeView  = findViewById(R.id.dbv_custom);
        //        tittle  = findViewById(R.id.tittle_tv);
        //        back  = findViewById(R.id.back_img);
                barcodeView  = findViewById(R.id.dbv_custom);
        //        backTv  = findViewById(R.id.back_tv);
        //        tittle.setText("条码扫描");
        barcodeView.getStatusView().setVisibility(View.GONE);
        capture = new CaptureManager(this, barcodeView);
        capture.initializeFromIntent(getIntent(), mBundle);
        capture.decode();


    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_scan;
    }

    @Override
    public void init() {
//        capture = new CaptureManager(this, barcodeView);
//        capture.initializeFromIntent(getIntent(), mBundle);
//        capture.decode();
        setTitleText("扫一扫");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}

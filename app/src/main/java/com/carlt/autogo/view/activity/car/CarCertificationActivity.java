package com.carlt.autogo.view.activity.car;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.DialogIdcardAccept;

import butterknife.BindView;
import butterknife.OnClick;

public class CarCertificationActivity extends BaseMvpActivity {


    @BindView(R.id.ll_vehicle_certification_add)
    LinearLayout llVehicleCertificationAdd;
    @BindView(R.id.edit_vehicle_certification_vin)
    EditText editVehicleCertificationVin;
    @BindView(R.id.edit_vehicle_certification_engine_num)
    EditText editVehicleCertificationEngineNum;
    @BindView(R.id.iv_vehicle_certification)
    ImageView ivVehicleCertification;
    @BindView(R.id.btn_vehicle_certification)
    Button btnVehicleCertification;
    int type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_car_certification);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_vehicle_certification;
    }

    @Override
    public void init() {
        setTitleText("车辆认证");
//        dialogIdcardAccept = new DialogIdcardAccept(this);
//        dialogIdcardAccept.setListner(new DialogIdcardAccept.ItemOnclickListner() {
//            @Override
//            public void byCermera() {
//
//                //                checkPermissions(mPermission, new PremissoinLisniter() {
//                //                    @Override
//                //                    public void createred() {
//                //                        doCarmera();
//                //                    }
//                //                });
//            }
//
//            @Override
//            public void byAblum() {
//
//                //                checkPermissions(mPermission, new PremissoinLisniter() {
//                //                    @Override
//                //                    public void createred() {
//                //                        doPhoto();
//                //                    }
//                //                });
//
//            }
//        });
    }

    @OnClick({R.id.btn_vehicle_certification,R.id.ll_vehicle_certification_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_vehicle_certification:
                startActivity(new Intent(this, CarActivateActivity.class));
                break;
            case R.id.ll_vehicle_certification_add:

                if (type == 0) {
                    type = 1;
                    startActivity(BrandActivity.class, false);
                }else if (type == 1){
                    type = 2;
                    startActivity(ModelActivity.class, false);
                }else if (type == 2){
                    type = 0;
                    startActivity(BrandCarActivity.class, false);
                }
                break;
        }
    }
}

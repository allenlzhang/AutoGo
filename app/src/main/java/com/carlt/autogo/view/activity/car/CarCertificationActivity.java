package com.carlt.autogo.view.activity.car;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.DialogIdcardAccept;

import butterknife.BindView;
import butterknife.OnClick;

public class CarCertificationActivity extends BaseMvpActivity {

    @BindView(R.id.etAddCar)
    EditText  etAddCar;
    @BindView(R.id.etFrameNum)
    EditText  etFrameNum;
    @BindView(R.id.etEngineNum)
    EditText  etEngineNum;
    @BindView(R.id.ivCarProof)
    ImageView ivCarProof;
    @BindView(R.id.btnNext)
    Button    btnNext;
    DialogIdcardAccept dialogIdcardAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_car_certification);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_car_certification;
    }

    @Override
    public void init() {
        setTitleText("车辆认证");
        dialogIdcardAccept = new DialogIdcardAccept(this);
        dialogIdcardAccept.setListner(new DialogIdcardAccept.ItemOnclickListner() {
            @Override
            public void byCermera() {

                //                checkPermissions(mPermission, new PremissoinLisniter() {
                //                    @Override
                //                    public void createred() {
                //                        doCarmera();
                //                    }
                //                });
            }

            @Override
            public void byAblum() {

                //                checkPermissions(mPermission, new PremissoinLisniter() {
                //                    @Override
                //                    public void createred() {
                //                        doPhoto();
                //                    }
                //                });

            }
        });
    }

    @OnClick({R.id.ivCarProof, R.id.btnNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivCarProof:
                dialogIdcardAccept.show();
                break;
            case R.id.btnNext:
                startActivity(new Intent(this, CarActivateActivity.class));
                break;
        }
    }
}

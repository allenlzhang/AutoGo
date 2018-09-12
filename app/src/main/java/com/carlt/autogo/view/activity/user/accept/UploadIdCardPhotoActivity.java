package com.carlt.autogo.view.activity.user.accept;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wsq
 * @time 10:33  2018/9/12/012
 * @describe  上传身份证照片 页面
 */
public class UploadIdCardPhotoActivity extends BaseMvpActivity {
    String[] mPermission = {Manifest.permission.CAMERA};
    @BindView(R.id.tv_name)TextView tvName;
    @BindView(R.id.img_delet_person_photo)ImageView imgDeletPersonPhoto;
    @BindView(R.id.img_person)ImageView imgPerson;
    @BindView(R.id.img_person_watermark)ImageView imgPersonWatermark;
    @BindView(R.id.img_person_c)ImageView imgPersonCamera;
    @BindView(R.id.img_delet_back_photo)ImageView imgDeletBackPhoto;
    @BindView(R.id.img_idcard_back_c)ImageView imgIdcardBackCamera;
    @BindView(R.id.img_idcard_back)ImageView imgIdcardBack;
    @BindView(R.id.img_back_watermark)ImageView imgBackWatermark;
    @BindView(R.id.idcard_upload_commit)Button idcardUploadCommit;


    private String name;
    private String idCardNum;


    @Override
    protected int getContentView() {
        return R.layout.activity_upload_id_card_photo;
    }

    @Override
    public void init() {
        setTitleText("上传身份证件");
        name = getIntent().getStringExtra("name");
        idCardNum = getIntent().getStringExtra("idcard");
        tvName.setText(name + "\t" +idCardNum);
    }

    //删除从新拍照
    @OnClick({R.id.img_delet_person_photo ,R.id.img_delet_back_photo })
    public void deletImage(View view){
        if(view.getId() == R.id.img_delet_person_photo){

            imgPerson.setImageDrawable(getResources().getDrawable(R.mipmap.idcard_person_bg));
            imgPersonWatermark.setVisibility(View.GONE);

            imgPersonCamera.setVisibility(View.VISIBLE);
        }else {

            imgIdcardBack.setImageDrawable(getResources().getDrawable(R.mipmap.idcard_person_bg));
            imgBackWatermark.setVisibility(View.GONE);
            imgIdcardBackCamera.setVisibility(View.VISIBLE);
        }


    }


    //拍照
    @OnClick({R.id.img_person_c ,R.id.img_idcard_back_c})
    public void useCamera(View view){

        if(view.getId() == R.id.img_person_c ){


        }else {


        }

    }


    public void showAndHideView (int pdele ,int pWatermark ,int pCamera ,int bdele, int bWatermark ,int bCamera){

    }
}

package com.carlt.autogo.view.activity.user;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.utils.PhotoUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wsq
 * @time 15:07  2018/9/11/011
 * @describe 用户个人头像
 */
public class PersonAvatarActivity extends BaseMvpActivity {

    @BindView(R.id.user_avatar)ImageView userAvatar;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    @Override
    protected int getContentView() {
        return R.layout.activity_person_avatar;
    }

    @Override
    public void init() {

        setTitleText("用户个人头像");

        setHeadColor(getResources().getColor(R.color.colorBlack),
                getResources().getColor(R.color.colorWhite),
                getResources().getColor(R.color.colorWhite)
                ,getResources().getColor(R.color.colorWhite));
        showHeaderRight("修改");


    }

    @OnClick({R.id.tv_base_right})
    //跳转手机相册
    public void headerRightBtnOnclick(){

        PhotoUtils.openPic(PersonAvatarActivity.this, CODE_GALLERY_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int output_X = 480, output_Y = 480;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case CODE_GALLERY_REQUEST://访问相册完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        newUri = FileProvider.getUriForFile(this, "com.carlt.autogo.fileprovider", new File(newUri.getPath()));
                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    LogUtils.e(bitmap);
                    setPicToView(bitmap);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  Glide.with(this).load(LoginInfo.getAvatar_img()).into(userAvatar);
    }

    public void setPicToView(Bitmap picToView) {
        Glide.with(this).load(picToView).into(userAvatar);

        //上传图片

    }
}

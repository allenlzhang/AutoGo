package com.carlt.autogo.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpFragment;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.basemvp.PresenterVariable;
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.entry.car.CarBaseInfo;
import com.carlt.autogo.entry.car.SingletonCar;
import com.carlt.autogo.entry.user.UserInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.presenter.car.ICarListView;
import com.carlt.autogo.presenter.car.MyCarListPresenter;
import com.carlt.autogo.utils.ActivityControl;
import com.carlt.autogo.utils.SharepUtil;
import com.carlt.autogo.utils.gildutils.GlideCircleTransform;
import com.carlt.autogo.view.activity.car.MyCarActivity;
import com.carlt.autogo.view.activity.more.safety.SafetyActivity;
import com.carlt.autogo.view.activity.more.transfer.AuthQRCodeActivity;
import com.carlt.autogo.view.activity.more.transfer.ScanActivity;
import com.carlt.autogo.view.activity.more.transfer.ScannerResultActivity;
import com.carlt.autogo.view.activity.more.transfer.WaitAuthActivity;
import com.carlt.autogo.view.activity.user.EditUserInfoActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;


/**
 * Description : 更多fragment
 * Author : zhanglei
 * Date : 2018/9/10
 */
@CreatePresenter(presenter = {MyCarListPresenter.class})
public class MoreFragment extends BaseMvpFragment implements ICarListView {


    /**
     * 性别
     */
    @BindView(R.id.iv_more_sex)
    ImageView mIvSex;

    /**
     * 编辑资料
     */
    @BindView(R.id.tv_more_edit_profile)
    TextView mTvEditProfile;

    /**
     * 账户与安全
     */
    @BindView(R.id.ll_more_accounts_and_security)
    LinearLayout mLlAccountsAndSecurity;

    /**
     * 我的爱车
     */
    @BindView(R.id.ll_more_myCar)
    LinearLayout mLlMyCar;

    /**
     * 头像
     */
    @BindView(R.id.iv_more_head_sculpture)
    ImageView ivMoreHeadSculpture;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    /**
     * 车主昵称
     */
    @BindView(R.id.tv_more_nickname)
    TextView  tvMoreNickname;

    /**
     * 退出登录
     */
    @BindView(R.id.ll_more_log_out)
    LinearLayout llMoreLogOut;


    @BindView(R.id.ll_more_layout1)
    LinearLayout       llMoreLayout1;
    @BindView(R.id.ll_more_remote_update)
    LinearLayout       llMoreRemoteUpdate;
    @BindView(R.id.ll_more_service_renewal)
    LinearLayout       llMoreServiceRenewal;
    @BindView(R.id.ll_more_contact)
    LinearLayout       llMoreContact;
    @PresenterVariable
    MyCarListPresenter mCarListPresenter;

    private UserInfo                    userInfo;
    private boolean                     isBindCar;
    private List<AuthCarInfo.MyCarBean> myCar;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    protected void init() {
    }


    @Override
    public void onResume() {
        super.onResume();
        initMyCarInfo();
        isBindCar = SingletonCar.getInstance().isBound();
        userInfo = SharepUtil.getBeanFromSp(GlobalKey.USER_INFO);
        if (!TextUtils.isEmpty(userInfo.realName)) {
            tvMoreNickname.setText(userInfo.realName + "");
        } else {
            tvMoreNickname.setText("--");
        }


        if (userInfo.gender == 1) {
            mIvSex.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sex_men));
        } else if (userInfo.gender == 2) {
            mIvSex.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sex_women));
        } else {
            mIvSex.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sex_secrecy));
        }

        Glide.with(mContext)
                .load(userInfo.avatarFile)
                .placeholder(R.mipmap.iv_def_head)
                .error(R.mipmap.iv_def_head)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true)
                .transform(new GlideCircleTransform(mContext))
                .into(ivMoreHeadSculpture);


    }

    private void initMyCarInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", 1);//1我的车辆 2被授权车辆 3我的车辆和被授权车辆
        map.put("isShowActive", 2);//默认1不显示，2显示设备等激活状态
        mCarListPresenter.getCarList(map);
    }

    @OnClick({R.id.ll_more_remote_update, R.id.ivAdd, R.id.ll_more_myCar, R.id.tv_more_edit_profile, R.id.ll_more_accounts_and_security, R.id.ll_more_log_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_more_edit_profile:
                Intent edit_profile = new Intent(mContext, EditUserInfoActivity.class);
                startActivity(edit_profile);
                break;
            case R.id.ll_more_accounts_and_security:
                Intent intent = new Intent(mContext, SafetyActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_more_myCar:
                Intent intent1 = new Intent(mContext, MyCarActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_more_log_out:

                ActivityControl.logout(mActivity);
                break;
            case R.id.ivAdd:
                showPop(ivAdd);
                break;
            case R.id.ll_more_remote_update:
                //                startActivity(new Intent(mActivity, ActivateStepActivity.class));
                break;
            default:
        }
    }

    private void showPop(ImageView ivAdd) {

        EasyPopup.create()
                .setContext(mActivity)
                .setContentView(R.layout.layout_right_pop)
                .setAnimationStyle(R.style.QQPopAnim)
                .setFocusAndOutsideEnable(true)
                .setOnViewListener(new EasyPopup.OnViewListener() {
                    @Override
                    public void initViews(View view, final EasyPopup easyPopup) {

                        View tvLine = view.findViewById(R.id.tvLine);
                        View tvScanner = view.findViewById(R.id.tvScanner);
                        View tvQrCode = view.findViewById(R.id.tvQrCode);
                        if (isBindCar && myCar != null && myCar.size() != 0) {
                            tvLine.setVisibility(View.VISIBLE);
                            tvQrCode.setVisibility(View.VISIBLE);
                        } else {

                            tvLine.setVisibility(View.GONE);
                            tvQrCode.setVisibility(View.GONE);
                        }
                        tvScanner.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //                                IntentIntegrator integrator = new IntentIntegrator(mActivity);
                                // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
                                //                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                                //                                integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
                                //                                integrator.setPrompt(""); //底部的提示文字，设为""可以置空
                                //                                integrator.setCameraId(0); //前置或者后置摄像头
                                //                                integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
                                //                                integrator.setBarcodeImageEnabled(false);
                                //                                integrator.initiateScan();
                                IntentIntegrator.forSupportFragment(MoreFragment.this)
                                        .setCaptureActivity(ScanActivity.class)
                                        .initiateScan();
                                easyPopup.dismiss();
                            }
                        });
                        tvQrCode.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(mActivity, AuthQRCodeActivity.class));
                                easyPopup.dismiss();
                            }
                        });
                    }
                })
                .showAtAnchorView(ivAdd, YGravity.BELOW, XGravity.LEFT, SizeUtils.dp2px(40), 0);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if ((userInfo.identityAuth == 2 || userInfo.alipayAuth == 2) && userInfo.faceId != 0) {
            if (result != null && result.getContents() != null) {
                LogUtils.e(result.getContents());
                //            ToastUtils.showShort("扫码成功---" + result.getContents());
                //            Toast.makeText(this,"扫码成功",Toast.LENGTH_LONG).show();
                String contents = result.getContents();

                if (contents.startsWith(GlobalKey.AUTH_REGEX)) {
                    if (checkTime(contents))
                        return;
                    checkQRCodeState(contents);
                } else if (contents.startsWith(GlobalKey.TRANSFER_REGEX)) {
                    if (checkTime(contents))
                        return;
                    checkTransferCode(contents);
                } else {
                    Intent intent = new Intent(mActivity, ScannerResultActivity.class);
                    intent.putExtra("result", result.getContents());
                    startActivity(intent);
                }


            } else {
                ToastUtils.showShort("扫码失败");
                //            Toast.makeText(this, "扫码失败", Toast.LENGTH_LONG).show();
            }
        } else {
            ToastUtils.showShort("请先进行身份认证");
        }
    }

    private boolean checkTime(String contents) {
        String[] split1 = contents.split("&");

        //        String time = contents.substring(contents.indexOf("&") + 1);
        String[] split = split1[1].split("=");
        long currentTime = System.currentTimeMillis() / 1000;
        Long aLong = Long.valueOf(split[1]);
        if (currentTime - aLong > 300) {
            //五分钟失效
            ToastUtils.showShort("二维码已失效");
            return true;
        }
        return false;
    }

    @SuppressLint("CheckResult")
    private void checkTransferCode(String contents) {

        String[] split1 = contents.split("&");
        String[] split = split1[0].split("=");
        final Integer id = Integer.valueOf(split[1]);
        HashMap<String, Object> map = new HashMap<>();
        map.put("transferId", id);
        ClientFactory.def(CarService.class).scanTransferCode(map)
                .subscribe(new Consumer<CarBaseInfo>() {
                    @Override
                    public void accept(CarBaseInfo carBaseInfo) throws Exception {
                        LogUtils.e(carBaseInfo.status);

                        if (carBaseInfo.status == 1) {
                            Intent intent = new Intent(mActivity, WaitAuthActivity.class);
                            intent.putExtra("code", 1);
                            intent.putExtra("transferId", id);
                            startActivity(intent);

                        } else {
                            ToastUtils.showShort(carBaseInfo.err.msg);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void checkQRCodeState(String contents) {
        String[] split1 = contents.split("&");
        String[] split = split1[0].split("=");
        final Integer id = Integer.valueOf(split[1]);
        LogUtils.e(id);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        ClientFactory.def(CarService.class).scanQrcode(map)
                .subscribe(new Consumer<CarBaseInfo>() {
                    @Override
                    public void accept(CarBaseInfo carBaseInfo) throws Exception {
                        if (carBaseInfo.err == null) {
                            Intent intent = new Intent(mActivity, WaitAuthActivity.class);
                            intent.putExtra("code", 2);
                            intent.putExtra("authId", id);
                            startActivity(intent);
                        } else {
                            ToastUtils.showShort(carBaseInfo.err.msg);
                        }
                        LogUtils.e(carBaseInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                    }
                });
    }

    @Override
    public void getCarListSuccess(AuthCarInfo info) {
        myCar = info.myCar;
        if (isBindCar && myCar != null && myCar.size() != 0) {
            llMoreLayout1.setVisibility(View.VISIBLE);
            llMoreRemoteUpdate.setVisibility(View.VISIBLE);
            llMoreServiceRenewal.setVisibility(View.VISIBLE);
            llMoreContact.setVisibility(View.VISIBLE);
        } else {
            llMoreLayout1.setVisibility(View.GONE);
            llMoreRemoteUpdate.setVisibility(View.GONE);
            llMoreServiceRenewal.setVisibility(View.GONE);
            llMoreContact.setVisibility(View.GONE);
        }
    }
}

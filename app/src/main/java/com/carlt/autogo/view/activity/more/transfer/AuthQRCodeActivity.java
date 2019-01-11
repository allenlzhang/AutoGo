package com.carlt.autogo.view.activity.more.transfer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.CarAuthTimeAdapter;
import com.carlt.autogo.adapter.CarNameItemAdapter;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.entry.car.CarAuthTimeInfo;
import com.carlt.autogo.entry.car.CarBaseInfo;
import com.carlt.autogo.entry.car.SingletonCar;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.presenter.carauth.AuthQRCodePresenter;
import com.carlt.autogo.presenter.carauth.IAuthQRCodeView;
import com.carlt.autogo.utils.QRCodeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zyyoona7.popup.EasyPopup;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Description : 过户二维码页面
 * Author     : zhanglei
 * Date       : 2018/11/22
 */
@CreatePresenter(presenter = AuthQRCodePresenter.class)
public class AuthQRCodeActivity extends BaseMvpActivity<AuthQRCodePresenter> implements IAuthQRCodeView {

    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.tvRefreshCode)
    TextView  tvRefreshCode;
    @BindView(R.id.tvCarName)
    TextView  tvCarName;
    @BindView(R.id.tvTime)
    TextView  tvTime;
    private CarAuthTimeAdapter    authTimeAdapter;
    private CarNameItemAdapter    carNameItemAdapter;
    private int                   mCarId;
    private int                   mTimeType;
    private AuthCarInfo.MyCarBean carInfo;
    private String                carName;

    @Override
    protected int getContentView() {
        return R.layout.activity_qrcode;
    }

    @Override
    public void init() {
        setTitleText("生成二维码");
        carInfo = SingletonCar.getInstance().getCarBean();
        carName = carInfo.carName;
        tvCarName.setText(carName);
        tvTime.setText("1小时");
        initQrCode(0, 0);
        //        checkQrCodeState();


    }

    @Override
    protected void onResume() {
        super.onResume();
        interval();
    }

    private Disposable              disposable;
    private int                     duration = 10 * 60;
    private HashMap<String, Object> map      = new HashMap<>();
    private int                     mId;

    @SuppressLint("CheckResult")
    private void checkQrCodeState(final int id) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        this.map = map;
        mId = id;
    }

    private void interval() {
        disposable = Observable.interval(5, TimeUnit.SECONDS)

                .flatMap(new Function<Long, ObservableSource<CarBaseInfo>>() {
                    @Override
                    public ObservableSource<CarBaseInfo> apply(Long aLong) throws Exception {
                        return ClientFactory.def(CarService.class).checkStatus(map);
                    }
                })
                .subscribe(new Consumer<CarBaseInfo>() {
                    @Override
                    public void accept(CarBaseInfo carBaseInfo) throws Exception {
                        LogUtils.e(carBaseInfo.checkStatus);
                        if (duration <= 0) {
                            disposable.dispose();
                        } else {
                            if (carBaseInfo.checkStatus == 2) {

                                Intent intent = new Intent(AuthQRCodeActivity.this, AuthHandleActivity.class);
                                intent.putExtra("id", mId);
                                startActivity(intent);
                                disposable.dispose();
                            }
                            duration--;
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        disposable.dispose();
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void initQrCode(int carId, int authType) {
        carInfo = SingletonCar.getInstance().getCarBean();
        ivQRCode.setImageBitmap(null);

        //        dialog.show();
        HashMap<String, Object> map = new HashMap<>();
        if (carId == 0) {
            map.put("carId", carInfo.id);
            mCarId = carInfo.id;
        } else {
            map.put("carId", carId);
            mCarId = carId;
        }
        if (authType == 0) {
            map.put("authType", 1);
        } else {
            map.put("authType", authType);
        }
        getPresenter().createAuthQrcode(map);
        //        ClientFactory.def(CarService.class).createAuthQrcode(map)
        //                .subscribe(new Consumer<CarBaseInfo>() {
        //                    @Override
        //                    public void accept(CarBaseInfo carInfo) throws Exception {
        //                        if (carInfo.id != 0) {
        //                            long l = System.currentTimeMillis() / 1000;
        //                            Bitmap codeBit = QRCodeUtils.createQRCode(GlobalKey.AUTH_REGEX + carInfo.id + "&time=" + l);
        //                            checkQrCodeState(carInfo.id);
        //                            ivQRCode.setImageBitmap(codeBit);
        //                        } else {
        //                            showToast(carInfo.err.msg);
        //
        //                            CommonDialog.createOneBtnDialog(AuthQRCodeActivity.this, carInfo.err.msg, false, new CommonDialog.DialogOneBtnClick() {
        //                                @Override
        //                                public void onOneBtnClick() {
        //                                    finish();
        //                                }
        //                            });
        //                        }
        //                        dialog.dismiss();
        //                        LogUtils.e(carInfo);
        //                    }
        //                }, new Consumer<Throwable>() {
        //                    @Override
        //                    public void accept(Throwable throwable) throws Exception {
        //                        LogUtils.e(throwable);
        //                        dialog.dismiss();
        //                    }
        //                });
    }

    @Override
    public void createAuthQRCode(CarBaseInfo info) {
        if (info.id != 0) {
            long l = System.currentTimeMillis() / 1000;
            Bitmap codeBit = QRCodeUtils.createQRCode(GlobalKey.AUTH_REGEX + info.id + "&time=" + l);
            checkQrCodeState(info.id);
            ivQRCode.setImageBitmap(codeBit);
        } else {
            //            showToast(info.err.msg);

            CommonDialog.createOneBtnDialog(AuthQRCodeActivity.this, info.err.msg, false, new CommonDialog.DialogOneBtnClick() {
                @Override
                public void onOneBtnClick() {
                    //                    finish();
                }
            });
        }
        dialog.dismiss();
        LogUtils.e(info);
    }

    @Override
    public void getMyCarList(AuthCarInfo info, View v) {
        LogUtils.e(info);
        List<AuthCarInfo.MyCarBean> myCar = info.myCar;
        carNameItemAdapter = new CarNameItemAdapter(myCar, carCurrentSelect);
        showCarPop(v, "请选择授权车辆", carNameItemAdapter);
    }

    @Override
    public void getAuthTimes(CarAuthTimeInfo info, View v) {
        LogUtils.e(info);
        authTimeAdapter = new CarAuthTimeAdapter(info.list, timeCurrentSelect);
        showCarPop(v, "请选择授权时长", authTimeAdapter);
    }

    @SuppressLint("CheckResult")
    private void initCarName(final View v) {
        //        dialog.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", 1);//1我的车辆 2被授权车辆 3我的车辆和被授权车辆
        map.put("isShowActive", 2);//默认1不显示，2显示设备等激活状态
        getPresenter().getMyCarList(map, v);
        //        ClientFactory.def(CarService.class).getMyCarList(map)
        //                .subscribe(new Consumer<AuthCarInfo>() {
        //                    @Override
        //                    public void accept(AuthCarInfo carInfo) throws Exception {
        //                        dialog.dismiss();
        //                        LogUtils.e(carInfo);
        //                        List<AuthCarInfo.MyCarBean> myCar = carInfo.myCar;
        //                        carNameItemAdapter = new CarNameItemAdapter(myCar, carCurrentSelect);
        //                        showCarPop(v, "请选择授权车辆", carNameItemAdapter);
        //                    }
        //                }, new Consumer<Throwable>() {
        //                    @Override
        //                    public void accept(Throwable throwable) throws Exception {
        //                        dialog.dismiss();
        //                        LogUtils.e(throwable);
        //                    }
        //                });


        //                Gson gson=new Gson();
        //                OkGo.<String>post(url)
        //                        .headers("Carlt-Access-Id", GlobalKey.TEST_ACCESSID)
        //                        .headers("Carlt-Token", SharepUtil.getPreferences().getString("token", ""))
        //                        .upJson(gson.toJson(map))
        //                        .execute(new StringCallback() {
        //                            @Override
        //                            public void onSuccess(Response<String> response) {
        //                                LogUtils.e(response.body());
        //                            }
        //
        //                            @Override
        //                            public void onError(Response<String> response) {
        //                                super.onError(response);
        //                                LogUtils.e(response.body());
        //                            }
        //                        });

    }

    public static final String url = "http://test.linewin.cc:8888/app/CarAuth/GetMyCarList";
    private             int    timeCurrentSelect;
    private             int    carCurrentSelect;

    @SuppressLint("CheckResult")
    private void initAuthTime(View view) {
        //        dialog.show();
        HashMap<String, Object> map = new HashMap<>();
        getPresenter().getAuthTimes(map, view);
        //        ClientFactory.def(CarService.class).getAuthSetting(map)
        //                .subscribe(new Consumer<CarAuthTimeInfo>() {
        //                    @Override
        //                    public void accept(CarAuthTimeInfo carAuthTimeInfo) throws Exception {
        //                        dialog.dismiss();
        //                        LogUtils.e(carAuthTimeInfo);
        //                        authTimeAdapter = new CarAuthTimeAdapter(carAuthTimeInfo.list, timeCurrentSelect);
        //                        showCarPop(view, "请选择授权时长", authTimeAdapter);
        //
        //                    }
        //                }, new Consumer<Throwable>() {
        //                    @Override
        //                    public void accept(Throwable throwable) throws Exception {
        //                        dialog.dismiss();
        //                        LogUtils.e(throwable);
        //                    }
        //                });


    }

    public static final int resCode = 101;

    @OnClick({R.id.tvRefreshCode, R.id.tvTime, R.id.tvCarName, R.id.llTransfer, R.id.ivQRCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvRefreshCode:
                //                refreshQrCode();
            case R.id.ivQRCode:
                refreshQrCode();
                break;
            case R.id.tvCarName:
                initCarName(view);
                break;
            case R.id.tvTime:
                initAuthTime(view);
                break;
            case R.id.llTransfer:
                Intent intent = new Intent(this, TransferQRCodeActivity.class);
                intent.putExtra("carId", mCarId);
                intent.putExtra("carName", carName);
                startActivityForResult(intent, resCode);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == resCode) {

                initQrCode(mCarId, mTimeType);
            }
        }
    }

    private void refreshQrCode() {
        //        Bitmap codeBit = QRCodeUtils.createQRCode("ddsdxza");
        //        ivQRCode.setImageBitmap(codeBit);
        initQrCode(mCarId, mTimeType);
    }

    private void showCarPop(View v, final String title, final BaseQuickAdapter adapter) {
        EasyPopup.create()
                .setContext(this)
                .setContentView(R.layout.layout_car_name_pop, ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(250))
                .setAnimationStyle(R.style.BottomDialogAnimation)
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .setDimValue(0.5f)
                .setOnViewListener(new EasyPopup.OnViewListener() {
                    @Override
                    public void initViews(View view, final EasyPopup easyPopup) {
                        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                easyPopup.dismiss();
                            }
                        });
                        TextView tvTitle = view.findViewById(R.id.tvPopTitle);
                        tvTitle.setText(title);
                        RecyclerView rv = view.findViewById(R.id.recyclerView);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AuthQRCodeActivity.this);
                        rv.setLayoutManager(linearLayoutManager);
                        rv.addItemDecoration(new DividerItemDecoration(AuthQRCodeActivity.this, DividerItemDecoration.VERTICAL));
                        //                        CarNameItemAdapter carNameItemAdapter = new CarNameItemAdapter(datas);
                        rv.setAdapter(adapter);
                        if (adapter instanceof CarAuthTimeAdapter) {
                            authTimeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    CarAuthTimeInfo.ListBean info = (CarAuthTimeInfo.ListBean) adapter.getData().get(position);
                                    timeCurrentSelect = position;
                                    authTimeAdapter.setDefSelect(position);
                                    LogUtils.e(info.name);
                                    tvTime.setText(info.name);
                                    easyPopup.dismiss();
                                    initQrCode(0, info.type);
                                    mTimeType = info.type;
                                }
                            });
                        } else {
                            carNameItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    AuthCarInfo.MyCarBean carBean = (AuthCarInfo.MyCarBean) adapter.getData().get(position);
                                    carCurrentSelect = position;
                                    tvCarName.setText(carBean.carName);
                                    carName = carBean.carName;
                                    easyPopup.dismiss();
                                    initQrCode(carBean.id, 0);
                                    mCarId = carBean.id;
                                }
                            });
                        }

                    }
                })
                .showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (disposable != null) {
            disposable.dispose();
        }
    }


}

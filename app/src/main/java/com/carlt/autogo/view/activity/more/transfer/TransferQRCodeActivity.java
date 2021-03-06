package com.carlt.autogo.view.activity.more.transfer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.adapter.CarNameItemAdapter;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.entry.car.CarBaseInfo;
import com.carlt.autogo.global.GlobalKey;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.presenter.cartransfer.ITransferQRCodeView;
import com.carlt.autogo.presenter.cartransfer.TransferQRCodePresenter;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Description : 生成过户二维码
 * Author     : zhanglei
 * Date       : 2018/11/23
 */
@CreatePresenter(presenter = TransferQRCodePresenter.class)
public class TransferQRCodeActivity extends BaseMvpActivity<TransferQRCodePresenter> implements ITransferQRCodeView {

    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.tvCarName)
    TextView  tvCarName;
    private int mCarId;

    @Override
    protected int getContentView() {
        return R.layout.activity_transfer_qrcode;
    }

    @Override
    public void init() {
        setTitleText("生成二维码");
        String carName = getIntent().getStringExtra("carName");

        tvCarName.setText(carName);
        initQRCode(0);

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
        map.put("transferId", id);
        this.map = map;
        mId = id;
    }

    private void interval() {
        disposable = Observable.interval(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Long, ObservableSource<CarBaseInfo>>() {
                    @Override
                    public ObservableSource<CarBaseInfo> apply(Long aLong) throws Exception {
                        return ClientFactory.def(CarService.class).checkTransferStatus(map);
                    }
                })
                .subscribe(new Consumer<CarBaseInfo>() {
                    @Override
                    public void accept(CarBaseInfo carBaseInfo) throws Exception {
                        LogUtils.e(carBaseInfo.status);
                        if (duration <= 0) {
                            disposable.dispose();
                        } else {
                            if (carBaseInfo.status == 2) {
                                disposable.dispose();
                                Intent intent = new Intent(TransferQRCodeActivity.this, TransHandleActivity.class);
                                intent.putExtra("id", mId);
                                intent.putExtra("carName", tvCarName.getText().toString().trim());
                                startActivity(intent);

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
    private void initQRCode(int id) {
        //        dialog.show();
        int carId = getIntent().getIntExtra("carId", 0);
        //        mCarId = carId;
        ivQRCode.setImageBitmap(null);
        HashMap<String, Object> map = new HashMap<>();
        if (id == 0) {
            map.put("carId", carId);
            mCarId = carId;
        } else {
            map.put("carId", id);
            mCarId = id;
        }
        getPresenter().createTransferQrcode(map);

        //        ClientFactory.def(CarService.class).launchTransfer(map)
        //                .subscribe(new Consumer<CarBaseInfo>() {
        //                    @Override
        //                    public void accept(CarBaseInfo carBaseInfo) throws Exception {
        //                        LogUtils.e(carBaseInfo.transferId);
        //                        long l = System.currentTimeMillis() / 1000;
        //                        Bitmap qrCode = QRCodeUtils.createQRCode(GlobalKey.TRANSFER_REGEX + carBaseInfo.transferId + "&time=" + l);
        //                        ivQRCode.setImageBitmap(qrCode);
        //                        checkQrCodeState(carBaseInfo.transferId);
        //                        dialog.dismiss();
        //                    }
        //                }, new Consumer<Throwable>() {
        //                    @Override
        //                    public void accept(Throwable throwable) throws Exception {
        //                        LogUtils.e(throwable);
        //                        dialog.dismiss();
        //                    }
        //                });
    }

    @OnClick({R.id.tvRefreshCode, R.id.tvCarName, R.id.ivQRCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvRefreshCode:
                //                initQRCode(mCarId);
                //                break;
            case R.id.ivQRCode:
                initQRCode(mCarId);
                break;
            case R.id.tvCarName:
                initCarName();
                break;
        }
    }

    private CarNameItemAdapter carNameItemAdapter;

    @SuppressLint("CheckResult")
    private void initCarName() {
        //        dialog.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", 1);//1我的车辆 2被授权车辆 3我的车辆和被授权车辆
        map.put("isShowActive", 2);//默认1不显示，2显示设备等激活状态
        getPresenter().getMyCarList(map);
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

    }

    private int carCurrentSelect;

    private void showCarPop(final BaseQuickAdapter adapter) {
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
                        tvTitle.setText("请选择授权车辆");
                        RecyclerView rv = view.findViewById(R.id.recyclerView);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TransferQRCodeActivity.this);
                        rv.setLayoutManager(linearLayoutManager);
                        rv.addItemDecoration(new DividerItemDecoration(TransferQRCodeActivity.this, DividerItemDecoration.VERTICAL));
                        //                        CarNameItemAdapter carNameItemAdapter = new CarNameItemAdapter(datas);
                        rv.setAdapter(adapter);
                        if (adapter instanceof CarNameItemAdapter) {

                            carNameItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    AuthCarInfo.MyCarBean carBean = (AuthCarInfo.MyCarBean) adapter.getData().get(position);
                                    carCurrentSelect = position;
                                    tvCarName.setText(carBean.carName);
                                    easyPopup.dismiss();
                                    initQRCode(carBean.id);
                                    mCarId = carBean.id;
                                }
                            });
                        }

                    }
                })
                .showAtLocation(tvCarName, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_OK);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void createTransferQRCode(CarBaseInfo info) {
        LogUtils.e(info.transferId);
        if (info.err != null) {
            CommonDialog.createOneBtnDialog(this, info.err.msg, false, new CommonDialog.DialogOneBtnClick() {
                @Override
                public void onOneBtnClick() {
                    //                    finish();
                }
            });
        } else {
            long l = System.currentTimeMillis() / 1000;
            Bitmap qrCode = QRCodeUtils.createQRCode(GlobalKey.TRANSFER_REGEX + info.transferId + "&time=" + l);
            ivQRCode.setImageBitmap(qrCode);
            checkQrCodeState(info.transferId);
        }

    }

    @Override
    public void getMyCarList(AuthCarInfo info) {
        List<AuthCarInfo.MyCarBean> myCar = info.myCar;
        carNameItemAdapter = new CarNameItemAdapter(myCar, carCurrentSelect);
        showCarPop(carNameItemAdapter);
    }


}

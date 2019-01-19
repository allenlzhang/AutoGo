package com.carlt.autogo.view.activity.more.transfer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.entry.car.CarBaseInfo;
import com.carlt.autogo.entry.car.SingletonCar;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.view.activity.car.MyCarActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Description : 授权过户登录页面
 * Author     : zhanglei
 * Date       : 2019/1/4
 */
public class WaitAuthActivity extends BaseMvpActivity {
    @BindView(R.id.tvDes)
    TextView tvDes;
    private String cancelTip;


    @Override
    protected int getContentView() {
        return R.layout.activity_wait_auth;
    }

    @Override
    public void init() {

        int code = getIntent().getIntExtra("code", -1);
        switch (code) {
            case 1:
                setTitleText("等待过户");
                tvDes.setText("已发送过户请求等待确认");
                pollingTransferResult();
                cancelTip = "过户失败";
                break;
            case 2:
                setTitleText("等待授权");
                tvDes.setText("已发送授权请求等待确认");
                cancelTip = "授权失败";
                pollingAuthResult();
                break;
            default:
        }
        interval();
    }


    int        count = 600;
    Disposable disposableCancel;

    private void interval() {
        disposableCancel = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override

                    public void accept(Long aLong) throws Exception {
                        LogUtils.e(count);
                        if (count <= 0) {

                            CommonDialog.createOneBtnDialog(WaitAuthActivity.this, cancelTip, false, new CommonDialog.DialogOneBtnClick() {
                                @Override
                                public void onOneBtnClick() {
                                    finish();
                                }
                            });
                            disposableCancel.dispose();
                        } else {
                            count--;
                        }
                    }
                });
    }

    private Disposable disposable;
    private int        duration = 600;

    @SuppressLint("CheckResult")
    private void pollingAuthResult() {
        int id = getIntent().getIntExtra("authId", 0);
        final HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        disposable = Observable.interval(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
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
                        LogUtils.e(duration);
                        if (duration <= 0) {
                            //                            CommonDialog.createOneBtnDialog(WaitAuthActivity.this, "授权已取消", false, new CommonDialog.DialogOneBtnClick() {
                            //                                @Override
                            //                                public void onOneBtnClick() {
                            //                                    finish();
                            //                                }
                            //                            });
                            disposable.dispose();

                        } else {
                            duration--;
                            if (carBaseInfo.checkStatus == 3) {
                                disposable.dispose();
                                CommonDialog.createOneBtnDialog(WaitAuthActivity.this, "授权成功", false, new CommonDialog.DialogOneBtnClick() {

                                    @Override
                                    public void onOneBtnClick() {
                                        Intent intent = new Intent(WaitAuthActivity.this, MyCarActivity.class);
                                        intent.putExtra("currentTab", 1);
                                        startActivity(intent);
                                        finish();

                                    }
                                });

                            } else if (carBaseInfo.checkStatus == 4) {
                                disposable.dispose();
                                CommonDialog.createOneBtnDialog(WaitAuthActivity.this, "授权失败", false, new CommonDialog.DialogOneBtnClick() {
                                    @Override
                                    public void onOneBtnClick() {
                                        finish();
                                    }
                                });
                            }
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

    private void pollingTransferResult() {
        int transferId = getIntent().getIntExtra("transferId", 0);
        final HashMap<String, Object> map = new HashMap<>();
        map.put("transferId", transferId);
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
                        LogUtils.e(duration);
                        if (duration <= 0) {
                            //                            CommonDialog.createOneBtnDialog(WaitAuthActivity.this, "过户已取消", false, new CommonDialog.DialogOneBtnClick() {
                            //                                @Override
                            //                                public void onOneBtnClick() {
                            //                                    finish();
                            //                                }
                            //                            });
                            disposable.dispose();
                        } else {
                            duration--;
                            if (carBaseInfo.status == 3) {
                                disposable.dispose();
                                CommonDialog.createOneBtnDialog(WaitAuthActivity.this, "过户成功", false, new CommonDialog.DialogOneBtnClick() {

                                    @Override
                                    public void onOneBtnClick() {
                                        Intent intent = new Intent(WaitAuthActivity.this, MyCarActivity.class);
                                        intent.putExtra("currentTab", 0);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } else if (carBaseInfo.status == 4) {
                                disposable.dispose();
                                CommonDialog.createOneBtnDialog(WaitAuthActivity.this, "过户失败", false, new CommonDialog.DialogOneBtnClick() {
                                    @Override
                                    public void onOneBtnClick() {
                                        finish();
                                    }
                                });
                            }
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
    private void GetCarList() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", 3);//1我的车辆 2被授权车辆 3我的车辆和被授权车辆
        map.put("isShowActive", 2);//默认1不显示，2显示设备等激活状态
        ClientFactory.def(CarService.class).getMyCarList(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AuthCarInfo>() {
                    @Override
                    public void accept(AuthCarInfo info) throws Exception {
                        parseGetMyList(info);
                        //                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable);
                        //                        finish();
                    }
                });
    }

    private void parseGetMyList(AuthCarInfo info) {
        if (info.err != null) {
            ToastUtils.showShort(info.err.msg);
        } else {
            isBindCar(info);
        }
    }

    /**
     * 是否绑定车辆
     * @param info
     */
    private void isBindCar(AuthCarInfo info) {
        if (info != null) {
            if (info.myCar != null && info.myCar.size() > 0) {
                SingletonCar.getInstance().setBound(true);
                SingletonCar.getInstance().setCarBean(info.myCar.get(0));
            } else if (info.authCar != null && info.authCar.size() > 0) {
                SingletonCar.getInstance().setBound(true);
                SingletonCar.getInstance().setCarBean(info.authCar.get(0));
            } else {
                SingletonCar.getInstance().setBound(false);
            }
        } else {
            SingletonCar.getInstance().setBound(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (disposable != null) {
            disposable.dispose();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposableCancel != null) {
            disposableCancel.dispose();
        }
    }
}

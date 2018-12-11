package com.carlt.autogo.view.activity.more.transfer;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.CommonDialog;
import com.carlt.autogo.entry.car.CarBaseInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class WaitAuthActivity extends BaseMvpActivity {
    @BindView(R.id.tvDes)
    TextView tvDes;

    //    @Override
    //    protected void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        setContentView(R.layout.activity_wait_auth);
    //    }

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
                break;
            case 2:
                setTitleText("等待授权");
                tvDes.setText("已发送授权请求等待确认");

                pollingAuthResult();
                break;
            default:
        }
    }


    private Disposable disposable;
    private int duration = 10 * 60 * 60;

    @SuppressLint("CheckResult")
    private void pollingAuthResult() {
        int id = getIntent().getIntExtra("authId", 0);
        final HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
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
                            duration--;
                            if (carBaseInfo.checkStatus == 3) {
                                disposable.dispose();
                                CommonDialog.createOneBtnDialog(WaitAuthActivity.this, "授权成功", false, new CommonDialog.DialogOneBtnClick() {
                                    @Override
                                    public void onOneBtnClick() {
                                        finish();
                                    }
                                });

                            } else if (carBaseInfo.checkStatus == 4) {
                                disposable.dispose();
                                CommonDialog.createOneBtnDialog(WaitAuthActivity.this, "拒绝授权", false, new CommonDialog.DialogOneBtnClick() {
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
                            duration--;
                            if (carBaseInfo.status == 3) {
                                disposable.dispose();
                                CommonDialog.createOneBtnDialog(WaitAuthActivity.this, "过户成功", false, new CommonDialog.DialogOneBtnClick() {
                                    @Override
                                    public void onOneBtnClick() {
                                        finish();
                                    }
                                });
                            } else if (carBaseInfo.status == 4) {
                                disposable.dispose();
                                CommonDialog.createOneBtnDialog(WaitAuthActivity.this, "拒绝过户", false, new CommonDialog.DialogOneBtnClick() {
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

    @Override
    protected void onPause() {
        super.onPause();
        if (disposable != null) {

            disposable.dispose();
        }
    }
}

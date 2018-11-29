package com.carlt.autogo.view.activity.more.transfer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
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
import com.carlt.autogo.entry.car.AuthCarInfo;
import com.carlt.autogo.entry.car.CarAuthTimeInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.utils.QRCodeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zyyoona7.popup.EasyPopup;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Description : 过户二维码页面
 * Author     : zhanglei
 * Date       : 2018/11/22
 */
public class AuthQRCodeActivity extends BaseMvpActivity {

    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.tvRefreshCode)
    TextView  tvRefreshCode;
    @BindView(R.id.tvCarName)
    TextView  tvCarName;
    @BindView(R.id.tvTime)
    TextView  tvTime;
    private ArrayList<String> carNames = new ArrayList<>();
    private ArrayList<String> times    = new ArrayList<>();
    private CarAuthTimeAdapter authTimeAdapter;
    private CarNameItemAdapter carNameItemAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_qrcode;
    }

    @Override
    public void init() {
        setTitleText("生成二维码");
        Bitmap codeBit = QRCodeUtils.createQRCode("sfsadfsfsf");
        ivQRCode.setImageBitmap(codeBit);
        carNames.add("奥迪 A4L 2017款 30 FSI 舒适版");
        carNames.add("奥迪 A5L 2017款 30 FSI 舒适版");
        carNames.add("奥迪 A6L 2017款 30 FSI 舒适版");
        carNames.add("奥迪 A1L 2017款 30 FSI 舒适版");
        carNames.add("奥迪 A3L 2017款 30 FSI 舒适版");
        carNames.add("奥迪 A8L 2017款 30 FSI 舒适版");
        carNames.add("奥迪 A7L 2017款 30 FSI 舒适版");
        times.add("1小时");
        times.add("2小时");
        times.add("3小时");
        times.add("4小时");
        times.add("5小时");
        times.add("6小时");
        //        initAuthTime();
        initCarName();
    }

    @SuppressLint("CheckResult")
    private void initCarName() {
        dialog.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", 1);
        ClientFactory.def(CarService.class).getMyCarList(map)
                .subscribe(new Consumer<AuthCarInfo>() {
                    @Override
                    public void accept(AuthCarInfo carInfo) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(carInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
       

    }

    public static final String url = "http://192.168.10.184:8080/app/CarAuth/GetMyCarList";

    @SuppressLint("CheckResult")
    private void initAuthTime(final View view) {
        dialog.show();
        HashMap<String, Object> map = new HashMap<>();
        ClientFactory.def(CarService.class).getAuthSetting(map)
                .subscribe(new Consumer<CarAuthTimeInfo>() {
                    @Override
                    public void accept(CarAuthTimeInfo carAuthTimeInfo) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(carAuthTimeInfo);
                        authTimeAdapter = new CarAuthTimeAdapter(carAuthTimeInfo.list);
                        showCarPop(view, "请选择授权时长", authTimeAdapter);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });


    }

    @OnClick({R.id.tvRefreshCode, R.id.tvTime, R.id.tvCarName, R.id.llTransfer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvRefreshCode:
                refreshQrCode();
                break;
            case R.id.tvCarName:
                carNameItemAdapter = new CarNameItemAdapter(carNames);
                showCarPop(view, "请选择授权车辆", carNameItemAdapter);
                break;
            case R.id.tvTime:
                //                showCarPop(view, "请选择授权时长", times);
                initAuthTime(view);
                break;
            case R.id.llTransfer:
                startActivity(TransferQRCodeActivity.class, false);
                break;
        }
    }

    private void refreshQrCode() {
        Bitmap codeBit = QRCodeUtils.createQRCode("ddsdxza");
        ivQRCode.setImageBitmap(codeBit);
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
                                    authTimeAdapter.setDefSelect(position);
                                    tvTime.setText(info.name);
                                    easyPopup.dismiss();
                                }
                            });
                        } else {
                            carNameItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    String name = (String) adapter.getData().get(position);
                                    tvCarName.setText(name);
                                    easyPopup.dismiss();
                                }
                            });
                        }

                    }
                })
                .showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }
}

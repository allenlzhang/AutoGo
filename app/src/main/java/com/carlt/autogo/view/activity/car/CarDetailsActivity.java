package com.carlt.autogo.view.activity.car;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.common.dialog.DialogEdit;
import com.carlt.autogo.entry.car.CarInfo;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.utils.MyTimeUtils;
import com.carlt.autogo.view.activity.activate.ActivateStepActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Marlon on 2018/11/27.
 * 车辆详情
 */
public class CarDetailsActivity extends BaseMvpActivity {
    @BindView(R.id.tvDetailsModel)
    TextView tvDetailsModel;//车型

    @BindView(R.id.llDetails)
    LinearLayout llDetails;//详情模块
    @BindView(R.id.tvDetailsBuyTime)
    TextView tvDetailsBuyTime;//购车时间
    @BindView(R.id.llDetailsBuyTime)
    LinearLayout llDetailsBuyTime;
    @BindView(R.id.tvDetailsServiceCycle)
    TextView tvDetailsServiceCycle;//上次保养里程
    @BindView(R.id.llDetailsServiceCycle)
    LinearLayout llDetailsServiceCycle;
    @BindView(R.id.tvDetailsServiceTime)
    TextView tvDetailsServiceTime;//上次保养时间
    @BindView(R.id.llDetailsServiceTime)
    LinearLayout llDetailsServiceTime;
    @BindView(R.id.tvDetailsInsureTime)
    TextView tvDetailsInsureTime;//上次投保时间
    @BindView(R.id.llDetailsInsureTime)
    LinearLayout llDetailsInsureTime;
    @BindView(R.id.tvDetailsAnnualTime)
    TextView tvDetailsAnnualTime;//上次年检时间
    @BindView(R.id.llDetailsAnnualTime)
    LinearLayout llDetailsAnnualTime;
    @BindView(R.id.tvDetailsAuthStartTime)
    TextView tvDetailsAuthStartTime;//授权开始时间
    @BindView(R.id.llDetailsAuthStart)
    LinearLayout llDetailsAuthStart;
    @BindView(R.id.tvDetailsAuthEndTime)
    TextView tvDetailsAuthEndTime;//授权结束时间
    @BindView(R.id.llDetailsAuthEnd)
    LinearLayout llDetailsAuthEnd;

    @BindView(R.id.llDetailsDevice)
    LinearLayout llDetailsDevice;//设备模块
    @BindView(R.id.llDetailsRemote)
    LinearLayout llDetailsRemote;//远程
    @BindView(R.id.ivDetailsRemoteState)
    ImageView ivDetailsRemoteState;//远程激活icon
    @BindView(R.id.tvDetailsRemoteState)
    TextView tvDetailsRemoteState;//远程激活状态
    @BindView(R.id.llDetailsRecorder)
    LinearLayout llDetailsRecorder;//记录仪
    @BindView(R.id.ivDetailsRecorderState)
    ImageView ivDetailsRecorderState;//记录仪icon
    @BindView(R.id.tvDetailsRecorderState)
    TextView tvDetailsRecorderState;//记录仪状态
    @BindView(R.id.llDetailsMachine)
    LinearLayout llDetailsMachine;//车机
    @BindView(R.id.ivDetailsMachineState)
    ImageView ivDetailsMachineState;//车机icon
    @BindView(R.id.tvDetailsMachineState)
    TextView tvDetailsMachineState;//车机状态
    @BindView(R.id.tvDetailsRemote)
    TextView tvDetailsRemote;//远程txt
    @BindView(R.id.tvDetailsRecorder)
    TextView tvDetailsRecorder;//记录仪txt
    @BindView(R.id.tvDetailsMachine)
    TextView tvDetailsMachine;//车机txt

    @BindView(R.id.btnCancelAuth)
    Button btnCancelAuth;//取消授权


    private int type = DETAILS_TYPE1;
    private boolean isRemoteActivating = false;  //远程是否激活中
    public static final int DETAILS_TYPE1 = 0;  //未激活未授权（我的爱车详情）
    public static final int DETAILS_TYPE2 = 1;  //已激活未授权（我的爱车详情）
    public static final int DETAILS_TYPE3 = 2;  //已激活授权中（我的爱车详情）
    public static final int DETAILS_TYPE4 = 3;  //（被授权车辆详情）
    private int remoteStatus = 0;   //远程状态
    private int authId = 0; //授权id
    private int carId = 0;  //车辆id

    private static final int BUYTIME = 0;   //购车时间触发日期弹窗
    private static final int MAINTENDATE = 1;//上次保养时间触发日期弹窗
    private static final int APPLICANTDATE = 2;//上次投保时间触发日期弹窗
    private static final int INSPECTTIME = 3;//上次年检时间触发日期弹窗

    @Override
    protected int getContentView() {
        return R.layout.activity_car_details;
    }

    @Override
    public void init() {
        setTitleText("车辆详情");
        initView();
    }

    private void initView() {

        Intent intent = getIntent();
        type = intent.getIntExtra("type", DETAILS_TYPE1);
        isRemoteActivating = intent.getBooleanExtra("remoteActivating", false);
        switch (type) {
            case DETAILS_TYPE1:
                llDetails.setVisibility(View.GONE);
                btnCancelAuth.setVisibility(View.GONE);
//                if (isRemoteActivating) {
//                    ivDetailsRemoteState.setImageResource(R.mipmap.ic_remote_activating_big);
//                    tvDetailsRemote.setTextColor(getResources().getColor(R.color.colorActivating));
//                    tvDetailsRemoteState.setTextColor(getResources().getColor(R.color.colorActivating));
//                } else {
//                    ivDetailsRemoteState.setImageResource(R.mipmap.ic_remote_activate_big);
//                    tvDetailsRemote.setTextColor(getResources().getColor(R.color.textColorGray));
//                    tvDetailsRemoteState.setTextColor(getResources().getColor(R.color.textColorGray));
//                }
                break;
            case DETAILS_TYPE2:
                llDetailsAuthStart.setVisibility(View.GONE);
                llDetailsAuthEnd.setVisibility(View.GONE);
                btnCancelAuth.setVisibility(View.GONE);
//                ivDetailsRemoteState.setImageResource(R.mipmap.ic_remote_activated_big);
//                tvDetailsRemote.setTextColor(getResources().getColor(R.color.colorBlue));
//                tvDetailsRemoteState.setTextColor(getResources().getColor(R.color.colorBlue));
                break;
            case DETAILS_TYPE3:
//                ivDetailsRemoteState.setImageResource(R.mipmap.ic_remote_activated_big);
//                tvDetailsRemote.setTextColor(getResources().getColor(R.color.colorBlue));
//                tvDetailsRemoteState.setTextColor(getResources().getColor(R.color.colorBlue));
                break;
            case DETAILS_TYPE4:
                llDetailsDevice.setVisibility(View.GONE);
                btnCancelAuth.setVisibility(View.GONE);
                break;
        }
        carId = intent.getIntExtra("id", 0);
        ClientGetCarInfo(carId);

    }

    /**
     * 获取车辆信息
     *
     * @param id
     */
    @SuppressLint("CheckResult")
    private void ClientGetCarInfo(int id) {
        dialog.show();
        Map<String, Integer> map = new HashMap<>();
        map.put("id", id);
        ClientFactory.def(CarService.class).getCarInfo(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CarInfo>() {
                    @Override
                    public void accept(CarInfo carInfo) {
                        dialog.dismiss();
                        if (carInfo.err != null) {
                            ToastUtils.showShort(carInfo.err.msg);
                        } else {
                            setData(carInfo);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        dialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
    }

    /**
     * 编辑车辆
     *
     * @param buyDate 购买时间
     * @param maintenMiles  保养里程
     * @param maintenDate 上次保养时间
     * @param applicantDate 上次投保时间
     * @param inspectTime   上次年检时间
     */
    @SuppressLint("CheckResult")
    private void modify(final long buyDate, final long maintenMiles, final long maintenDate, final long applicantDate, final long inspectTime) {
        dialog.show();

        Map<String, Object> map = new HashMap<>();
        map.put("id", carId);
        if (buyDate >= 0) {
            map.put("buyDate", buyDate);
        }
        if (maintenMiles >= 0) {
            map.put("maintenMiles", maintenMiles);
        }
        if (maintenDate >=0){
            map.put("maintenDate",maintenDate);
        }
        if (applicantDate >= 0){
            map.put("applicantDate",applicantDate);
        }
        if (inspectTime >= 0){
            map.put("inspectTime",inspectTime);
        }

        ClientFactory.def(CarService.class).modify(map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        dialog.dismiss();
                        if (baseError.msg != null) {
                            ToastUtils.showShort(baseError.msg);
                        } else {
                            ToastUtils.showShort("修改成功");
                            modifySuccess(buyDate,maintenMiles,maintenDate,applicantDate,inspectTime);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });
    }

    public void modifySuccess(long buyDate, long maintenMiles,long maintenDate,long applicantDate,long inspectTime) {
        if (buyDate >= 0) {
            tvDetailsBuyTime.setText(MyTimeUtils.formatDateSecend(buyDate));
        }
        if (maintenMiles >= 0) {
            tvDetailsServiceCycle.setText(String.valueOf(maintenMiles));
        }
        if (maintenDate >=0){
            tvDetailsServiceTime.setText(MyTimeUtils.formatDateSecend(maintenDate));
        }
        if (applicantDate >= 0){
            tvDetailsInsureTime.setText(MyTimeUtils.formatDateSecend(applicantDate));
        }
        if (inspectTime >= 0){
            tvDetailsAnnualTime.setText(MyTimeUtils.formatDateSecend(inspectTime));
        }
    }

    /**
     * 取消授权
     */
    @SuppressLint("CheckResult")
    private void cancelAuth() {
        dialog.show();
        Map<String, Integer> map = new HashMap<>();
        map.put("id", authId);
        ClientFactory.def(CarService.class).cancelAuth(map).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseError>() {
                    @Override
                    public void accept(BaseError baseError) throws Exception {
                        dialog.dismiss();
                        if (baseError != null) {
                            if (!TextUtils.isEmpty(baseError.msg)) {
                                ToastUtils.showShort(baseError.msg);
                            } else {
                                ToastUtils.showShort("取消授权成功");
                                finish();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                        LogUtils.e(throwable);
                    }
                });

    }
    int themeResId = AlertDialog.THEME_HOLO_LIGHT;
    @OnClick({R.id.llDetailsRemote, R.id.llDetailsRecorder, R.id.llDetailsMachine, R.id.btnCancelAuth, R.id.llDetailsBuyTime
            , R.id.llDetailsServiceCycle, R.id.llDetailsServiceTime, R.id.llDetailsInsureTime, R.id.llDetailsAnnualTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llDetailsRemote:
                clickRemote();
                break;
            case R.id.llDetailsRecorder:
//                showDatePicker("--",themeResId);
                break;
            case R.id.llDetailsMachine:
//                showEditDilog();
                break;
            case R.id.btnCancelAuth:
                cancelAuth();
                break;
            case R.id.llDetailsBuyTime:
                showDatePicker(BUYTIME,tvDetailsBuyTime.getText().toString());
                break;
            case R.id.llDetailsServiceCycle:
                showEditDilog();
                break;
            case R.id.llDetailsServiceTime:
                showDatePicker(MAINTENDATE,tvDetailsServiceTime.getText().toString());
                break;
            case R.id.llDetailsInsureTime:
                showDatePicker(APPLICANTDATE,tvDetailsInsureTime.getText().toString());
                break;
            case R.id.llDetailsAnnualTime:
                showDatePicker(INSPECTTIME,tvDetailsAnnualTime.getText().toString());
                break;
        }
    }

    private void clickRemote() {
        Intent intent = new Intent();
        if (remoteStatus == 1) {
            intent.setClass(CarDetailsActivity.this, ActivateStepActivity.class);
        } else if (remoteStatus == 2) {
            intent.setClass(CarDetailsActivity.this, ActivateStepActivity.class);
        } else {
            intent.setClass(CarDetailsActivity.this, DeviceActivateActivity.class);
        }
        intent.putExtra("carId", carId);
        startActivity(intent);
    }

    public void setData(CarInfo data) {
        remoteStatus = data.remoteStatus;
        authId = data.authId;
        if (!TextUtils.isEmpty(data.carName)) {
            tvDetailsModel.setText(data.carName);
        }
        if (data.buyDate != 0) {
            tvDetailsBuyTime.setText(MyTimeUtils.formatDateGetDaySecend(data.buyDate));
        } else {
            tvDetailsBuyTime.setText("--");
        }
        if (data.maintenMiles != 0) {
            tvDetailsServiceCycle.setText(String.valueOf(data.maintenMiles));
        } else {
            tvDetailsServiceCycle.setText("--");
        }
        if (data.maintenDate != 0) {
            tvDetailsServiceTime.setText(MyTimeUtils.formatDateGetDaySecend(data.maintenDate));
        } else {
            tvDetailsServiceTime.setText("--");
        }
        if (data.applicantDate != 0) {
            tvDetailsInsureTime.setText(MyTimeUtils.formatDateGetDaySecend(data.applicantDate));
        } else {
            tvDetailsInsureTime.setText("--");
        }
        if (data.inspectTime != 0) {
            tvDetailsAnnualTime.setText(MyTimeUtils.formatDateGetDaySecend(data.inspectTime));
        } else {
            tvDetailsAnnualTime.setText("--");
        }
        if (data.authStartTime != 0) {
            tvDetailsAuthStartTime.setText(MyTimeUtils.formatDateSecend(data.authStartTime));
        } else {
            tvDetailsAuthStartTime.setText("--");
        }
        if (data.authEndTime != 0) {
            long time = 0;
            try {
                time = MyTimeUtils.FORMAT_DAY.parse("2038-01-01").getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (data.authEndTime * 1000 >= time) {
                tvDetailsAuthEndTime.setText("永久");
            } else {
                tvDetailsAuthEndTime.setText(MyTimeUtils.formatDateSecend(data.authEndTime));
            }
        } else {
            tvDetailsAuthEndTime.setText("--");
        }
        if (data.remoteStatus == 1) {
            ivDetailsRemoteState.setImageResource(R.mipmap.ic_remote_activating_big);
            tvDetailsRemote.setTextColor(getResources().getColor(R.color.colorActivating));
            tvDetailsRemoteState.setTextColor(getResources().getColor(R.color.colorActivating));
        } else if (data.remoteStatus == 2) {
            ivDetailsRemoteState.setImageResource(R.mipmap.ic_remote_activated_big);
            tvDetailsRemote.setTextColor(getResources().getColor(R.color.colorBlue));
            tvDetailsRemoteState.setTextColor(getResources().getColor(R.color.colorBlue));
        } else {
            ivDetailsRemoteState.setImageResource(R.mipmap.ic_remote_activate_big);
            tvDetailsRemote.setTextColor(getResources().getColor(R.color.textColorGray));
            tvDetailsRemoteState.setTextColor(getResources().getColor(R.color.textColorGray));
        }

    }

    private void showEditDilog() {
        DialogEdit dialogEdit = new DialogEdit(this, R.style.DialogCommon);
        dialogEdit.setTitle("上次保养里程");
        dialogEdit.setOnClickListener(new DialogEdit.DialogConfirmClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(String txt) {
//                DecimalFormat format = new DecimalFormat("0.00");
//                txt = format.format(new BigDecimal(txt));
//                modify(Integer.parseInt(txt));
                modify(-1,Long.valueOf(txt),-1,-1,-1);
//                ToastUtils.showShort(txt);
            }
        });
        dialogEdit.show();
    }

    private void showDatePicker(final int modifyType, String date){
        final Calendar calendar = Calendar.getInstance();
        try {
            Date date1 = MyTimeUtils.FORMAT_DAY.parse(date);
            calendar.setTime(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DatePickerDialog dialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i,i1-1,i2);
                modifyType(modifyType,calendar.getTimeInMillis()/1000);
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void modifyType(int modifyType,long date){
        switch (modifyType){
            case BUYTIME:
                modify(date,-1,-1,-1,-1);
                break;
            case MAINTENDATE:
                modify(-1,-1,date,-1,-1);
                break;
            case APPLICANTDATE:
                modify(-1,-1,-1,date,-1);
                break;
            case INSPECTTIME:
                modify(-1,-1,-1,-1,date);
                break;

        }
    }
}

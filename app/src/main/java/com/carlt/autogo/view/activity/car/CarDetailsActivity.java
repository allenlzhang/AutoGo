package com.carlt.autogo.view.activity.car;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.common.dialog.DialogEdit;
import com.carlt.autogo.entry.car.CarInfo;
import com.carlt.autogo.entry.user.BaseError;
import com.carlt.autogo.presenter.car.CarDetailsPresenter;
import com.carlt.autogo.presenter.car.ICarDetailsView;
import com.carlt.autogo.utils.MyTimeUtils;
import com.carlt.autogo.view.activity.activate.ActivateStepActivity;
import com.carlt.autogo.view.activity.activate.DeviceActivateActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Marlon on 2018/11/27.
 * 车辆详情
 */
@CreatePresenter(presenter = CarDetailsPresenter.class)
public class CarDetailsActivity extends BaseMvpActivity<CarDetailsPresenter> implements ICarDetailsView {
    @BindView(R.id.tvDetailsModel)
    TextView tvDetailsModel;//车型

    @BindView(R.id.llDetails)
    LinearLayout llDetails;//详情模块
    @BindView(R.id.tvDetailsBuyTime)
    TextView     tvDetailsBuyTime;//购车时间
    @BindView(R.id.llDetailsBuyTime)
    LinearLayout llDetailsBuyTime;
    @BindView(R.id.tvDetailsServiceCycle)
    TextView     tvDetailsServiceCycle;//上次保养里程
    @BindView(R.id.llDetailsServiceCycle)
    LinearLayout llDetailsServiceCycle;
    @BindView(R.id.tvDetailsServiceTime)
    TextView     tvDetailsServiceTime;//上次保养时间
    @BindView(R.id.llDetailsServiceTime)
    LinearLayout llDetailsServiceTime;
    @BindView(R.id.tvDetailsInsureTime)
    TextView     tvDetailsInsureTime;//上次投保时间
    @BindView(R.id.llDetailsInsureTime)
    LinearLayout llDetailsInsureTime;
    @BindView(R.id.tvDetailsAnnualTime)
    TextView     tvDetailsAnnualTime;//上次年检时间
    @BindView(R.id.llDetailsAnnualTime)
    LinearLayout llDetailsAnnualTime;
    @BindView(R.id.tvDetailsAuthStartTime)
    TextView     tvDetailsAuthStartTime;//授权开始时间
    @BindView(R.id.llDetailsAuthStart)
    LinearLayout llDetailsAuthStart;
    @BindView(R.id.tvDetailsAuthEndTime)
    TextView     tvDetailsAuthEndTime;//授权结束时间
    @BindView(R.id.llDetailsAuthEnd)
    LinearLayout llDetailsAuthEnd;

    @BindView(R.id.llDetailsDevice)
    LinearLayout llDetailsDevice;//设备模块
    @BindView(R.id.llDetailsRemote)
    LinearLayout llDetailsRemote;//远程
    @BindView(R.id.ivDetailsRemoteState)
    ImageView    ivDetailsRemoteState;//远程激活icon
    @BindView(R.id.tvDetailsRemoteState)
    TextView     tvDetailsRemoteState;//远程激活状态
    @BindView(R.id.llDetailsRecorder)
    LinearLayout llDetailsRecorder;//记录仪
    @BindView(R.id.ivDetailsRecorderState)
    ImageView    ivDetailsRecorderState;//记录仪icon
    @BindView(R.id.tvDetailsRecorderState)
    TextView     tvDetailsRecorderState;//记录仪状态
    @BindView(R.id.llDetailsMachine)
    LinearLayout llDetailsMachine;//车机
    @BindView(R.id.ivDetailsMachineState)
    ImageView    ivDetailsMachineState;//车机icon
    @BindView(R.id.tvDetailsMachineState)
    TextView     tvDetailsMachineState;//车机状态
    @BindView(R.id.tvDetailsRemote)
    TextView     tvDetailsRemote;//远程txt
    @BindView(R.id.tvDetailsRecorder)
    TextView     tvDetailsRecorder;//记录仪txt
    @BindView(R.id.tvDetailsMachine)
    TextView     tvDetailsMachine;//车机txt

    @BindView(R.id.btnCancelAuth)
    Button btnCancelAuth;//取消授权


    private             int type          = DETAILS_TYPE1;
    public static final int DETAILS_TYPE1 = 0;  //未激活未授权（我的爱车详情）
    public static final int DETAILS_TYPE2 = 1;  //已激活未授权（d）
    public static final int DETAILS_TYPE3 = 2;  //已激活授权中（我的爱车详情）
    public static final int DETAILS_TYPE4 = 3;  //（被授权车辆详情）
    private             int remoteStatus  = -1;   //远程状态
    private             int withTbox      = -1;   //前后装
    private             int authId        = 0; //授权id
    private             int carId         = 0;  //车辆id

    private TimePickerView pvCustomTime;

    private long buyDate       = -1;
    private long maintenMiles  = -1;
    private long maintenDate   = -1;
    private long applicantDate = -1;
    private long inspectTime   = -1;

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
        switch (type) {
            case DETAILS_TYPE1:
                llDetails.setVisibility(View.GONE);
                btnCancelAuth.setVisibility(View.GONE);
                break;
            case DETAILS_TYPE2:
                llDetailsAuthStart.setVisibility(View.GONE);
                llDetailsAuthEnd.setVisibility(View.GONE);
                btnCancelAuth.setVisibility(View.GONE);
                break;
            case DETAILS_TYPE3:
                break;
            case DETAILS_TYPE4:
                llDetailsDevice.setVisibility(View.GONE);
                btnCancelAuth.setVisibility(View.GONE);
                llDetails.setClickable(true);
                break;
        }
        carId = intent.getIntExtra("id", 0);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().ClientGetCarInfo(carId);
    }

    /**
     * 编辑车辆
     * @param buyDate
     *         购买时间
     * @param maintenMiles
     *         保养里程
     * @param maintenDate
     *         上次保养时间
     * @param applicantDate
     *         上次投保时间
     * @param inspectTime
     *         上次年检时间
     */
    @SuppressLint("CheckResult")
    private void modify(final long buyDate, final long maintenMiles, final long maintenDate, final long applicantDate, final long inspectTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", carId);
        if (buyDate >= 0) {
            map.put("buyDate", buyDate);
            this.buyDate = buyDate;
        }
        if (maintenMiles >= 0) {
            map.put("maintenMiles", maintenMiles);
            this.maintenMiles = maintenMiles;
        }
        if (maintenDate >= 0) {
            map.put("maintenDate", maintenDate);
            this.maintenDate = maintenDate;
        }
        if (applicantDate >= 0) {
            map.put("applicantDate", applicantDate);
            this.applicantDate = applicantDate;
        }
        if (inspectTime >= 0) {
            map.put("inspectTime", inspectTime);
            this.inspectTime = inspectTime;
        }
        getPresenter().modify(map);
    }

    public void modifySuccess(long buyDate, long maintenMiles, long maintenDate, long applicantDate, long inspectTime) {
        if (buyDate >= 0) {
            tvDetailsBuyTime.setText(MyTimeUtils.formatDateGetDaySecend(buyDate));
            this.buyDate = -1;
        }
        if (maintenMiles >= 0) {
            tvDetailsServiceCycle.setText(String.valueOf(maintenMiles) + " km");
            this.maintenMiles = -1;
        }
        if (maintenDate >= 0) {
            tvDetailsServiceTime.setText(MyTimeUtils.formatDateGetDaySecend(maintenDate));
            this.maintenDate = -1;
        }
        if (applicantDate >= 0) {
            tvDetailsInsureTime.setText(MyTimeUtils.formatDateGetDaySecend(applicantDate));
            this.applicantDate = -1;
        }
        if (inspectTime >= 0) {
            tvDetailsAnnualTime.setText(MyTimeUtils.formatDateGetDaySecend(inspectTime));
            this.inspectTime = -1;
        }
    }

    @OnClick({R.id.llDetailsRemote, R.id.llDetailsRecorder, R.id.llDetailsMachine, R.id.btnCancelAuth, R.id.llDetailsBuyTime
            , R.id.llDetailsServiceCycle, R.id.llDetailsServiceTime, R.id.llDetailsInsureTime, R.id.llDetailsAnnualTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llDetailsRemote:
                clickRemote();
                break;
            case R.id.llDetailsRecorder:
                break;
            case R.id.llDetailsMachine:
                //                showEditDilog();
                break;
            case R.id.btnCancelAuth:
                getPresenter().cancelAuth(authId);
                break;
            case R.id.llDetailsBuyTime:
                //                showDatePicker(BUYTIME,tvDetailsBuyTime.getText().toString());
                if (type != DETAILS_TYPE4) {
                    initCustomTimePicker(tvDetailsBuyTime.getText().toString());
                    pvCustomTime.show(tvDetailsBuyTime);
                }
                break;
            case R.id.llDetailsServiceCycle:
                if (type != DETAILS_TYPE4) {
                    showEditDilog();
                }
                break;
            case R.id.llDetailsServiceTime:
                if (type != DETAILS_TYPE4) {
                    initCustomTimePicker(tvDetailsServiceTime.getText().toString());
                    pvCustomTime.show(tvDetailsServiceTime);
                }
                break;
            case R.id.llDetailsInsureTime:
                if (type != DETAILS_TYPE4) {
                    initCustomTimePicker(tvDetailsInsureTime.getText().toString());
                    pvCustomTime.show(tvDetailsInsureTime);
                }
                break;
            case R.id.llDetailsAnnualTime:
                if (type != DETAILS_TYPE4) {
                    initCustomTimePicker(tvDetailsAnnualTime.getText().toString());
                    pvCustomTime.show(tvDetailsAnnualTime);
                }
                break;
        }
    }

    private void clickRemote() {
        Intent intent = new Intent();
        switch (remoteStatus) {
            case 0:
                intent.setClass(CarDetailsActivity.this, DeviceActivateActivity.class);
                break;
            case 1:
                intent.setClass(CarDetailsActivity.this, ActivateStepActivity.class);
                break;
            case 2:
                intent.setClass(CarDetailsActivity.this, ActivateStepActivity.class);
                break;
            case 3:
                intent.setClass(CarDetailsActivity.this, ActivateStepActivity.class);
                break;
            default:
                break;
        }
        //        if (remoteStatus == 1) {
        //            intent.setClass(CarDetailsActivity.this, ActivateStepActivity.class);
        //        } else if (remoteStatus == 2) {
        //            intent.setClass(CarDetailsActivity.this, ActivateStepActivity.class);
        //        } else {
        //            intent.setClass(CarDetailsActivity.this, DeviceActivateActivity.class);
        //        }
        intent.putExtra("carId", carId);
        intent.putExtra("withTbox", withTbox);
        startActivity(intent);
    }

    public void setData(CarInfo data) {
        remoteStatus = data.remoteStatus;
        authId = data.authId;
        withTbox = data.withTbox;
        switch (remoteStatus) {

            case 1:
            case 3:
                ivDetailsRemoteState.setImageResource(R.mipmap.ic_remote_activating_big);
                tvDetailsRemote.setTextColor(getResources().getColor(R.color.colorActivating));
                tvDetailsRemoteState.setTextColor(getResources().getColor(R.color.colorActivating));
                tvDetailsRemoteState.setText("激活中");
                break;
            case 2:
                ivDetailsRemoteState.setImageResource(R.mipmap.ic_remote_activated_big);
                tvDetailsRemote.setTextColor(getResources().getColor(R.color.colorBlue));
                tvDetailsRemoteState.setTextColor(getResources().getColor(R.color.colorBlue));
                tvDetailsRemoteState.setText("已激活");
                break;

            case 0:
            default:
                tvDetailsRemoteState.setText("未激活");
                ivDetailsRemoteState.setImageResource(R.mipmap.ic_remote_activate_big);
                tvDetailsRemote.setTextColor(getResources().getColor(R.color.textColorGray));
                tvDetailsRemoteState.setTextColor(getResources().getColor(R.color.textColorGray));
                break;
        }

        if (!TextUtils.isEmpty(data.carName)) {
            tvDetailsModel.setText(data.carName);
        }
        if (data.buyDate != 0) {
            tvDetailsBuyTime.setText(MyTimeUtils.formatDateGetDaySecend(data.buyDate));
        } else {
            tvDetailsBuyTime.setText("--");
        }
        if (data.maintenMiles != 0) {
            tvDetailsServiceCycle.setText(String.valueOf(data.maintenMiles) + " km");
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


    }

    /**
     * 里程输入弹窗
     */
    private void showEditDilog() {
        DialogEdit dialogEdit = new DialogEdit(this, R.style.DialogCommon);
        dialogEdit.setTitle("上次保养里程");
        dialogEdit.setOnClickListener(new DialogEdit.DialogConfirmClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(String txt) {
                if (!TextUtils.isEmpty(txt)) {
                    modify(-1, Long.valueOf(txt), -1, -1, -1);
                }
            }
        });
        dialogEdit.show();
    }

    /**
     * 初始化日期选择框
     * @param date
     */
    private void initCustomTimePicker(String date) {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Date time = endDate.getTime();
        if (TextUtils.isEmpty(date) || date.equals("--")) {
            date = dateFormat.format(time);
        }
        startDate.set(2000, 0, 1);
        String format = dateFormat.format(time);
        String[] split = format.split("-");
        String[] selectedSplit = date.split("-");
        selectedDate.set(Integer.parseInt(selectedSplit[0]), Integer.parseInt(selectedSplit[1]) - 1, Integer.parseInt(selectedSplit[2]));
        endDate.set(Integer.valueOf(split[0]), Integer.valueOf(split[1]) - 1, Integer.valueOf(split[2]));
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                LogUtils.e(date.getTime() + "");
                switch (v.getId()) {
                    case R.id.tvDetailsBuyTime:
                        modify(date.getTime() / 1000, -1, -1, -1, -1);
                        break;
                    case R.id.tvDetailsServiceTime:
                        modify(-1, -1, date.getTime() / 1000, -1, -1);
                        break;
                    case R.id.tvDetailsInsureTime:
                        modify(-1, -1, -1, date.getTime() / 1000, -1);
                        break;
                    case R.id.tvDetailsAnnualTime:
                        modify(-1, -1, -1, -1, date.getTime() / 1000);
                        break;
                }
            }
        })

                .setDate(selectedDate)
                //                .setLayoutRes(R.layout.time_edit_dialog, new CustomListener() {
                //                    @Override
                //                    public void customLayout(View v) {
                //                        final TextView _OK = (TextView) v.findViewById(R.id.sex_change_OK);
                //                        final TextView _cancel = (TextView) v.findViewById(R.id.sex_change_cancel);
                //                        _OK.setOnClickListener(new View.OnClickListener() {
                //                            @Override
                //                            public void onClick(View v) {
                //                                pvCustomTime.returnData();
                //                                pvCustomTime.dismiss();
                //                            }
                //                        });
                //
                //                        _cancel.setOnClickListener(new View.OnClickListener() {
                //                            @Override
                //                            public void onClick(View v) {
                //                                pvCustomTime.dismiss();
                //                            }
                //                        });
                //
                //                    }
                //                })
                .setSubCalSize(14)
                .setContentSize(18)
                .setRangDate(startDate, endDate)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(2.0f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFFFFFFFF)
                .build();
    }

    /**
     * 获取车辆信息成功回调
     * @param carInfo
     */
    @Override
    public void getCarInfoSuccess(CarInfo carInfo) {
        if (carInfo.err != null) {
            ToastUtils.showShort(carInfo.err.msg);
        } else {
            setData(carInfo);
        }
    }

    /**
     * 修改成功回调
     * @param baseError
     */
    @Override
    public void modifySuccess(BaseError baseError) {
        if (baseError.msg != null) {
            ToastUtils.showShort(baseError.msg);
        } else {
            ToastUtils.showShort("修改成功");
            modifySuccess(buyDate, maintenMiles, maintenDate, applicantDate, inspectTime);
        }
    }

    /**
     * 取消授权成功回调
     * @param baseError
     */
    @Override
    public void cancelAuthSuccess(BaseError baseError) {
        if (baseError != null) {
            if (!TextUtils.isEmpty(baseError.msg)) {
                ToastUtils.showShort(baseError.msg);
            } else {
                ToastUtils.showShort("取消授权成功");
                btnCancelAuth.setVisibility(View.GONE);
                finish();
            }
        }
    }
}

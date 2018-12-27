package com.carlt.autogo.view.activity.activate;

import android.annotation.SuppressLint;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.car.ActivateStepInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class ActivateStepActivity extends BaseMvpActivity {

    @BindView(R.id.step_view)
    StepView  stepView;
    @BindView(R.id.ivState)
    ImageView ivState;
    @BindView(R.id.tvState)
    TextView  tvState;
    @BindView(R.id.tvDes)
    TextView  tvDes;
    @BindView(R.id.btnRetry)
    Button    btnRetry;

    //    @Override
    //    protected void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        setContentView(R.layout.activity_activate_step);
    //        ButterKnife.bind(this);
    //    }

    @Override
    protected int getContentView() {
        return R.layout.activity_activate_step;
    }

    @Override
    public void init() {
        setTitleText("设备激活");
        //        stepView.go(3, true);
        //        stepView.setOnStepClickListener(new StepView.OnStepClickListener() {
        //            @Override
        //            public void onStepClick(int step) {
        //                switch (step) {
        //                    case 0:
        //                        ivState.setImageResource(R.mipmap.iv_activate_success);
        //                        tvState.setText("核实车辆信息成功");
        //                        break;
        //                    case 1:
        //                        ivState.setImageResource(R.mipmap.iv_activate_success);
        //                        tvState.setText("核实车辆信息成功");
        //                        break;
        //                    case 2:
        //                        ivState.setImageResource(R.mipmap.iv_activate_success);
        //                        tvState.setText("核实车辆信息成功");
        //                        break;
        //                    case 3:
        //                        ivState.setImageResource(R.mipmap.iv_activate_ing);
        //                        tvState.setText("核实车辆信息中");
        //                        break;
        //                }
        //            }
        //        });
        initActivateLogs();
    }

    @SuppressLint("CheckResult")
    private void initActivateLogs() {
        dialog.show();
        int carId = getIntent().getIntExtra("carId", 0);
        LogUtils.e(carId);
        Map<String, Object> params = new HashMap<>();
        params.put("carID", carId);
        ClientFactory.def(CarService.class).getLogs(params)
                .subscribe(new Consumer<ActivateStepInfo>() {
                    @Override
                    public void accept(ActivateStepInfo info) throws Exception {
                        LogUtils.e(info);
                        initStepView(info);
                        dialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialog.dismiss();
                    }
                });
    }

    private void initStepView(ActivateStepInfo info) {
        if (info.err == null) {
            final List<ActivateStepInfo.StepsBean> steps = info.steps;
            final ArrayList<ActivateStepInfo.StepsBean> logs = new ArrayList<>();
            for (ActivateStepInfo.StepsBean step : steps) {
                if (step.isSuccess == 1) {
                    logs.add(step);
                }
            }

            stepView.setStepsNumber(steps.size());
            stepView.go(logs.size(), true);
            ActivateStepInfo.StepsBean stepsBean = steps.get(logs.size());
            if (stepsBean.isSuccess == -1) {
                tvState.setText(stepsBean.title);
                tvDes.setText(stepsBean.description);
                ivState.setImageResource(R.mipmap.iv_activate_ing);
            } else if (stepsBean.isSuccess == 2) {
                tvState.setText(stepsBean.title);
                tvDes.setText(stepsBean.failReason);
                ivState.setImageResource(R.mipmap.iv_activate_err);
            }

            stepView.setOnStepClickListener(new StepView.OnStepClickListener() {
                @Override
                public void onStepClick(int step) {
                    if (step < logs.size()) {
                        ActivateStepInfo.StepsBean logsBean = logs.get(step);
                        tvState.setText(logsBean.title);
                        if (logsBean.isSuccess == 1) {
                            tvDes.setText(logsBean.description);
                            ivState.setImageResource(R.mipmap.iv_activate_success);
                        } else {
                            ivState.setImageResource(R.mipmap.iv_activate_err);
                            tvDes.setText(logsBean.failReason);
                        }
                    } else if (step == logs.size()) {
                        ActivateStepInfo.StepsBean stepsBean = steps.get(step);
                        tvState.setText(stepsBean.title);
                        ivState.setImageResource(R.mipmap.iv_activate_ing);
                        tvDes.setText(stepsBean.description);
                    } else {
                        ActivateStepInfo.StepsBean stepsBean = steps.get(step);
                        tvState.setText(stepsBean.title);
                        ivState.setImageResource(R.mipmap.iv_activate_no_conn);
                        tvDes.setText(stepsBean.description);
                    }

                }
            });
        } else {
            showToast(info.err.msg);
        }

    }

    @OnClick(R.id.btnRetry)
    public void onViewClicked() {
    }
}

package com.carlt.autogo.view.activity.activate;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.entry.car.ActivateStepInfo;
import com.carlt.autogo.net.base.ClientFactory;
import com.carlt.autogo.net.service.CarService;
import com.carlt.autogo.view.activity.car.DeviceActivateActivity;
import com.carlt.autogo.view.activity.car.ModifyCarActivity;
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
    @BindView(R.id.ivLoading)
    ImageView ivLoading;
    @BindView(R.id.tvState)
    TextView  tvState;
    @BindView(R.id.tvDes)
    TextView  tvDes;
    @BindView(R.id.tvErr)
    TextView  tvErr;
    @BindView(R.id.tvTip)
    TextView  tvTip;
    @BindView(R.id.btnRetry)
    Button    btnRetry;
    private int carId;

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
        initActivateLogs();
        initAnimator();
    }

    @SuppressLint("CheckResult")
    private void initActivateLogs() {
        dialog.show();
        carId = getIntent().getIntExtra("carId", 0);
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
                    btnRetry.setVisibility(View.GONE);
                    tvTip.setVisibility(View.GONE);
                    logs.add(step);
                } else if (step.isSuccess == 2) {
                    btnRetry.setVisibility(View.VISIBLE);
                    tvTip.setVisibility(View.VISIBLE);
                }
            }
            ActivateStepInfo.StepsBean sixStep = steps.get(4);
            sixStep.failCode = 2226;
            if (sixStep.isSuccess == 2) {
                if (sixStep.failCode == 2226) {
                    ERR_TYPE = 1;
                } else if (sixStep.failCode == 2201) {
                    ERR_TYPE = 0;
                }
            }

            stepView.setStepsNumber(steps.size());
            ActivateStepInfo.StepsBean stepsBean;
            if (logs.size() == steps.size()) {
                stepView.go(logs.size() - 1, true);
                stepView.done(true);
                stepsBean = steps.get(logs.size() - 1);
            } else {
                stepView.go(logs.size(), true);
                stepsBean = steps.get(logs.size());
            }


            tvDes.setText(stepsBean.description);
            tvErr.setText(stepsBean.failReason);
            tvState.setText(stepsBean.title);
            if (stepsBean.isSuccess == -1) {
                tvState.setText(stepsBean.title);
                ivLoading.setVisibility(View.VISIBLE);
                //                ivState.setImageResource(R.mipmap.iv_activate_ing);
            } else if (stepsBean.isSuccess == 2) {
                mAnimator.cancel();
                ivLoading.setVisibility(View.GONE);
                ivState.setImageResource(R.mipmap.iv_activate_err);
            } else if (stepsBean.isSuccess == 1) {
                mAnimator.cancel();
                ivLoading.setVisibility(View.GONE);
                ivState.setImageResource(R.mipmap.iv_activate_success);
            }

            //            stepView.setOnStepClickListener(new StepView.OnStepClickListener() {
            //                @Override
            //                public void onStepClick(int step) {
            //                    if (step < logs.size()) {
            //                        ActivateStepInfo.StepsBean logsBean = logs.get(step);
            //                        tvState.setText(logsBean.title);
            //                        tvDes.setText(logsBean.description);
            //                        tvErr.setText(logsBean.failReason);
            //                        if (logsBean.isSuccess == 1) {
            //                            ivState.setImageResource(R.mipmap.iv_activate_success);
            //                        } else {
            //                            ivState.setImageResource(R.mipmap.iv_activate_err);
            //                        }
            //                    } else if (step == logs.size()) {
            //                        ActivateStepInfo.StepsBean stepsBean = steps.get(step);
            //                        if (stepsBean.isSuccess == -1) {
            //                            tvState.setText(stepsBean.title);
            //                            ivState.setImageResource(R.mipmap.iv_activate_ing);
            //                        } else if (stepsBean.isSuccess == 2) {
            //                            tvState.setText(stepsBean.title);
            //                            ivState.setImageResource(R.mipmap.iv_activate_err);
            //                        }
            //
            //                        tvDes.setText(stepsBean.description);
            //                        tvErr.setText(stepsBean.failReason);
            //                    } else {
            //                        ActivateStepInfo.StepsBean stepsBean = steps.get(step);
            //                        tvState.setText(stepsBean.title);
            //                        ivState.setImageResource(R.mipmap.iv_activate_no_conn);
            //                        tvDes.setText(stepsBean.description);
            //                    }
            //
            //                }
            //            });
        } else {
            showToast(info.err.msg);
        }


    }

    public ObjectAnimator mAnimator;

    private void initAnimator() {
        mAnimator = ObjectAnimator.ofFloat(ivLoading, "rotation", 0f, 359f);
        mAnimator.setRepeatCount(-1);
        mAnimator.setDuration(2000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.start();

    }

    private int ERR_TYPE;

    @OnClick(R.id.btnRetry)
    public void onViewClicked() {
        int withTbox = getIntent().getIntExtra("withTbox", 0);
        Intent intent = new Intent();
        switch (ERR_TYPE) {
            case 0:
                intent.setClass(this, DeviceActivateActivity.class);
                break;
            case 1:
                intent.setClass(this, ModifyCarActivity.class);
                break;
            default:
                break;
        }
        intent.putExtra("withTbox", withTbox);
        intent.putExtra("carId", carId);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }
}

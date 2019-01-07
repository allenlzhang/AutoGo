package com.carlt.autogo.view.activity.activate;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.carlt.autogo.basemvp.CreatePresenter;
import com.carlt.autogo.entry.car.ActivateStepInfo;
import com.carlt.autogo.presenter.activite.ActivateStepPresenter;
import com.carlt.autogo.presenter.activite.IActivateStepView;
import com.carlt.autogo.utils.ActivityControl;
import com.carlt.autogo.view.activity.car.CarDetailsActivity;
import com.carlt.autogo.view.activity.car.ModifyCarActivity;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
 /**
  * Description : 激活步骤展示页面
  * Author     : zhanglei
  * Date       : 2019/1/5
  */
@CreatePresenter(presenter = ActivateStepPresenter.class)
public class ActivateStepActivity extends BaseMvpActivity<ActivateStepPresenter> implements IActivateStepView {

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

    Disposable disposable;
    int        time = 1800;

    @SuppressLint("CheckResult")
    private void initActivateLogs() {
        //        dialog.show();
        carId = getIntent().getIntExtra("carId", 0);
        LogUtils.e(carId);
        final Map<String, Object> params = new HashMap<>();
        params.put("carID", carId);
        getPresenter().getStepInfos(params, true);
        disposable = Observable.interval(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        getPresenter().getStepInfos(params, false);
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        time--;
                        if (time <= 0) {
                            disposable.dispose();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(throwable.getMessage());
                    }
                });

        //        ClientFactory.def(CarService.class).getLogs(params)
        //                .subscribe(new Consumer<ActivateStepInfo>() {
        //                    @Override
        //                    public void accept(ActivateStepInfo info) throws Exception {
        //                        LogUtils.e(info);
        //                        initStepView(info);
        //                        dialog.dismiss();
        //                    }
        //                }, new Consumer<Throwable>() {
        //                    @Override
        //                    public void accept(Throwable throwable) throws Exception {
        //                        dialog.dismiss();
        //                    }
        //                });

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
                    disposable.dispose();
                }
            }
            ActivateStepInfo.StepsBean sixStep = steps.get(4);
            //            sixStep.failCode = 2226;
            if (sixStep.isSuccess == 2) {
                if (sixStep.failCode == 2226) {
                    ERR_TYPE = 1;
                } else if (sixStep.failCode == 2201) {
                    ERR_TYPE = 0;
                }
            }

            stepView.setStepsNumber(steps.size());
            ActivateStepInfo.StepsBean stepsBean;
            LogUtils.e(logs.size() + steps.size());
            if (logs.size() == steps.size()) {
                btnRetry.setText("开启智能驾驶之旅");
                btnRetry.setVisibility(View.VISIBLE);
                ERR_TYPE = 2;
                disposable.dispose();
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
        int withTbox = getIntent().getIntExtra("withTbox", -1);
//        LogUtils.e(withTbox);

        switch (ERR_TYPE) {
            case 0:
                Intent intent = new Intent();
                intent.putExtra("withTbox", withTbox);
                intent.putExtra("carId", carId);
                intent.setClass(this, DeviceActivateActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent();
                intent1.putExtra("withTbox", withTbox);
                intent1.putExtra("carId", carId);
                intent1.setClass(this, ModifyCarActivity.class);
                startActivity(intent1);
                break;
            case 2:
                for (Activity activity : ActivityControl.mActivityList) {
                    if (activity instanceof CarDetailsActivity) {
                        activity.finish();
                    }

                }
                break;
            default:
                break;
        }


        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void getStepInfoFinish(ActivateStepInfo info) {
        initStepView(info);
    }

    @Override
    public void getStepInfoErr(Throwable throwable) {

    }
}

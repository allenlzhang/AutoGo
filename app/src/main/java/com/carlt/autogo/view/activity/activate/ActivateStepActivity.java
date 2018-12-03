package com.carlt.autogo.view.activity.activate;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;
import com.shuhart.stepview.StepView;

import butterknife.BindView;
import butterknife.OnClick;

public class ActivateStepActivity extends BaseMvpActivity {

    @BindView(R.id.step_view)
    StepView  stepView;
    @BindView(R.id.ivState)
    ImageView ivState;
    @BindView(R.id.tvState)
    TextView  tvState;
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
        stepView.go(3, true);
        stepView.setOnStepClickListener(new StepView.OnStepClickListener() {
            @Override
            public void onStepClick(int step) {
                switch (step) {
                    case 0:
                        ivState.setImageResource(R.mipmap.iv_activate_success);
                        tvState.setText("核实车辆信息成功");
                        break;
                    case 1:
                        ivState.setImageResource(R.mipmap.iv_activate_success);
                        tvState.setText("核实车辆信息成功");
                        break;
                    case 2:
                        ivState.setImageResource(R.mipmap.iv_activate_success);
                        tvState.setText("核实车辆信息成功");
                        break;
                    case 3:
                        ivState.setImageResource(R.mipmap.iv_activate_ing);
                        tvState.setText("核实车辆信息中");
                        break;
                }
            }
        });
    }

    @OnClick(R.id.btnRetry)
    public void onViewClicked() {
    }
}

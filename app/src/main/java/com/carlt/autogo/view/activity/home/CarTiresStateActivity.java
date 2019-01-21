package com.carlt.autogo.view.activity.home;

import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.base.BaseMvpActivity;

import butterknife.BindView;

/**
 * Created by Marlon on 2019/1/18.
 */
public class CarTiresStateActivity extends BaseMvpActivity {
    @BindView(R.id.txtTiresState)
    TextView txtTiresState;
    @BindView(R.id.txtTireTime)
    TextView txtTireTime;
    @BindView(R.id.ivTireLeftFront)
    ImageView ivTireLeftFront;
    @BindView(R.id.ivTireRightFront)
    ImageView ivTireRightFront;
    @BindView(R.id.ivTireLeftBehind)
    ImageView ivTireLeftBehind;
    @BindView(R.id.ivTireRightBehind)
    ImageView ivTireRightBehind;
    @BindView(R.id.txtLeftFrontT)
    TextView txtLeftFrontT;
    @BindView(R.id.txtLeftFrontPressure)
    TextView txtLeftFrontPressure;
    @BindView(R.id.ivLeftFrontState)
    ImageView ivLeftFrontState;
    @BindView(R.id.rlLeftFront)
    RelativeLayout rlLeftFront;
    @BindView(R.id.txtRightFrontT)
    TextView txtRightFrontT;
    @BindView(R.id.txtRightFrontPressure)
    TextView txtRightFrontPressure;
    @BindView(R.id.ivRightFrontState)
    ImageView ivRightFrontState;
    @BindView(R.id.rlRightFront)
    RelativeLayout rlRightFront;
    @BindView(R.id.txtLeftBehindT)
    TextView txtLeftBehindT;
    @BindView(R.id.txtLeftBehindPressure)
    TextView txtLeftBehindPressure;
    @BindView(R.id.ivLeftBehindState)
    ImageView ivLeftBehindState;
    @BindView(R.id.rlLeftBehind)
    RelativeLayout rlLeftBehind;
    @BindView(R.id.txtRightBehindT)
    TextView txtRightBehindT;
    @BindView(R.id.txtRightBehindPressure)
    TextView txtRightBehindPressure;
    @BindView(R.id.ivRightBehindState)
    ImageView ivRightBehindState;
    @BindView(R.id.rlRightBehind)
    RelativeLayout rlRightBehind;
    @BindView(R.id.leftFrontPressureSymbol)
    TextView leftFrontPressureSymbol;
    @BindView(R.id.rightFrontPressureSymbol)
    TextView rightFrontPressureSymbol;
    @BindView(R.id.leftBehindPressureSymbol)
    TextView leftBehindPressureSymbol;
    @BindView(R.id.rightBehindPressureSymbol)
    TextView rightBehindPressureSymbol;

    @Override
    protected int getContentView() {
        return R.layout.activity_tires;
    }

    @Override
    public void init() {
        setTitleText("胎压监测");
        tvBaseRight.setBackground(getResources().getDrawable(R.drawable.icon_flush));
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/DIN1451.ttf");
        txtLeftFrontT.setTypeface(typeface);
        txtLeftFrontPressure.setTypeface(typeface);
        leftFrontPressureSymbol.setTypeface(typeface);
        txtRightFrontT.setTypeface(typeface);
        txtRightFrontPressure.setTypeface(typeface);
        rightFrontPressureSymbol.setTypeface(typeface);
        txtLeftBehindT.setTypeface(typeface);
        txtLeftBehindPressure.setTypeface(typeface);
        leftBehindPressureSymbol.setTypeface(typeface);
        txtRightBehindT.setTypeface(typeface);
        txtRightBehindPressure.setTypeface(typeface);
        rightBehindPressureSymbol.setTypeface(typeface);
    }

}

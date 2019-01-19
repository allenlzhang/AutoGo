package com.carlt.autogo.common.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.carlt.autogo.R;
import com.carlt.autogo.widget.TempControlView;

import razerdp.basepopup.BasePopupWindow;

/**
 * Description : 空调
 * Author     : zhanglei
 * Date       : 2019/1/19
 */
public class AirConditionerPopup extends BasePopupWindow {


    private TempControlView mTempControlView;

    public AirConditionerPopup(Context context) {
        super(context);
        //        setPopupGravity(Gravity.BOTTOM);
        mTempControlView = findViewById(R.id.tempControlView);
        final TextView tvTemp = findViewById(R.id.tvTemp);
        final TextView tvDegree = findViewById(R.id.tvDegree);
        mTempControlView.setOnTempChangeListener(new TempControlView.OnTempChangeListener() {
            @Override
            public void changeUp(int temp) {

            }

            @Override
            public void changeMove(int temp) {
                tvTemp.setText(temp + "");
                tvDegree.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_air_conditioner);
    }


}

package com.carlt.autogo.common.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import com.carlt.autogo.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Description :车窗
 * Author     : zhanglei
 * Date       : 2019/1/19
 */
public class WindowDoorPopup extends BasePopupWindow {


    public WindowDoorPopup(Context context) {
        super(context);
        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_windown_door);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 300);
    }

}

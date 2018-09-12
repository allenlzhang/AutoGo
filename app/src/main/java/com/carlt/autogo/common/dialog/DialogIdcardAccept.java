package com.carlt.autogo.common.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlt.autogo.R;

import butterknife.BindView;
import butterknife.OnClick;

public class DialogIdcardAccept extends BaseDialog {
    ItemOnclickListner listner ;
    @BindView(R.id.head)LinearLayout head;
    @BindView(R.id.ll_show)LinearLayout llShow;
    @BindView(R.id.tv_by_carmera)TextView tvByCarmera;
    @BindView(R.id.tv_by_ablun)TextView tvByAblun;
    @BindView(R.id.tv_cancle)TextView tvCancle;
    @BindView(R.id.tv_ok)TextView tvOk;


    public DialogIdcardAccept(@NonNull Context context) {
        super(context);
    }

    @Override
    void setWindowParams() {

    }

    @Override
    int setRes() {
        return R.layout.dialog_idcard_accept;
    }

    @Override
    void init() {

    }

    @OnClick({R.id.tv_ok})
    public void onClickOk(){

        llShow.setVisibility(View.GONE);
        head.setVisibility(View.VISIBLE);
        layoutParams.gravity = Gravity.BOTTOM;
        dismiss();
        show();
    }

    @OnClick({R.id.tv_by_carmera,R.id.tv_by_ablun ,R.id.tv_cancle})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_by_carmera:
                if(listner != null){
                    listner.byCermera();
                }
                dismiss();
                break;

            case R.id.tv_by_ablun:
                if(listner != null){
                    listner.byAblum();
                }
                dismiss();
                break;

            case R.id.tv_cancle:

                dismiss();
                break;
        }

    }
    public void setListner(ItemOnclickListner listner){
        this.listner = listner ;
    }
    public   interface ItemOnclickListner {

        void byCermera();
        void byAblum();

    }
}

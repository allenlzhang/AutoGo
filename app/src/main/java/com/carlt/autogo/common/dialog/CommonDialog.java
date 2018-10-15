package com.carlt.autogo.common.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.carlt.autogo.R;

public class CommonDialog {


    public static void createDialogNotitle(Context context, String title1, String title2, String left, String right, boolean cancelable, final DialogWithTitleClick click) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_common, null);
        //        final Dialog dialogI = new Dialog(context, R.style.DialogCommon);

        builder.setView(view);
        builder.create();
        final AlertDialog alertDialog = builder.show();
        TextView tvRight = view.findViewById(R.id.tvDialogRight);
        TextView tvLeft = view.findViewById(R.id.tvDialogLeft);
        TextView tvTitle1 = view.findViewById(R.id.tvTitle1);
        TextView tvTitle2 = view.findViewById(R.id.tvTitle2);
        alertDialog.setCancelable(cancelable);
        tvRight.setText(right);
        tvLeft.setText(left);
        tvTitle1.setText(title2);
        if (TextUtils.isEmpty(title1)) {
            tvTitle1.setVisibility(View.GONE);
        } else {
            tvTitle1.setVisibility(View.VISIBLE);
            tvTitle1.setText(title1);
        }
        if (TextUtils.isEmpty(title2)) {
            tvTitle2.setVisibility(View.GONE);
        } else {
            tvTitle2.setVisibility(View.VISIBLE);
            tvTitle2.setText(title2);
        }
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                click.onRightClick();
            }
        });
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                click.onLeftClick();
            }
        });

        //                int w = (int) (AutoGoApp.ScreenDensity * 300);
        //        ViewGroup.LayoutParams parm = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //        dialogI.setContentView(view);
        //        dialogI.show();

    }

    public interface DialogWithTitleClick {

        void onLeftClick();

        void onRightClick();
    }
}

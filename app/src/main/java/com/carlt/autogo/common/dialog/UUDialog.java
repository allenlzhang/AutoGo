package com.carlt.autogo.common.dialog;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.carlt.autogo.R;


import butterknife.BindView;


public class UUDialog extends BaseDialog {


	@BindView(R.id.progress_img_animate)
	ImageView progressImgAnimate ;
	protected TextView content;

	protected TextView title;
	 ;
	public ObjectAnimator 	animator ;

	public UUDialog(@NonNull Context context, int style) {
		super(context, style);
	}

	@Override
	void setWindowParams() {

	}

	@Override
	int setRes() {
		return R.layout.dialog_progress;
	}

	@Override
	void init() {
		animator = ObjectAnimator.ofFloat(progressImgAnimate, "rotation", 0f, 359f);
		animator.setRepeatCount(-1);
		animator.setDuration(500);
		animator.start();

	}

	public void setContentText(String t) {
		if (content != null) {
			content.setText(t);
		}
	}

	public void setTitleText(String t) {
		if (title != null) {
//			title.setText(t);
		}
	}

	@Override
	public void show() {
		super.show();
		animator.start();
	}

	@Override
	public void dismiss() {
		super.dismiss();

		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				animator.cancel();
			}
		});
	}
}

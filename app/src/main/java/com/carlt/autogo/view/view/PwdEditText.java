/*--------------------------------------------------
 * Copyright (C) 2015 The Android Y-CarPlus Project
 *                http://www.yesway.cn/
 * 创建时间：2015-12-16
 * 内容说明：
 * 
 * 变更时间：
 * 变更说明：
 * -------------------------------------------------- */

package com.carlt.autogo.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * 自定义密码输入框
 */
public class PwdEditText extends android.support.v7.widget.AppCompatEditText {

    /**
     * 间隔
     */
    private final int PWD_SPACING = 1;

    /**
     * 密码大小
     */
    private final int PWD_SIZE = 15;

    /**
     * 密码长度
     */
    private final int PWD_LENGTH = 6;

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 宽度
     */
    private int mWidth;

    /**
     * 高度
     */
    private int mHeight;

    /**
     * 密码框
     */
    private Rect mRect;

    /**
     * 密码画笔
     */
    private Paint mPwdPaint;

    /**
     * 密码框画笔
     */
    private Paint mRectPaint;

    /**
     * 分割线画笔
     */
    private Paint mLinePaint;

    /**
     * 输入的密码长度
     */
    private int mInputLength;

    /**
     * 输入监听
     */
    private OnInputListener mOnInputListener;

    /**
     * 构造方法
     * 
     * @param context
     * @param attrs
     */
    public PwdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 初始化密码画笔
        mPwdPaint = new Paint();
        mPwdPaint.setColor(Color.BLACK);
        mPwdPaint.setStyle(Paint.Style.FILL);
        mPwdPaint.setAntiAlias(true);
        // 初始化密码框
        mRectPaint = new Paint();
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setColor(Color.TRANSPARENT);
        mRectPaint.setAntiAlias(true);
        // 初始化分割线
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(Color.parseColor("#cccccc"));
        mLinePaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();

        // 这几行代码非常关键，大家可以注释点在看看效果
        // 带圆角
         Paint paintFrame = new Paint();
         paintFrame.setColor(Color.parseColor("#cccccc"));
         paintFrame.setStrokeWidth(1);
         paintFrame.setStyle(Paint.Style.STROKE);
         RectF outerRect1 = new RectF(0, 0, mWidth, mHeight);
//         canvas.drawRoundRect(outerRect1, 10, 10, paintFrame);
        //
        RectF outerRect2 = new RectF(2, 2, mWidth - 2, mHeight - 2);
        Paint paintContent = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintContent.setColor(Color.WHITE);
        paintContent.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(outerRect2, 10, 10, paintContent);

        // 不带圆角
        // Paint paintFrame = new Paint();
        // paintFrame.setColor(Color.parseColor("#BDBDBD"));
        // paintFrame.setStrokeWidth(2);
        // paintFrame.setStyle(Paint.Style.STROKE);
//         canvas.drawRect(0, 0, mWidth, mHeight, paintContent);

        // Paint paintContent = new Paint();
        // paintContent.setColor(Color.WHITE);
        // paintContent.setStyle(Paint.Style.FILL);
        // canvas.drawRect(2, 2, mWidth - 2, mHeight - 2, paintContent);

        // 计算每个密码框宽度
        int rectWidth = (mWidth - PWD_SPACING * (PWD_LENGTH - 1)) / PWD_LENGTH;
        // 绘制密码框
        for (int i = 0; i < PWD_LENGTH; i++) {
            int left = (rectWidth + PWD_SPACING) * i;
            int top = 0;
            int right = left + rectWidth;
            int bottom = mHeight - top;
            mRect = new Rect(left, top, right, bottom);
            canvas.drawRect(mRect, mRectPaint);
        }

        // 绘制密码框
        for (int i = 1; i < PWD_LENGTH; i++) {
            int left = (rectWidth + PWD_SPACING) * i + 1;
            int right = left + 1;
            canvas.drawLine(left, 0, right, mHeight, mLinePaint);
        }

        // 绘制密码
        for (int i = 0; i < mInputLength; i++) {
            int cx = rectWidth / 2 + (rectWidth + PWD_SPACING) * i;
            int cy = mHeight / 2;
            canvas.drawCircle(cx, cy, PWD_SIZE, mPwdPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.mInputLength = text.toString().length();
        invalidate();
        if (mOnInputListener != null) {
            if (mInputLength == PWD_LENGTH && mOnInputListener != null) {
                mOnInputListener.onInputChange(getId(), mInputLength, text.toString(), true);
            } else {
                mOnInputListener.onInputChange(getId(), lengthAfter, "", false);
            }
        }
    }

    public interface OnInputListener {
        /**
         * 密码改变
         * 
         * @param viewID
         * @param length
         * @param password
         * @param isFinished
         */
        void onInputChange(int viewID, int length, String password, boolean isFinished);
    }

    /**
     * 设置输入完成监听
     * 
     */
    public void setOnInputListener(OnInputListener onInputListener) {
        this.mOnInputListener = onInputListener;
    }

}

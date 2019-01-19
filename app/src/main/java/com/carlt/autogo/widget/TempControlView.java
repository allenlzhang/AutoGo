package com.carlt.autogo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.carlt.autogo.R;


/**
 * 温度控制
 * Created by yangle on 2016/11/29.
 */
public class TempControlView extends View {

    // 控件宽
    private int                  width;
    // 控件高
    private int                  height;
    // 刻度盘半径
    private int                  dialRadius;
    // 圆弧半径
    private int                  arcRadius;
    // 刻度高
    private int                  scaleHeight = dp2px(20);
    // 刻度盘画笔
    private Paint                dialPaint;
    // 圆弧画笔
    private Paint                arcPaint;
    // 标题画笔
    private Paint                titlePaint;
    // 旋转按钮画笔
    private Paint                buttonPaint;
    // 温度显示画笔
    private Paint                tempPaint;
    // 文本提示
    private String               title       = "当前空调温度";
    // 温度
    private int                  temperature = 15;
    // 最低温度
    private int                  minTemp     = 15;
    // 最高温度
    private int                  maxTemp     = 30;
    // 四格代表温度1度
    private int                  angleRate   = 4;
    // 每格的角度
    private float                angleOne    = (float) 270 / (maxTemp - minTemp) / angleRate;
    // 按钮图片
    private Bitmap               buttonImage = BitmapFactory.decodeResource(getResources(),
            R.drawable.temp_control_btn);
    // 抗锯齿
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    // 温度改变监听
    private OnTempChangeListener onTempChangeListener;
    // 控件点击监听
    private OnClickListener      onClickListener;

    // 当前按钮旋转的角度
    private float rotateAngle;
    // 当前的角度
    private float currentAngle;

    public TempControlView(Context context) {
        this(context, null);
    }

    public TempControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TempControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dialPaint = new Paint();
        dialPaint.setAntiAlias(true);
        dialPaint.setStrokeWidth(dp2px(1));
        dialPaint.setStyle(Paint.Style.STROKE);

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);

        arcPaint.setStrokeWidth(dp2px(20));
        arcPaint.setStyle(Paint.Style.STROKE);

        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(sp2px(12));
        titlePaint.setColor(Color.parseColor("#04959F"));
        titlePaint.setStyle(Paint.Style.STROKE);

        buttonPaint = new Paint();
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        tempPaint = new Paint();
        tempPaint.setAntiAlias(true);
        tempPaint.setTextSize(sp2px(60));
        tempPaint.setColor(Color.parseColor("#FFFFFF"));
        tempPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 控件宽、高
        width = height = Math.min(h, w);
        // 刻度盘半径
        dialRadius = width / 2 - dp2px(10);
        // 圆弧半径
        arcRadius = dialRadius - dp2px(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArc(canvas);
        //        drawScale(canvas);
        drawText(canvas);
        //        drawTemp(canvas);
        drawButton(canvas);
    }

    /**
     * 绘制刻度盘
     * @param canvas
     *         画布
     */
    private void drawScale(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        // 逆时针旋转135-2度
        canvas.rotate(-115);
        dialPaint.setColor(Color.parseColor("#7f32394B"));
        for (int i = 0; i < maxTemp - minTemp - 1; i++) {
            canvas.drawLine(0, -dialRadius + dp2px(10), 0, -dialRadius + scaleHeight + dp2px(10), dialPaint);
            canvas.rotate((float) 245 / (maxTemp - minTemp - 1));
        }
        canvas.restore();
    }

    /**
     * 绘制刻度盘下的圆弧
     * @param canvas
     *         画布
     */
    private void drawArc(Canvas canvas) {
        canvas.save();
        canvas.translate(width / 2, height / 2);
        canvas.rotate(135 + 2);
        arcPaint.setColor(Color.parseColor("#394054"));
        RectF rectF = new RectF(-arcRadius, -arcRadius, arcRadius, arcRadius);
        canvas.drawArc(rectF, 0, 265, false, arcPaint);

//        arcPaint.setColor(Color.parseColor("#04B59E"));
        arcPaint.setColor(getResources().getColor(R.color.colorBlue));
        RectF rectF1 = new RectF(-arcRadius, -arcRadius, arcRadius, arcRadius);
        canvas.drawArc(rectF1, 0, (temperature - minTemp) * 265 / (maxTemp - minTemp), false, arcPaint);


        canvas.restore();
    }

    /**
     * 绘制标题与温度标识
     * @param canvas
     *         画布
     */
    private void drawText(Canvas canvas) {
        canvas.save();

        // 绘制标题
        float titleWidth = titlePaint.measureText(title);
        canvas.drawText(title, (width - titleWidth) / 2, (float) (dialRadius * (Math.cos(Math.PI / 180 * 60)) + dialRadius + dp2px(15)), titlePaint);

    }

    /**
     * 绘制旋转按钮
     * @param canvas
     *         画布
     */
    private void drawButton(Canvas canvas) {
        canvas.save();

        // 按钮宽高
        int buttonWidth = buttonImage.getWidth();
        int buttonHeight = buttonImage.getHeight();
        canvas.translate((width - buttonWidth) / 2, (height - buttonHeight) / 2);
        Matrix matrix = new Matrix();
        // 设置按钮位置，移动到控件中心
        matrix.setTranslate((float) Math.cos(Math.PI / 180 * ((temperature - minTemp) * 270 / (maxTemp - minTemp) + 135)) * arcRadius,
                (float) Math.sin(Math.PI / 180 * ((temperature - minTemp) * 270 / (maxTemp - minTemp) + 135)) * arcRadius);

        //设置抗锯齿
        canvas.setDrawFilter(paintFlagsDrawFilter);
        canvas.drawBitmap(buttonImage, matrix, buttonPaint);
        canvas.restore();
    }

    /**
     * 绘制温度
     * @param canvas
     *         画布
     */
    private void drawTemp(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);

        float tempWidth = tempPaint.measureText(temperature + "");
        float tempHeight = (tempPaint.ascent() + tempPaint.descent()) / 2;
        canvas.drawText(temperature + "°", -tempWidth / 2 - dp2px(5), -tempHeight, tempPaint);
        canvas.restore();
    }

    private boolean isDown;
    private boolean isMove;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                float downX = event.getX();
                float downY = event.getY();
                currentAngle = calcAngle(downX, downY);
                break;

            case MotionEvent.ACTION_MOVE:
                isMove = true;
                float targetX;
                float targetY;
                downX = targetX = event.getX();
                downY = targetY = event.getY();
                float angle = calcAngle(targetX, targetY);

                // 滑过的角度增量
                float angleIncreased = angle - currentAngle;

                // 防止越界
                if (angleIncreased < -270) {
                    angleIncreased = angleIncreased + 360;
                } else if (angleIncreased > 270) {
                    angleIncreased = angleIncreased - 360;
                }

                IncreaseAngle(angleIncreased);
                currentAngle = angle;
                invalidate();
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (isDown) {
                    if (isMove) {
                        // 纠正指针位置
                        rotateAngle = (float) ((temperature - minTemp) * angleRate * angleOne);
                        invalidate();
                        // 回调温度改变监听
                        if (onTempChangeListener != null) {
                            onTempChangeListener.changeUp(temperature);
                        }
                        isMove = false;
                    } else {
                        // 点击事件
                        if (onClickListener != null) {
                            onClickListener.onClick(temperature);
                        }
                    }
                    isDown = false;
                }
                break;
            }
        }
        return true;
    }

    /**
     * 以按钮圆心为坐标圆点，建立坐标系，求出(targetX, targetY)坐标与x轴的夹角
     * @param targetX
     *         x坐标
     * @param targetY
     *         y坐标
     * @return (targetX, targetY)坐标与x轴的夹角
     */
    private float calcAngle(float targetX, float targetY) {
        float x = targetX - width / 2;
        float y = targetY - height / 2;
        double radian;

        if (x != 0) {
            float tan = Math.abs(y / x);
            if (x > 0) {
                if (y >= 0) {
                    radian = Math.atan(tan);
                } else {
                    radian = 2 * Math.PI - Math.atan(tan);
                }
            } else {
                if (y >= 0) {
                    radian = Math.PI - Math.atan(tan);
                } else {
                    radian = Math.PI + Math.atan(tan);
                }
            }
        } else {
            if (y > 0) {
                radian = Math.PI / 2;
            } else {
                radian = -Math.PI / 2;
            }
        }
        return (float) ((radian * 180) / Math.PI);
    }

    /**
     * 增加旋转角度
     * @param angle
     *         增加的角度
     */
    private void IncreaseAngle(float angle) {
        rotateAngle += angle;
        if (rotateAngle < 0) {
            rotateAngle = 0;
        } else if (rotateAngle > 270) {
            rotateAngle = 270;
        }
        // 加上0.5是为了取整时四舍五入
        temperature = (int) ((rotateAngle / angleOne) / angleRate + 0.5) + minTemp;
        onTempChangeListener.changeMove(temperature);
    }

    /**
     * 设置几格代表1度，默认4格
     * @param angleRate
     *         几格代表1度
     */
    public void setAngleRate(int angleRate) {
        this.angleRate = angleRate;
    }

    /**
     * 设置温度
     * @param temp
     *         设置的温度
     */
    public void setTemp(int temp) {
        setTemp(minTemp, maxTemp, temp);
    }

    /**
     * 设置温度
     * @param minTemp
     *         最小温度
     * @param maxTemp
     *         最大温度
     * @param temp
     *         设置的温度
     */
    public void setTemp(int minTemp, int maxTemp, int temp) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        if (temp < minTemp) {
            this.temperature = minTemp;
        } else {
            this.temperature = temp;
        }

        // 计算旋转角度
        rotateAngle = (float) ((temp - minTemp) * angleRate * angleOne);
        // 计算每格的角度
        angleOne = (float) 270 / (maxTemp - minTemp) / angleRate;
        invalidate();
    }

    /**
     * 设置温度改变监听
     * @param onTempChangeListener
     *         监听接口
     */
    public void setOnTempChangeListener(OnTempChangeListener onTempChangeListener) {
        this.onTempChangeListener = onTempChangeListener;
    }

    /**
     * 设置点击监听
     * @param onClickListener
     *         点击回调接口
     */
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * 温度改变监听接口
     */
    public interface OnTempChangeListener {
        /**
         * 回调方法
         * @param temp
         *         温度
         */
        void changeUp(int temp);

        void changeMove(int temp);
    }

    /**
     * 点击回调接口
     */
    public interface OnClickListener {
        /**
         * 点击回调方法
         * @param temp
         *         温度
         */
        void onClick(int temp);
    }

    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }
}

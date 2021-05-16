package com.gpfei.recruit.utils.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class SmileToastView extends View {
    //矩形，设置toast布局时用
    RectF rectF =new RectF();
    //属性动画
    ValueAnimator valueAnimator;
    float mAnimatedValue = 0f;
    //自定义view的画笔
    private Paint mPaint;

    private float mWidth = 0f; //view的宽
    private float mEyeWidth = 0f; //笑脸的眼睛半径
    private float mPadding = 0f;  //view的偏移量。
    private float endAngle = 0f; //圆弧结束的度数

    //是左眼还是右眼
    private boolean isSmileLeft = false;
    private boolean isSmileRight = false;

    //设置画笔的参数及矩形的参数
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#6982ff"));
        mPaint.setStrokeWidth(dip2px(2));
    }
    private void initRect() {
        rectF = new RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding);
    }
    //dip转px。为了支持多分辨率手机
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //重写onMeasure
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initPaint();
        initRect();
        mWidth = getMeasuredWidth(); //当前view在父布局里的宽度。即view所占宽度。
        mPadding = dip2px(10);
        mEyeWidth = dip2px(3);
    }
    // 重新ondraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.STROKE);
        //画微笑弧（从左向右画弧）
        canvas.drawArc(rectF, 180, endAngle, false, mPaint);
        //设置画笔为实心
        mPaint.setStyle(Paint.Style.FILL);
        //左眼
        if (isSmileLeft) {
            canvas.drawCircle(mPadding + mEyeWidth + mEyeWidth / 2, mWidth / 3, mEyeWidth, mPaint);
        }
        //右眼
        if (isSmileRight) {
            canvas.drawCircle(mWidth - mPadding - mEyeWidth - mEyeWidth / 2, mWidth / 3, mEyeWidth, mPaint);
        }
    }

    /**
     * startAnim()不带参数的方法
     */
    public void startAnim() {
        stopAnim();
        startViewAnim(0f, 1f, 1500);
    }

    /**
     * 停止动画的方法
     *
     */
    public void stopAnim() {
        if (valueAnimator != null) {
            clearAnimation();
            isSmileLeft = false;
            isSmileRight = false;
            mAnimatedValue = 0f;
            valueAnimator.end();
        }
    }
    /**
     * 开始动画的方法
     * @param startF 起始值
     * @param endF   结束值
     * @param time  动画的时间
     * @return
     */
    private ValueAnimator startViewAnim(float startF, final float endF, long time) {
        //设置valueAnimator 的起始值和结束值。
        valueAnimator = ValueAnimator.ofFloat(startF, endF);
        //设置动画时间
        valueAnimator.setDuration(time);
        //设置补间器。控制动画的变化速率
        valueAnimator.setInterpolator(new LinearInterpolator());
        //设置监听器。监听动画值的变化，做出相应方式。
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                mAnimatedValue = (float) valueAnimator.getAnimatedValue();
                //如果value的值小于0.5
                if (mAnimatedValue < 0.5) {
                    isSmileLeft = false;
                    isSmileRight = false;
                    endAngle = -360 * (mAnimatedValue);
                    //如果value的值在0.55和0.7之间
                } else if (mAnimatedValue > 0.55 && mAnimatedValue < 0.7) {
                    endAngle = -180;
                    isSmileLeft = true;
                    isSmileRight = false;
                    //其他
                } else {
                    endAngle = -180;
                    isSmileLeft = true;
                    isSmileRight = true;
                }
                //重绘
                postInvalidate();
            }
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
        return valueAnimator;
    }



    public SmileToastView(Context context) {
        super(context);
    }

    public SmileToastView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SmileToastView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

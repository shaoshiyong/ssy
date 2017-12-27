package com.simuwang.xxx.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


/**
 * function:广告倒计时控件
 *
 * <p>
 * Created by Leo on 2017/4/7.
 */
@SuppressWarnings("unused")
public class CountDownCircleView extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener {
    private Paint mPaint;
    private Rect mBounds;
    private RectF mArcRectF;

    private int mCircleRadius;
    private int mProgress;
    private int gapTime = 30;
    private int swipCount = 0;
    private int mTime = 5000;

    private int mColorBg = Color.parseColor("#B2000000");
    private int mColorProgressBg = Color.parseColor("#B2000000");
    private int mSizeProgressCircle = 10;
    private int mTextColor = Color.WHITE;
    private int mColorProgressGoing = Color.parseColor("#EF534E");
    private String mText = "跳过";
    private ICountDownCallback mICountDownCallback;
    private int progressUnit;

    public CountDownCircleView(Context context) {
        this(context, null);
    }

    public CountDownCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountDownCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mPaint = new Paint();
        mBounds = new Rect();
        mArcRectF = new RectF();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (width > height) height = width;
        else width = height;
        mCircleRadius = width / 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getDrawingRect(mBounds);
        int mCenterX = mBounds.centerX();
        int mCenterY = mBounds.centerY();

        mPaint.setAntiAlias(true);

        //画背景大圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColorBg);
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), mCircleRadius, mPaint);

        //画进度条背景圈
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mSizeProgressCircle);
        mPaint.setColor(mColorProgressBg);
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), mCircleRadius - mSizeProgressCircle, mPaint);

        //画文字
        Paint paint = getPaint();
        paint.setColor(mTextColor);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        float textY = mCenterY - (paint.descent() + paint.ascent()) / 2;
        canvas.drawText(mText, mCenterX, textY, paint);

        //画进度条
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mSizeProgressCircle);
        mPaint.setColor(mColorProgressGoing);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcRectF.set(mBounds.left + mSizeProgressCircle, mBounds.top + mSizeProgressCircle, mBounds.right - mSizeProgressCircle, mBounds.bottom - mSizeProgressCircle);
        canvas.drawArc(mArcRectF, -90, mProgress, false, mPaint);
    }

    public void star() {
        progressUnit = 360 * gapTime / mTime;
        post(mRunnable);
        setOnClickListener(this);
    }

    public void stop() {
        isStop = true;
        removeCallbacks(mRunnable);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isStop) {
                removeCallbacks(mRunnable);
                return;
            }
            invalidate();
            ++swipCount;
            mProgress = progressUnit * swipCount;
            if (mProgress == 360) {
                stop();
                if (mICountDownCallback != null) mICountDownCallback.onFinish();
            } else {
                postDelayed(mRunnable, gapTime);
            }
        }
    };

    private volatile boolean isStop = false;

    @Override
    public void onClick(View v) {
        stop();
        if (mICountDownCallback != null) mICountDownCallback.onFinish();
    }

    public CountDownCircleView setTime(int time) {
        mTime = time;
        return this;
    }

    public CountDownCircleView setGapTime(int gapTime) {
        this.gapTime = gapTime;
        return this;
    }

    public CountDownCircleView setColorBg(int mColorBg) {
        this.mColorBg = mColorBg;
        return this;
    }

    public CountDownCircleView setColorProgressBg(int mColorProgressBg) {
        this.mColorProgressBg = mColorProgressBg;
        return this;
    }

    public CountDownCircleView setSizeProgressCircle(int mSizeProgressCircle) {
        this.mSizeProgressCircle = mSizeProgressCircle;
        return this;
    }

    public CountDownCircleView setTextColorExtra(int mTextColor) {
        this.mTextColor = mTextColor;
        return this;
    }

    public CountDownCircleView setColorProgressGoing(int mColorProgressGoing) {
        this.mColorProgressGoing = mColorProgressGoing;
        return this;
    }

    public CountDownCircleView setText(String mText) {
        this.mText = mText;
        return this;
    }

    public CountDownCircleView setCallback(ICountDownCallback callback) {
        this.mICountDownCallback = callback;
        return this;
    }

    public interface ICountDownCallback {
        /**
         * 倒计时结束或者点击了控件回调
         */
        void onFinish();
    }
}

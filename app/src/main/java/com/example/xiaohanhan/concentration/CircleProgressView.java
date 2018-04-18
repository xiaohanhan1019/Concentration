package com.example.xiaohanhan.concentration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.xiaohanhan.concentration.R;

import java.util.Locale;

/**
 * Created by xiaohanhan on 2018/4/18.
 */

public class CircleProgressView extends View {

    // 画实心圆的画笔
    private Paint mCirclePaint;
    // 画圆环的画笔
    private Paint mRingPaint;
    // 画圆环的画笔背景色
    private Paint mRingBgPaint;
    // 画字体的画笔
    private Paint mTextPaint;
    // 圆形颜色
    private int mCircleColor;
    // 圆环颜色
    private int mRingColor;
    // 圆环背景颜色
    private int mRingBgColor;
    // 半径
    private float mRadius;
    // 圆环半径
    private float mRingRadius;
    // 圆环宽度
    private float mRingStrokeWidth;
    // 圆心x坐标
    private int mXCenter;
    // 圆心y坐标
    private int mYCenter;
    // 字的长度
    private float mTxtWidth;
    // 字的高度
    private float mTxtHeight;
    // 字的颜色
    private int mTxtColor;
    // 总进度
    private int mTotalProgress = 100;
    // 当前进度
    private int mCurrentProgress;
    // 总时间 以秒为单位
    private int mTotalTime;
    // 当前时间 以秒为单位
    private int mCurrentTime = 0;
    // 开始键位置
    private Rect mSrcRect, mDestRect;
    // 开始键画笔
    private Paint mBitPaint;
    // 开始键
    private Bitmap mStartBitmap;
    // 外圆环背景
    private RectF mRingBgRect;
    // 进度条
    private RectF mRingRect;

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义的属性
        initAttrs(context, attrs);
        initVariable();
    }

    //属性
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getResources().obtainAttributes(attrs,
                R.styleable.CircleProgressViewAttrs);
        mRadius = typeArray.getDimension(R.styleable.CircleProgressViewAttrs_radius,100);
        mRingStrokeWidth = typeArray.getDimension(R.styleable.CircleProgressViewAttrs_ringStrokeWidth, 10);
        mCircleColor = typeArray.getColor(R.styleable.CircleProgressViewAttrs_circleColor, Color.WHITE);
        mRingColor = typeArray.getColor(R.styleable.CircleProgressViewAttrs_ringColor,Color.WHITE);
        mRingBgColor = typeArray.getColor(R.styleable.CircleProgressViewAttrs_ringBgColor, Color.WHITE);
        mTxtColor = typeArray.getColor(R.styleable.CircleProgressViewAttrs_textColor,Color.WHITE);
        typeArray.recycle();

        mRingRadius = mRadius + mRingStrokeWidth / 2;

        mStartBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic_start);
        mStartBitmap = zoom(mStartBitmap,(int)mRadius/3,(int)mRadius/3);
        mSrcRect = new Rect();
        mDestRect = new Rect();

        mRingBgRect = new RectF();
        mRingRect = new RectF();
    }

    //初始化画笔
    private void initVariable() {
        //内圆
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);

        //外圆环背景
        mRingBgPaint = new Paint();
        mRingBgPaint.setAntiAlias(true);
        mRingBgPaint.setDither(true);
        mRingBgPaint.setColor(mRingBgColor);
        mRingBgPaint.setStyle(Paint.Style.STROKE);
        mRingBgPaint.setStrokeWidth(mRingStrokeWidth);


        //外圆环
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setDither(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mRingStrokeWidth);
        mRingPaint.setStrokeCap(Paint.Cap.ROUND);//设置线冒样式，有圆 有方

        //中间字
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTxtColor);
        mTextPaint.setTextSize(mRadius / 2);

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);

        // 开始键
        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setAntiAlias(true);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
    }

    //画图
    @Override
    protected void onDraw(Canvas canvas) {
        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;

        //内圆
        canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);

        //外圆环背景
        mRingBgRect.left = (mXCenter - mRingRadius);
        mRingBgRect.top = (mYCenter - mRingRadius);
        mRingBgRect.right = mRingRadius * 2 + (mXCenter - mRingRadius);
        mRingBgRect.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
        canvas.drawArc(mRingBgRect, 0, 360, false, mRingBgPaint); //圆环所在的椭圆对象、圆环的起始角度、圆环的角度、是否显示半径连线

        //进度条显示 无进度条显示开始键
        if (mCurrentTime > 0 && mCurrentProgress!=100) {
            mRingRect.left = (mXCenter - mRingRadius);
            mRingRect.top = (mYCenter - mRingRadius);
            mRingRect.right = mRingRadius * 2 + (mXCenter - mRingRadius);
            mRingRect.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
            canvas.drawArc(mRingRect, -90, ((float) mCurrentProgress / mTotalProgress) * 360, false, mRingPaint); //

            //字体
            String txt = getTxt(mCurrentTime);
            mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
            canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);
        } else {
            mSrcRect.left = 0;
            mSrcRect.top = 0;
            mSrcRect.right = mStartBitmap.getWidth();
            mSrcRect.bottom = mStartBitmap.getHeight();
            mDestRect.left = (int)(mXCenter - 0.28*mRingRadius);
            mDestRect.top = (int)(mYCenter - 0.33*mRingRadius);
            mDestRect.right = (int)(mXCenter + 0.38*mRingRadius);
            mDestRect.bottom = (int)(mYCenter + 0.33*mRingRadius);
//            canvas.drawRect(mSrcRect,mRingBgPaint);
//            canvas.drawRect(mDestRect,mRingBgPaint);
            canvas.drawBitmap(mStartBitmap,mSrcRect,mDestRect,mBitPaint);
        }

    }

    //定时
    public void setTotalTime(int totalTime){
        mTotalTime = totalTime;
    }

    //设置进度
    public void setProgress(int currentTime) {
        mCurrentTime=currentTime;
        mCurrentProgress = currentTime*mTotalProgress/mTotalTime;
        //重绘
        postInvalidate();
    }

    //设置文字
    private String getTxt(int currentTime){
        int remainTime = mTotalTime - currentTime;
        int h=remainTime/3600;
        remainTime = remainTime - h*3600;
        int m=remainTime / 60;
        int s=remainTime % 60;
        if(h==0){
            return String.format(Locale.getDefault(),"%02d:%02d",m,s);
        } else {
            return String.format(Locale.getDefault(),"%02d:%02d:%02d",h,m,s);
        }
    }

    //缩放图片
    public static Bitmap zoom(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}

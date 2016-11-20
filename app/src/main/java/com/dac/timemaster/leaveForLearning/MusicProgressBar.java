package com.dac.timemaster.leaveForLearning;

/**
 * Created by GeniusHe on 2016/10/28.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dac.timemaster.R;


public class MusicProgressBar extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 点的半径
     */
    private float pointRadius;

    /**
     * 空心点的宽度
     */
    private float pointWidth;

    private Drawable mThumb, mThumbPress;

    public MusicProgressBar(Context context) {
        this(context, null);
    }

    public MusicProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 3);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.BLACK);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_imageMax, 100);

        pointRadius = mTypedArray.getDimension(R.styleable.RoundProgressBar_pointRadius, 3);
        pointWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_pointWidth, 2);

        mTypedArray.recycle();

        // 加载拖动图标
        mThumb = getResources().getDrawable(R.drawable.btn_lamp_plate_nor);// 圆点图片
        int thumbHalfheight = mThumb.getIntrinsicHeight() / 2;
        int thumbHalfWidth = mThumb.getIntrinsicWidth() / 2;
        mThumb.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);

        mThumbPress = getResources().getDrawable(R.drawable.btn_lamp_plate_down);// 圆点图片
        thumbHalfheight = mThumbPress.getIntrinsicHeight() / 2;
        thumbHalfWidth = mThumbPress.getIntrinsicWidth() / 2;
        mThumbPress.setBounds(-thumbHalfWidth, -thumbHalfheight, thumbHalfWidth, thumbHalfheight);
        paddingOuterThumb = thumbHalfheight;
    }

    @Override
    public void onDraw(Canvas canvas) {
        /**
         * 画最外层的大圆环
         */
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centerX, centerY, radius, paint); //画出圆环

        /**
         * 画文字
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        // paint.setTypeface(Typeface.DEFAULT); //设置字体
        String textTime = getTimeText(progress);
        float textWidth = paint.measureText(textTime);
        //上面测量字体宽度，我们需要根据字体的宽度设置在圆环中间

        canvas.drawText(textTime, centerX - textWidth / 2, centerY + textSize/2, paint);

        /**
         * 画圆弧 ，画圆环的进度
         */
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);  //用于定义的圆弧的形状和大小的界限

        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, 270, 360 * progress / max, false, paint);  //根据进度画圆弧

        // 画圆上的两个点
        paint.setStrokeWidth(pointWidth);//点的宽度
        PointF startPoint = ChartUtil.calcArcEndPointXY(centerX, centerY, radius, 0, 270); //起始点
        canvas.drawCircle(startPoint.x, startPoint.y, pointRadius, paint);//画点

        //paint.setStyle(Paint.Style.FILL_AND_STROKE);
        PointF progressPoint = ChartUtil.calcArcEndPointXY(centerX, centerY, radius, 360 * progress / max, 270); //可移动的点
        //canvas.drawCircle(progressPoint.x, progressPoint.y, pointRadius, paint);
        // 画Thumb
        canvas.save();
        canvas.translate(progressPoint.x, progressPoint.y);
        if (downOnArc) {
            mThumbPress.draw(canvas);
        } else {
            mThumb.draw(canvas);
        }
        canvas.restore();
    }

    //是否点在区域中
    private boolean downOnArc = false;

    /**
     * onTouchEvent(MotionEvent event)
     * 应用程序可以通过该方法处理手机屏幕的触摸事件
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {

            //MotionEvent.ACTION_DOWN：屏幕被按下
            case MotionEvent.ACTION_DOWN:
                if (isTouchArc(x, y)) {
                    downOnArc = true;
                    updateArc(x, y);
                    return true;
                }
                break;
            //MotionEvent.ACTION_MOVE：在屏幕中拖动
            case MotionEvent.ACTION_MOVE:
                if (downOnArc) {
                    updateArc(x, y);
                    return true;
                }
                break;

            //MotionEvent.ACTION_UP：屏幕被抬起
            case MotionEvent.ACTION_UP:
                downOnArc = false;
                invalidate();
                if (changeListener != null) {
                    changeListener.onProgressChangeEnd(max, progress);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private int centerX, centerY;
    private int radius;
    private int paddingOuterThumb;

    /**
     * onSizeChanged(int width, int height, int oldw, int oldh)
     * 初始化每个数值
     * 如centerX，centerY，中心点坐标
     * radius 半径
     * minValidateTouchArcRadius最小点击区域
     * maxValidateTouchArcRadius最大点击区域
     */
    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        centerX = width / 2;
        centerY = height / 2;
        int minCenter = Math.min(centerX, centerY);

        radius = (int) (minCenter - roundWidth/2 - paddingOuterThumb); //圆环的半径
        minValidateTouchArcRadius = (int) (radius - paddingOuterThumb*1.5f);
        maxValidateTouchArcRadius = (int) (radius + paddingOuterThumb*1.5f);
        super.onSizeChanged(width, height, oldw, oldh);
    }

    /**
     *  根据点的位置，更新progress
     * @param x
     * @param y
     */
    private void updateArc(int x, int y) {
        int cx = x - getWidth() / 2;
        int cy = y - getHeight() / 2;
        // 计算角度，得出（-1->1）之间的数据，等同于（-180°->180°）
        double angle = Math.atan2(cy, cx)/Math.PI;
        // 将角度转换成（0->2）之间的值，然后加上90°的偏移量
        angle = ((2 + angle)%2 + (90/180f))%2;
        // 用（0->2）之间的角度值乘以总进度，等于当前进度
        progress = (int) (angle * max/2);
        if (changeListener != null) {
            changeListener.onProgressChange(max, progress);
        }
        invalidate();
    }

    private int minValidateTouchArcRadius; // 最小有效点击半径
    private int maxValidateTouchArcRadius; // 最大有效点击半径


    /**
     * 判断是否点在圆边上
     * @param x 传入坐标的x
     * @param y 传入坐标的y
     * @return
     */
    private boolean isTouchArc(int x, int y) {
        double d = getTouchRadius(x, y);
        return d >= minValidateTouchArcRadius && d <= maxValidateTouchArcRadius;
    }

    /**
     *  计算某点到圆点的距离
     * @param x 传入坐标的x
     * @param y 传入坐标的y
     * @return
     */
    private double getTouchRadius(int x, int y) {
        int cx = x - getWidth() / 2;
        int cy = y - getHeight() / 2;
        return Math.hypot(cx, cy);
    }


    /**
     * 文字中时间的计算方式
     * @param progress
     * @return result
     * result为时间
     */
    private String getTimeText(int progress) {
        int hour = progress / 60;
        int minute = progress % 60;
        String result = (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute;
        return result;
    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     * @param max
     * max单位为秒（s）
     */
    public synchronized void setMax(int max) {
        if(max < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress > max){
            progress = max;

        }
        if(progress <= max){
            this.progress = progress;
            postInvalidate();
        }

    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public static class ChartUtil {

        /**
         * 依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标
         * @param cirX
         * @param cirY
         * @param radius
         * @param cirAngle
         * @return
         */
        public static PointF calcArcEndPointXY(float cirX, float cirY, float radius, float cirAngle){
            float posX = 0.0f;
            float posY = 0.0f;
            //将角度转换为弧度
            float arcAngle = (float) (Math.PI * cirAngle / 180.0);
            if (cirAngle < 90)
            {
                posX = cirX + (float)(Math.cos(arcAngle)) * radius;
                posY = cirY + (float)(Math.sin(arcAngle)) * radius;
            }
            else if (cirAngle == 90)
            {
                posX = cirX;
                posY = cirY + radius;
            }
            else if (cirAngle > 90 && cirAngle < 180)
            {
                arcAngle = (float) (Math.PI * (180 - cirAngle) / 180.0);
                posX = cirX - (float)(Math.cos(arcAngle)) * radius;
                posY = cirY + (float)(Math.sin(arcAngle)) * radius;
            }
            else if (cirAngle == 180)
            {
                posX = cirX - radius;
                posY = cirY;
            }
            else if (cirAngle > 180 && cirAngle < 270)
            {
                arcAngle = (float) (Math.PI * (cirAngle - 180) / 180.0);
                posX = cirX - (float)(Math.cos(arcAngle)) * radius;
                posY = cirY - (float)(Math.sin(arcAngle)) * radius;
            }
            else if (cirAngle == 270)
            {
                posX = cirX;
                posY = cirY - radius;
            }
            else
            {
                arcAngle = (float) (Math.PI * (360 - cirAngle) / 180.0);
                posX = cirX + (float)(Math.cos(arcAngle)) * radius;
                posY = cirY - (float)(Math.sin(arcAngle)) * radius;
            }
            return new PointF(posX, posY);
        }

        public static PointF calcArcEndPointXY(float cirX, float cirY, float radius, float cirAngle, float orginAngle){
            cirAngle = (orginAngle + cirAngle) % 360;
            return calcArcEndPointXY(cirX, cirY, radius, cirAngle);
        }
    }

    private OnProgressChangeListener changeListener;

    public void setChangeListener(OnProgressChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public interface OnProgressChangeListener {
        void onProgressChange(int duration, int progress);

        void onProgressChangeEnd(int duration, int progress);
    }
}


package com.cxd.radarview2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.cxd.radarview2.options.CobwebOptions;
import com.cxd.radarview2.options.TextOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * create by cxd on 2020/5/9
 * 雷达图View
 *
 * 相较于旧版{@link com.cxd.radarview.RadarView}:
 * 优化了绘制方式，大大提高了效率
 * 优化了UI适配
 * 优化了使用方式
 * 修复了渐变背景失真问题
 * 添加了移除数据线api -> {@link RadarView#removeLines()}
 */
@SuppressLint("DrawAllocation")
public class RadarView extends View {
    private Context mContext ;
    private Builder mBuilder ;
    private Point[][] mPoints;
    private Point[] mTextPoints ;
    private int mPetal ;
    private int mLevel ;
    private int mPadding ;
    private int mWidth ;
    private int mHeight ; //这里的高等于宽，不是View的高
    private Paint mTextPaint ;
    private Paint mCobwebPaint ;
    private int mPetalLenght ;
    private List<DataLineBean> mDataLines ;


    public RadarView(Context context, Builder builder) {
        super(context);
        mContext = context ;
        mBuilder = builder;
        mPetal = mBuilder.cobwebOptions.petal ;
        mLevel = mBuilder.cobwebOptions.level ;
        mTextPoints = new Point[mPetal];
        mPoints = new Point[mLevel][mPetal];
        mPadding = mBuilder.padding;

        /*文字画笔*/
        mTextPaint = new Paint();
        mTextPaint.setTextSize(sp2px(mBuilder.textOptions.textSize));
        mTextPaint.setColor(mBuilder.textOptions.textColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTypeface((mBuilder.textOptions.isBold == true) ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);

        /*蛛网画笔*/
        mCobwebPaint = new Paint();
        mCobwebPaint.setStyle(Paint.Style.FILL);
        mCobwebPaint.setColor(mBuilder.cobwebOptions.silkColor);
        mCobwebPaint.setStrokeWidth(mBuilder.cobwebOptions.silkWidth);
        mCobwebPaint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = mWidth = getMeasuredWidth();

        final float angle = (float)360 / mPetal ;

        /*填装文字基准点*/
        Point textStandardPoint = new Point(mWidth / 2 , mPadding);
        for (int p = 0; p < mPetal; p++) {
            mTextPoints[p] = calcNewPoint(textStandardPoint,p * angle);
        }

        /*填装points*/
        Point[] standardPoints = new Point[mLevel];
        int textSpace = mBuilder.space ;
        int levelHeight = (mHeight / 2 - (mPadding + textSpace)) / mLevel ;
        for (int i = 0; i < mLevel; i++) {
            Point temp = new Point();
            temp.x = mWidth / 2 ;
            temp.y = mPadding + textSpace + i * levelHeight ;
            standardPoints[i] = temp ;
        }
        for (int l = 0; l < mLevel; l++) {
            for (int p = 0; p < mPetal; p++) {
                mPoints[l][p] = calcNewPoint(standardPoints[l],p * angle);
            }
        }

        /*蛛网瓣长*/
        mPetalLenght = mHeight / 2 - (mPadding + textSpace ) ;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        /*绘制渐变色*/
        if(mBuilder.cobwebOptions.fillColors != null){
            if(mBuilder.cobwebOptions.fillStyle == CobwebOptions.FillStyle.LADDER){ //阶梯
                for (int i = 0; i < mLevel; i++) {
                    canvas.save();
                    Path path = new Path();
                    for (int p = 0; p < mPetal; p++) {
                        if(p == 0){
                            path.moveTo(mPoints[i][p].x,mPoints[i][p].y);
                        }else{
                            path.lineTo(mPoints[i][p].x,mPoints[i][p].y);
                        }
                    }
                    path.close();
                    canvas.clipPath(path);

                    if(mBuilder.cobwebOptions.fillColors.length < mLevel){
                        int d = mLevel - mBuilder.cobwebOptions.fillColors.length ;
                        if(d > i){
                            canvas.restore();
                            continue;
                        }
                    }
                    canvas.drawColor(mBuilder.cobwebOptions.fillColors[mLevel - i - 1]);
                    canvas.restore();
                }
            }else if(mBuilder.cobwebOptions.fillStyle == CobwebOptions.FillStyle.GRADUAL){ //渐变
                canvas.save();
                Path path = new Path();
                for (int p = 0; p < mPetal; p++) {
                    if(p == 0){
                        path.moveTo(mPoints[0][p].x,mPoints[0][p].y);
                    }else{
                        path.lineTo(mPoints[0][p].x,mPoints[0][p].y);
                    }
                }
                path.close();
                canvas.clipPath(path);
                Shader shader = new RadialGradient(mWidth / 2 , mHeight / 2 ,mWidth / 2
                        ,mBuilder.cobwebOptions.fillColors,
                        null,Shader.TileMode.REPEAT);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                paint.setShader(shader);
                canvas.drawPath(path,paint);
                canvas.restore();
            }
        }


        /*绘制文字*/
        for (int i = 0; i < mPetal; i++) {
            if(mBuilder.textOptions.texts == null || mBuilder.textOptions.texts.length <= i){
                break;
            }
            Path ph = new Path();
            ph.moveTo(mTextPoints[i].x,mTextPoints[i].y);
            ph.rMoveTo(-(sp2px(mBuilder.textOptions.textSize) * mBuilder.textOptions.texts[i].length() )/ 2,0);
            ph.rLineTo(sp2px(mBuilder.textOptions.textSize) * mBuilder.textOptions.texts[i].length(),0);
            canvas.drawTextOnPath(mBuilder.textOptions.texts[i],ph,0,mBuilder.textOptions.textSize / 2 , mTextPaint);
        }


        /*绘制蛛网*/
        for (int p = 0; p < mPetal; p++) {  //轴线
            canvas.drawLine(mPoints[0][p].x,mPoints[0][p].y,mWidth / 2 , mHeight / 2,mCobwebPaint);
        }
        for (int l = 0; l < mLevel; l++) {  //环线
            for (int p = 0; p < mPetal; p++) {
                if(p == 0){
                    continue;
                }else {
                    canvas.drawLine(mPoints[l][p-1].x,mPoints[l][p-1].y,mPoints[l][p].x,mPoints[l][p].y,mCobwebPaint);
                }
                if(p == mPetal - 1){
                    canvas.drawLine(mPoints[l][p].x,mPoints[l][p].y,mPoints[l][0].x,mPoints[l][0].y,mCobwebPaint);
                }
            }
        }

        /*绘制折线集*/
        if(mDataLines != null){
            for(DataLineBean b : mDataLines){
                if(b.getVals() == null || b.getVals().length < mPetal){
                    throw new RuntimeException("数据数组长度不够！");
                }
                Point[] line = new Point[mPetal];
                /*封装一条线*/
                final float angle = (float)360 / mPetal ;
                for (int i = 0; i < mPetal; i++) {
                    int linel = (int) (mPetalLenght * (b.getVals()[i] / mBuilder.maxVal));
                    Point startP = new Point();
                    startP.x = mWidth / 2 ;
                    startP.y = mHeight / 2 - linel ;
                    line[i] = calcNewPoint(startP,i * angle) ;
                }
                /*绘制*/
                Path lp = new Path();
                for (int i = 0; i < mPetal; i++) {
                    if(i == 0){
                        lp.moveTo(line[i].x,line[i].y);
                        continue;
                    }else{
                        lp.lineTo(line[i].x,line[i].y);
                    }

                    if(i == mPetal - 1){
                        lp.close();
                    }
                }
                canvas.drawPath(lp,b.getPaint());
            }
        }

    }

    /**
     * 某个点旋转一定角度后，得到一个新的点
     * @param p 初始点
     * @param angle 旋转角度
     * @return
     */
    private Point calcNewPoint(Point p , float angle) {
        Point pCenter = new Point(mWidth/2,mHeight/2) ;
        // calc arc
        float l = (float) ((angle * Math.PI) / 180);

        //sin/cos value
        float cosv = (float) Math.cos(l);
        float sinv = (float) Math.sin(l);

        // calc new point
        float newX = (p.x - pCenter.x) * cosv - (p.y - pCenter.y) * sinv + pCenter.x;
        float newY = (p.x - pCenter.x) * sinv + (p.y - pCenter.y) * cosv + pCenter.y;
        return new Point((int) newX, (int) newY);
    }

    /**
     * 添加一条数据线
     * @param vals
     * @param paint
     */
    public void addLine(float[] vals , Paint paint){
        if(mDataLines == null){
            mDataLines = new ArrayList<>();
        }

        mDataLines.add(new DataLineBean(vals,paint));
        invalidate();
    }

    /**
     * 移除所有的数据线
     */
    public void removeLines(){
        mDataLines = null ;
        invalidate();
    }

    public static class Builder{
        private Context context ;
        private CobwebOptions cobwebOptions ;
        private TextOptions textOptions ;
        private long maxVal ; //最大数值
        private int space ; //文字与蛛网的距离
        private int padding ;

        public Builder context(Context context){
            this.context = context ;
            return this;
        }
        public Builder cobwebOptions(CobwebOptions cobwebOptions){
            this.cobwebOptions = cobwebOptions ;
            return this;
        }
        public Builder textOptions(TextOptions textOptions){
            this.textOptions = textOptions ;
            return this;
        }
        public Builder maxVal(long maxVal){
            this.maxVal = maxVal ;
            return this;
        }
        public Builder space(int space){
            this.space = space ;
            return this ;
        }
        public Builder padding(int padding){
            this.padding = padding ;
            return this;
        }
        public RadarView build(){
            return new RadarView(this.context,this);
        }

    }

    private class DataLineBean implements Serializable{
        private float[] vals ;
        private Paint paint ;

        public DataLineBean(float[] vals, Paint paint) {
            this.vals = vals;
            this.paint = paint;
        }

        public Paint getPaint() {
            return paint;
        }

        public float[] getVals() {
            return vals;
        }
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @return
     */
    public int sp2px(float spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}

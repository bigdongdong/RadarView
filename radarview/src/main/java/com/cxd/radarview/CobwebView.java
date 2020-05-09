package com.cxd.radarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.view.View;

import com.cxd.radarview.options.CobwebOptions;
import com.cxd.radarview.options.DataLineOptions;

import java.util.List;

@Deprecated
public class CobwebView extends View {
    public CobwebOptions cobwebOptions ;
    public Float cobwebCanvasScale ;
    public List<RadarView.DataLineBean> lineList ;
    public float maxValue ;

    public CobwebView(Context context, RadarView.Builder builder ) {
        super(context);
        this.cobwebOptions = builder.cobwebOptions;
        this.cobwebCanvasScale = builder.cobwebCanvasScale;

        this.lineList = builder.lineList;
        this.maxValue = builder.maxValue;

        this.setBackground(new ShapeDrawable(new RadarShape()));
    }

    public void addLine(DataLineOptions lineOptions , float[] values){
        if(lineOptions!=null && values !=null){
            lineList.add(new RadarView.DataLineBean(lineOptions,values));
            //刷新View
            requestLayout();
        }
    }

    private class RadarShape extends Shape {

        private int halfW ;
        private float A ; //每一瓣的角度
        private float r  ; //每一瓣的弧度值的一半

        int[] levelColors = null;

        @Override
        protected void onResize(float width, float height) {
            super.onResize(width, height);

            halfW = (int) width / 2  ;
            A = 360f / cobwebOptions.petal;
            r = (float) (Math.PI / cobwebOptions.petal);
        }

        float bili ;
        private int getW(float val){
            bili = (val * 1.0f) / (maxValue * 1.0f );
            int h = (int) (halfW * bili);
            return (int) (Math.tan(r) * h);
        }

        private int getH(float val){
            bili = (val * 1.0f) / (maxValue * 1.0f );
            int h = (int) (halfW * bili);
            return halfW - h;
        }

        /*绘制数据折线的path*/
        private Path designValPath(DataLineOptions lineOptions ,float[] values , int p){
            Path path = new Path();
            float lastVal , nextVal ;

            lastVal = values[p];
            if(p == values.length - 1){
                nextVal = values[0];
            }else{
                nextVal = values[p+1];
            }
            if(lineOptions.isFill == true){
                path.moveTo(halfW , halfW);
                path.lineTo(halfW - getW(lastVal) , getH(lastVal));
            }else{
                path.moveTo(halfW - getW(lastVal) , getH(lastVal));
            }

            path.lineTo(halfW + getW(nextVal) , getH(nextVal));

            if(lineOptions.isFill == true){
                path.close();
            }

            return path ;
        }

        /*画雷达蜘蛛网基线*/
        private Path designCobwebPath(int l){
            Path path = new Path();
            int w , h;

            h = (int) (halfW * ((cobwebOptions.level - l + 1) * 1.0f/cobwebOptions.level * 1.0f));
            w = (int) (Math.tan(r) * h);
            path.moveTo(halfW,halfW);
            path.lineTo(halfW - w,halfW - h);
            path.lineTo(halfW + w,halfW - h);
            path.close();

            return path ;
        }

        /*获取填充画笔*/
        private Paint getFillPaint(int color){
            Paint paint = new Paint();
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            return paint ;
        }

        /*获取描边画笔*/
        private Paint getStrokePaint(int color , int width){
            Paint paint = new Paint();
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(width); //如果为0，则默认画笔宽度为1px
            paint.setStrokeCap(Paint.Cap.SQUARE);
            return paint ;
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {
            /*将画布缩小*/
            if(cobwebCanvasScale != null){
                canvas.scale(cobwebCanvasScale,cobwebCanvasScale,halfW,halfW);
            }else{
                canvas.scale(0.7f,0.7f,halfW,halfW);
            }

            /*起始旋转，使尖角朝正上方*/
            canvas.rotate(A/2, halfW , halfW);

            //设置level的颜色
            if(cobwebOptions.cobwebLevelColors != null){
                levelColors = cobwebOptions.cobwebLevelColors;
            }else if(cobwebOptions.centerColorStr != null && cobwebOptions.peripheralColorStr != null){
                levelColors = ColorUtils.generateColors(cobwebOptions.peripheralColorStr,cobwebOptions.centerColorStr,cobwebOptions.level);
            }

            for(int l = 1 ; l <= cobwebOptions.level ; l++){
                for(int p = 0 ; p < cobwebOptions.petal ; p++ ){
                    /*绘制蛛网填充*/
                    if(levelColors != null){
                        canvas.drawPath(designCobwebPath(l) , getFillPaint(levelColors[l-1]));
                    }
                    /*绘制蛛网边框*/
                    if(cobwebOptions.silkWidth > 0){
                        canvas.drawPath(designCobwebPath(l) , getStrokePaint(cobwebOptions.silkColor,cobwebOptions.silkWidth));
                    }
                    /*旋转一个角度*/
                    canvas.rotate(A , halfW , halfW);
                }
            }

            for(RadarView.DataLineBean bean : lineList){
                for(int p = 0 ; p < cobwebOptions.petal ; p++ ){
                    /*绘制数据折线*/
                    if(bean.values != null && bean.options.lineWidth > 0 && bean.options.lineColor != 0){
                        if(bean.options.isFill == true){
                            canvas.drawPath(designValPath(bean.options,bean.values,p) , getFillPaint(bean.options.lineColor));
                        }else{
                            canvas.drawPath(designValPath(bean.options,bean.values,p) , getStrokePaint(bean.options.lineColor,bean.options.lineWidth));
                        }
                    }
                    /*旋转一个角度*/
                    canvas.rotate(A , halfW , halfW);
                }
            }
        }
    }
}

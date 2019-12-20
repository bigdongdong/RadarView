package com.cxd.moudle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.widget.RelativeLayout;

import com.cxd.moudle.options.CobwebOptions;
import com.cxd.moudle.options.DataLineOptions;
import com.cxd.moudle.options.TextOptions;

import java.util.ArrayList;
import java.util.List;


public class RadarView extends RelativeLayout {
    private CobwebOptions cobwebOptions ;
    private TextOptions textOptions ;
    private String[] texts;
    private Float textCanvasScale ;
    private int backgroundColor ;
    private Context context ;
    private CobwebView cobwebView ;


    public RadarView(Context context ,  Builder builder) {
        super(context);
        this.context = builder.context ;
        this.cobwebOptions = builder.cobwebOptions ;
        this.textOptions = builder.textOptions ;
        this.texts = builder.texts ;
        this.textCanvasScale = builder.textCanvasScale ;
        this.backgroundColor = builder.backgroundColor ;

        if(builder.texts.length != cobwebOptions.petal){
            throw new RuntimeException("文字数量与蛛网瓣数不一致！");
        }

        this.setLayoutParams(new LayoutParams(-1,-1));
        this.setBackground(new ShapeDrawable(new RadarShape()));

        this.removeAllViews();
        cobwebView = new CobwebView(context,builder) ;
        LayoutParams params = new LayoutParams(-1,-1);
        cobwebView.setLayoutParams(params);
        this.addView(cobwebView);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        this.getLayoutParams().height = r - l ;
    }

    public void addLine(DataLineOptions lineOptions , float[] values){
        if(cobwebView != null){
            cobwebView.addLine(lineOptions,values);
        }
    }

    public static class Builder {
        public Context context ;
        public CobwebOptions cobwebOptions ;
        public TextOptions textOptions ;
        public String[] texts ;
        public Float textCanvasScale = null;
        public Float cobwebCanvasScale = null;
        public int backgroundColor ;
        public float maxValue ;
        public List<DataLineBean> lineList ;

        public Builder textOptions(TextOptions textOptions){
            this.textOptions = textOptions ;
            return this ;
        }

        public Builder context(Context context){
            this.context = context ;
            return this ;
        }
        public Builder cobwebOptions(CobwebOptions cobwebOptions){
            this.cobwebOptions = cobwebOptions ;
            return this ;
        }
        public Builder texts(String[] texts){
            this.texts = texts ;
            return this ;
        }
        public Builder textCanvasScale(float textCanvasScale){
            this.textCanvasScale = textCanvasScale ;
            return this ;
        }
        public Builder cobwebCanvasScale(float cobwebCanvasScale){
            this.cobwebCanvasScale = cobwebCanvasScale ;
            return this ;
        }
        public Builder backgroundColor(int backgroundColor){
            this.backgroundColor = backgroundColor ;
            return this ;
        }
        public Builder maxValue(float maxValue){
            this.maxValue = maxValue ;
            return this ;
        }

        public RadarView build () {
            lineList = new ArrayList<>();
            return new RadarView(this.context,this);
        }
    }

    public static class DataLineBean{
        public DataLineOptions options ;
        public float[] values ;

        public DataLineBean(DataLineOptions options, float[] values) {
            this.options = options;
            this.values = values;
        }
    }

    private class RadarShape extends Shape {
        private int halfW ; //半尺寸
        private float A ; //每一瓣的角度

        @Override
        protected void onResize(float width, float height) {
            super.onResize(width, height);

            halfW = (int) (width / 2);
            A = 360f / cobwebOptions.petal;
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {
            /*绘制背景*/
            canvas.drawColor(backgroundColor);
            /*将画布缩小*/
            if(textCanvasScale != null){
                canvas.scale(textCanvasScale,textCanvasScale,halfW,halfW);
            }else{
                canvas.scale(0.9f,0.9f,halfW,halfW);
            }

            Point p ;
            for(int t = 0 ; t < texts.length ; t++){
                p = calcNewPoint(t * A);
                canvas.drawText(texts[t],p.x,p.y + sp2px(textOptions.textSize) / 3,getTextPaint());
            }
        }

        private Paint textPaint ;
        private Paint getTextPaint(){
            if(textPaint == null){
                textPaint = new Paint();
                textPaint.setTextSize(sp2px(textOptions.textSize));
                textPaint.setColor(textOptions.textColor);
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                textPaint.setAntiAlias(true);
                textPaint.setTypeface((textOptions.isBold == true) ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
            }
            return textPaint ;
        }

        private Point calcNewPoint(float angle) {
            Point p = new Point(halfW , 0 );
            Point pCenter = new Point(halfW,halfW) ;
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
    }

    public int sp2px( float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


}

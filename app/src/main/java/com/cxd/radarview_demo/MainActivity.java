package com.cxd.radarview_demo;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.cxd.radarview2.RadarView;
import com.cxd.radarview2.options.CobwebOptions;
import com.cxd.radarview2.options.TextOptions;
import com.cxd.radarview_demo.utils.DensityUtil;
import com.cxd.radarview_demo.utils.ScreenUtil;


public class MainActivity extends AppCompatActivity {

    private FrameLayout zhangmengFL ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zhangmengFL = findViewById(R.id.zhangmengFL);
        zhangmengFL.setLayoutParams(new LinearLayout.LayoutParams(-1,
                ScreenUtil.getScreenWidth(this)));

        zhangmengFL.addView(generateRadarView());
    }

    /*生成掌盟的雷达图*/
    private RadarView generateRadarView(){
        int petal = 20 ;
        /*文字样式*/
        TextOptions textOptions = new TextOptions.Builder()
                .textColor(Color.GRAY)
                .textSize(12)
                .isBold(true)
                .texts(new String[]{"击杀","生存","助攻", "理物","魔法","防御","金钱"})
                .build();
        /*蛛网填充颜色*/
        int[] colors = new int[]{Color.parseColor("#2b898e"),
                Color.parseColor("#58c0c9"),
                Color.parseColor("#9cdce0"),
                Color.parseColor("#d4f1f0"),
        } ;
        /*蜘蛛网样式*/
        CobwebOptions cobwebOptions = new CobwebOptions.Builder()
                .petal(petal)
                .silkWidth(2)
                .silkColor(Color.BLACK)
                .level(100)
                .fillStyle(CobwebOptions.FillStyle.LADDER)
                .fillColors(colors)
                .build();

        /*生成雷达控件*/
        RadarView radarView = new RadarView.Builder()
                .context(this)
                .textOptions(textOptions)
                .cobwebOptions(cobwebOptions)
                .maxVal(100)
                .padding(DensityUtil.dip2px(this,20))
                .space(DensityUtil.dip2px(this,15))
                .build();


        /*随机生成数据*/
        float[] values = new float[petal];
        float[] values2 = new float[petal];
        for(int i = 0 ; i < values.length ; i++){
            values[i] = (float) Math.max(50,Math.random() * 100);
            values2[i] = (float) Math.max(50,Math.random() * 100);
            Log.i("RadarView", "generateRadarView: = "+values[i]);
        }


        //添加一条数据线
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.parseColor("#5011ffff"));
        p.setStrokeWidth(3);
        radarView.addLine(values,p);

        Paint p2 = new Paint();
        p2.setStyle(Paint.Style.STROKE);
        p2.setColor(Color.RED);
        p2.setStrokeWidth(3);
        radarView.addLine(values2,p2);

//        radarView.removeLines();

        return radarView ;
    }

}

package com.cxd.radarview;

import android.graphics.Color;
import android.util.Log;

import java.util.Arrays;

@Deprecated
public class ColorUtils {

    /**
     * 根据颜色起始值 和终止值 生成level个过渡颜色代码值
     * 可能存在精度不足问题，导致的后果是，颜色整体偏淡
     *
     * 开始颜色需要比终止颜色淡
     * @param startC
     * @param endC
     * @param level
     * @return
     */
    public static int[] generateColors(String startC , String endC , int level){

        if(startC == null || endC == null || (startC.length() != 7 && startC.length() != 9) || (endC.length() != 7 && endC.length() != 9)){
            return null ;
        }

        /*忽视透明度*/
        if(startC.length() == 9){
            startC = startC.substring(2,9);
        }

        if(endC.length() == 9){
            endC = endC.substring(2,9);
        }

        startC = startC.substring(1,7);
        endC = endC.substring(1,7);

        int redStartVal = Integer.parseInt(startC.substring(0,2),16);
        int greenStartVal = Integer.parseInt(startC.substring(2,4),16);
        int blueStartVal = Integer.parseInt(startC.substring(4,6),16);

        int redEndVal = Integer.parseInt(endC.substring(0,2),16);
        int greenEndVal = Integer.parseInt(endC.substring(2,4),16);
        int blueEndVal = Integer.parseInt(endC.substring(4,6),16);

        int redSpace = (redEndVal - redStartVal) / level ;
        int greenSpace = (greenEndVal - greenStartVal) / level ;
        int blueSpace = (blueEndVal - blueStartVal) / level ;
        
        int[] colors = new int[level];
        for(int i = 0; i < level ; i++){
            colors[i] = Color.parseColor(
                    "#" + intToHexStr(redStartVal + redSpace * i)
                    + intToHexStr(greenStartVal + greenSpace * i)
                    + intToHexStr(blueStartVal + blueSpace * i)
                    );
        }
        return colors ;
    }

    /**
     * int 值转成 十六进制字符串 例如：204 -> CC
     * @param decimal
     * @return
     */
    public static String intToHexStr(int decimal){
        int I = decimal / 16 ;
        int i = decimal % 16 ;
        return Integer.toHexString(I)+Integer.toHexString(i);
    }
}

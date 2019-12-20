package com.cxd.radarview;

import android.graphics.Color;

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

        /*忽视透明度*/
        if(startC.length() == 9){
            startC = startC.substring(2,9);
        }

        if(endC.length() == 9){
            endC = endC.substring(2,9);
        }

        startC = startC.substring(1,7);
        endC = endC.substring(1,7);

        int redStartVal = hexStrToInt(startC.substring(0,2));
        int greenStartVal = hexStrToInt(startC.substring(2,4));
        int blueStartVal = hexStrToInt(startC.substring(4,6));

        int redEndVal = hexStrToInt(endC.substring(0,2));
        int greenEndVal = hexStrToInt(endC.substring(2,4));
        int blueEndVal = hexStrToInt(endC.substring(4,6));

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
     * 两个字母转成十进制数值 例如：FF -> 255
     * @param xs
     * @return
     */
    public static int hexStrToInt(String xs){
        if(xs.length() != 2 ){
            return -1 ;
        }

        //第一个字母的十进制数字
        int firstDecimal = hexLetterToInt(String.valueOf(xs.charAt(0)));
        int secondDecimal = hexLetterToInt(String.valueOf(xs.charAt(1)));

        return firstDecimal * 16 + secondDecimal ;
    }


    /**
     * 单个字母转成十进制数值 例如：A -> 10
     * @param letter
     * @return
     */
    public static int hexLetterToInt(String letter){
        int i ;
        switch(letter){
            case "0":
                i = 0 ;
                break;
            case "1":
                i = 1 ;
                break;
            case "2":
                i = 2 ;
                break;
            case "3":
                i = 3 ;
                break;
            case "4":
                i = 4 ;
                break;
            case "5":
                i = 5 ;
                break;
            case "6":
                i = 6 ;
                break;
            case "7":
                i = 7 ;
                break;
            case "8":
                i = 8 ;
                break;
            case "9":
                i = 9 ;
                break;
            case "A":
            case "a":
                i = 10 ;
                break;
            case "B":
            case "b":
                i = 11 ;
                break;
            case "C":
            case "c":
                i = 12 ;
                break;
            case "D":
            case "d":
                i = 13 ;
                break;
            case "E":
            case "e":
                i = 14 ;
                break;
            case "F":
            case "f":
                i = 15 ;
                break;
            default:
                i = -1 ;
                break;
        }
        return i ;
    }

    /**
     * int 值转成 十六进制字符串 例如：204 -> CC
     * @param decimal
     * @return
     */
    public static String intToHexStr(int decimal){
        int I = decimal / 16 ;
        int i = decimal % 16 ;
        return intToHexLetter(I)+intToHexLetter(i);
    }

    /**
     * 单个int 数值转成单个十六进制字母 例如：13 -> D
     * @param i
     * @return
     */
    public static String intToHexLetter(int i){
        if(i >= 0 && i<10){
            return String.valueOf(i);
        }else{
            String letter ;

            switch(i){
                case 10:
                    letter = "A";
                    break;
                case 11:
                    letter = "B";
                    break;
                case 12:
                    letter = "C";
                    break;
                case 13:
                    letter = "D";
                    break;
                case 14:
                    letter = "E";
                    break;
                case 15:
                    letter = "F";
                    break;
                default:
                    letter = "";
                    break;
            }
            return letter ;
        }

    }
}

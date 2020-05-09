package com.cxd.radarview2.options;

/**
 * 蛛网样式
 */
public class CobwebOptions {
    public int silkWidth ; //蛛丝宽度
    public int silkColor ; //蛛丝颜色
    public int petal ; //蛛丝瓣数
    public int level ;  //蛛丝层数
    public int[] fillColors ; //蛛丝填充颜色（中心->外围）
    public FillStyle fillStyle ; //蛛网颜色填充样式

    public CobwebOptions(Builder builder) {
        this.silkWidth = builder.silkWidth ;
        this.silkColor = builder.silkColor ;
        this.petal = builder.petal ;
        this.level = builder.level ;
        this.fillColors = builder.fillColors ;
        this.fillStyle = builder.fillStyle ;
    }

    public static class Builder{
        private int silkWidth ;
        private int silkColor ;
        private int petal ;
        private int level ;
        private int[] fillColors ;
        private FillStyle fillStyle ;

        public Builder silkWidth(int silkWidth) {
            this.silkWidth = silkWidth ;
            return this ;
        }
        public Builder silkColor(int silkColor) {
            this.silkColor = silkColor ;
            return this ;
        }
        public Builder petal(int petal) {
            this.petal = petal ;
            return this ;
        }
        public Builder level(int level) {
            this.level = level ;
            return this ;
        }
        public Builder fillColors(int[] fillColors) {
            this.fillColors = fillColors ;
            return this ;
        }
        public Builder fillStyle(FillStyle fillStyle) {
            this.fillStyle = fillStyle ;
            return this ;
        }

        public CobwebOptions build(){
            return new CobwebOptions(this);
        }
    }

    public enum FillStyle{
        LADDER ,  //阶梯
        GRADUAL  //渐变
    }
}

package com.cxd.radarview2.options;

/**
 * 文本样式
 */
public class TextOptions {
    public int textSize ; //文字尺寸
    public int textColor ; //文字颜色
    public boolean isBold ; //文字是否加粗
    public String[] texts ; //文字集合


    public TextOptions(Builder builder) {
        this.textSize = builder.textSize;
        this.textColor = builder.textColor;
        this.isBold = builder.isBold;
        this.texts = builder.texts;
    }

    public static class Builder{
        private int textSize ;
        private int textColor ;
        private boolean isBold ;
        private String[] texts ;
        private int space ;

        public Builder textSize(int textSize){
            this.textSize = textSize ;
            return this ;
        }
        public Builder textColor(int textColor){
            this.textColor = textColor ;
            return this ;
        }
        public Builder isBold(boolean isBold){
            this.isBold = isBold ;
            return this ;
        }
        public Builder texts(String[] texts){
            this.texts = texts ;
            return this ;
        }

        public TextOptions build(){
            return new TextOptions(this);
        }
    }

}

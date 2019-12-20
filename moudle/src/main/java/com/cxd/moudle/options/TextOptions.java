package com.cxd.moudle.options;

public class TextOptions {
    public int textSize ;
    public int textColor ;
    public boolean isBold ;

    public TextOptions(Builder builder) {
        this.textSize = builder.textSize;
        this.textColor = builder.textColor;
        this.isBold = builder.isBold;
    }

    public static class Builder{
        public int textSize ;
        public int textColor ;
        public boolean isBold ;

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
        public TextOptions build(){
            return new TextOptions(this);
        }
    }

}

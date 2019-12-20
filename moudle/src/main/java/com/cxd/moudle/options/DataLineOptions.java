package com.cxd.moudle.options;

public class DataLineOptions {
    public int lineWidth ;
    public int lineColor ;
    public boolean isFill ;

    public DataLineOptions(Builder builder) {
        this.lineWidth = builder.lineWidth ;
        this.lineColor = builder.lineColor ;
        this.isFill = builder.isFill ;
    }

    public static class Builder{
        public int lineWidth ;
        public int lineColor ;
        public boolean isFill ;

        public Builder lineWidth(int lineWidth ){
            this.lineWidth = lineWidth ;
            return this;
        }
        public Builder lineColor(int lineColor ){
            this.lineColor = lineColor ;
            return this;
        }
        public Builder isFill(boolean isFill ){
            this.isFill = isFill ;
            return this;
        }
        public DataLineOptions build(){
            return new DataLineOptions(this);
        }

    }
}

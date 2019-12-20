package com.cxd.moudle.options;

public class CobwebOptions {
    public int silkWidth ;
    public int silkColor ;
    public int petal ;
    public int level ;
    public int[] cobwebLevelColors ;
    public String centerColorStr ;
    public String peripheralColorStr ;

    public CobwebOptions(Builder builder) {
        this.silkWidth = builder.silkWidth ;
        this.silkColor = builder.silkColor ;
        this.petal = builder.petal ;
        this.level = builder.level ;
        this.cobwebLevelColors = builder.cobwebLevelColors ;
        this.centerColorStr = builder.centerColorStr ;
        this.peripheralColorStr = builder.peripheralColorStr ;
    }

    public static class Builder{
        public int silkWidth ;
        public int silkColor ;
        public int petal ;
        public int level ;
        public int[] cobwebLevelColors ;
        public String centerColorStr ;
        public String peripheralColorStr ;


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
        public Builder cobwebLevelColors(int[] cobwebLevelColors) {
            this.cobwebLevelColors = cobwebLevelColors ;
            return this ;
        }
        public Builder centerColorStr(String centerColorStr) {
            this.centerColorStr = centerColorStr ;
            return this ;
        }
        public Builder peripheralColorStr(String peripheralColorStr) {
            this.peripheralColorStr = peripheralColorStr ;
            return this ;
        }

        public CobwebOptions build(){
            return new CobwebOptions(this);
        }
    }
}

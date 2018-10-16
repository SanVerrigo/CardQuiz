package com.verrigo.cardquiz;

public class Pack {
    private int _id;
    private String packName;

    public Pack(String packName) {
        this.packName = packName;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }
}

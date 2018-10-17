package com.verrigo.cardquiz;

public class Pack {
    private int _id;
    private String packName;

    public Pack(int _id, String packName) {
        this._id = _id;
        this.packName = packName;
    }

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

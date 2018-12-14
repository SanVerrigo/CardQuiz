package com.verrigo.cardquiz;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Pack {

    @SerializedName("id")
    private int _id;

    @SerializedName("packName")
    private String packName;

    @SerializedName("cards")
    private ArrayList<Card> cards;

    public Pack(int _id, String packName) {
        this._id = _id;
        this.packName = packName;
    }

    public Pack(int _id, String packName, ArrayList<Card> cards) {
        this._id = _id;
        this.packName = packName;
        this.cards = cards;
    }

    public Pack(String packName) {
        this.packName = packName;
    }

    public String getPackName() {
        return packName;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
}

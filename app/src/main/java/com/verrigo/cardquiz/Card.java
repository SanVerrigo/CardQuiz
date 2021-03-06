package com.verrigo.cardquiz;

public class Card {

    private int _id;
    private String packName;
    private String question;
    private String answer;

    private boolean isShown = true;

    public Card(String packName, String question, String answer) {
        this.packName = packName;
        this.question = question;
        this.answer = answer;
    }

    public Card(int _id, String packName, String question, String answer) {
        this._id = _id;
        this.packName = packName;
        this.question = question;
        this.answer = answer;
    }

public Card(int _id, String packName, String question, String answer, boolean isShown) {
        this.isShown = isShown;
        this._id = _id;
        this.packName = packName;
        this.question = question;
        this.answer = answer;
    }



    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public String getPackName() {
        return packName;
    }

    public String getAnswer() {
        return answer;
    }

    public int get_id() {
        return _id;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public boolean isShown() {
        return isShown;
    }
}

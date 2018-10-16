package com.verrigo.cardquiz;

public class Card {

    public Card(String packName, String question, String answer) {
        this.packName = packName;
        this.question = question;
        this.answer = answer;
    }

    private String packName;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    private String question;
    private String answer;
}

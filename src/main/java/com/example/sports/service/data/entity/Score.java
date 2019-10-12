package com.example.sports.service.data.entity;

public enum Score {
    LOVE("LOVE", 0),
    FIFTEEN("FIFTEEN", 15),
    THIRTY("THIRTY", 30),
    FOURTY("FOURTY", 40),
    ADV("ADV", -1),
    WIN("WIN", -2);

    private String score;
    private int number;

    Score (String score, int number) {
        this.number = number;
        this.score = score;
    }

    public int getValue(){
        return  number;
    }
}

package com.example.sports.tennis.request;

import javax.validation.constraints.NotNull;

public class CreateMatchRequest {
    @NotNull
    private String player1;

    @NotNull
    private String player2;

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }
}

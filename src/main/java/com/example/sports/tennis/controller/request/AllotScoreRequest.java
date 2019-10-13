package com.example.sports.tennis.controller.request;

import javax.validation.constraints.NotNull;

public class AllotScoreRequest {

    @NotNull
    private  String playerName;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}

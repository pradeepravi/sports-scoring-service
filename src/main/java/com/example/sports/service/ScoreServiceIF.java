package com.example.sports.service;

import com.example.sports.service.data.entity.MatchSet;
import com.example.sports.service.exception.BadRequestException;
import com.example.sports.service.exception.MatchAlreadyConcludedException;
import com.example.sports.service.exception.NotFoundException;

import java.util.Map;

public interface ScoreServiceIF {

    MatchSet pointWonBy(Long matchId, String playerName) throws NotFoundException, BadRequestException, MatchAlreadyConcludedException;


    Map<String, String> getScores(Long matchId) throws NotFoundException;
}

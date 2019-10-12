package com.example.sports.service;

import com.example.sports.service.data.entity.Match;
import com.example.sports.service.exception.BadRequestException;
import com.example.sports.tennis.request.CreateMatchRequest;

import java.util.List;

public interface MatchServiceIF {
    Match createMatch(CreateMatchRequest request) throws BadRequestException;

    List<String>  getAllMatches();
}

package com.example.sports.service;

import com.example.sports.service.data.entity.TennisMatch;
import com.example.sports.service.exception.BadRequestException;
import com.example.sports.tennis.controller.request.CreateMatchRequest;

import java.util.List;

public interface MatchServiceIF {
    TennisMatch createMatch(CreateMatchRequest request) throws BadRequestException;
}

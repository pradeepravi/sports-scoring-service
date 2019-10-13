package com.example.sports.service;

import com.example.sports.service.data.entity.TennisMatch;
import com.example.sports.service.data.repository.MatchRepository;
import com.example.sports.service.exception.BadRequestException;
import com.example.sports.tennis.controller.request.CreateMatchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TennisMatchServiceImpl implements  MatchServiceIF{

    private MatchRepository matchRepository;

    @Autowired
    public TennisMatchServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public TennisMatch createMatch(CreateMatchRequest request) throws BadRequestException {
        if(request.getPlayer1().equalsIgnoreCase(request.getPlayer2())) {
            throw new BadRequestException("Please select two different names");
        }

        TennisMatch tennisMatch = new TennisMatch(request.getPlayer1(), request.getPlayer2());
        tennisMatch = matchRepository.save(tennisMatch);
        return tennisMatch;
    }
}

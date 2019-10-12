package com.example.sports.service;

import com.example.sports.service.data.entity.Match;
import com.example.sports.service.data.entity.repository.MatchRepository;
import com.example.sports.service.exception.BadRequestException;
import com.example.sports.tennis.request.CreateMatchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TennisMatchServiceImpl implements  MatchServiceIF{

    private MatchRepository matchRepository;

    @Autowired
    public TennisMatchServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public Match createMatch(CreateMatchRequest request) throws BadRequestException {
        if(request.getPlayer1().equalsIgnoreCase(request.getPlayer2())) {
            throw new BadRequestException("Please select two different names");
        }

        Match match = new Match(request.getPlayer1(), request.getPlayer2());
        match = matchRepository.save(match);
        return match;
    }

    @Override
    public List<String> getAllMatches() {
        //TODO
        return null;
    }
}

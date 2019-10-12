package com.example.sports.tennis;

import com.example.sports.service.ScorerServiceIF;
import com.example.sports.service.data.entity.MatchSet;
import com.example.sports.service.exception.BadRequestException;
import com.example.sports.service.exception.MatchAlreadyConcludedException;
import com.example.sports.service.exception.NotFoundException;
import com.example.sports.tennis.request.AllotScoreRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/api/v1/sports/tennis/scores")
public class TennisScorerController {

    private ScorerServiceIF sportsScoreServiceIF;

    @Autowired
    public TennisScorerController(ScorerServiceIF sportsScoreServiceIF) {
        this.sportsScoreServiceIF = sportsScoreServiceIF;
    }

    @ResponseBody
    @PostMapping("/{matchId}")
    MatchSet pointWonBy(@PathVariable("matchId") Long matchId, @NotNull @RequestBody() AllotScoreRequest request)
            throws NotFoundException, BadRequestException, MatchAlreadyConcludedException {
        return sportsScoreServiceIF.pointWonBy(matchId, request.getPlayerName());
    }

    @ResponseBody
    @GetMapping("/{matchId}")
    Map<String, String> getGameScore(@PathVariable Long matchId) throws NotFoundException {
        return sportsScoreServiceIF.getScores(matchId);
    }
}

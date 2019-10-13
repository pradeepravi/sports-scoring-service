package com.example.sports.tennis.controller;

import com.example.sports.service.ScoreServiceIF;
import com.example.sports.service.data.entity.MatchSet;
import com.example.sports.service.exception.BadRequestException;
import com.example.sports.service.exception.MatchAlreadyConcludedException;
import com.example.sports.service.exception.NotFoundException;
import com.example.sports.tennis.controller.request.AllotScoreRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Api(value="Tennis Scorer Controller", description="Operation to perform tennis scoring operations")
@RestController
@CrossOrigin(origins = {"${supported.settings.cors_origin}"})
@RequestMapping(path = "/api/v1/sports/tennis/scores")
public class TennisScorerController {

    private ScoreServiceIF sportsScoreServiceIF;

    @Autowired
    public TennisScorerController(ScoreServiceIF sportsScoreServiceIF) {
        this.sportsScoreServiceIF = sportsScoreServiceIF;
    }

    @ApiOperation(value = "Score a player with next logical score for the match", response = MatchSet.class)
    @ResponseBody
    @PostMapping("/{matchId}")
    MatchSet pointWonBy(@PathVariable("matchId") Long matchId, @NotNull @RequestBody() AllotScoreRequest request)
            throws NotFoundException, BadRequestException, MatchAlreadyConcludedException {
        return sportsScoreServiceIF.pointWonBy(matchId, request.getPlayerName());
    }

    @ApiOperation(value = "Fetch current scoreboard", response = Map.class)
    @ResponseBody
    @GetMapping("/{matchId}")
    Map<String, String> getGameScore(@PathVariable Long matchId) throws NotFoundException {
        return sportsScoreServiceIF.getScores(matchId);
    }
}

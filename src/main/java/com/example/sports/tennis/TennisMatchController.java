package com.example.sports.tennis;

import com.example.sports.service.MatchServiceIF;
import com.example.sports.service.data.entity.Match;
import com.example.sports.service.exception.BadRequestException;
import com.example.sports.tennis.request.CreateMatchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/api/v1/tennis/matches")
public class TennisMatchController {

    private MatchServiceIF matchServiceIF;

    @Autowired
    public TennisMatchController(MatchServiceIF matchServiceIF) {
        this.matchServiceIF = matchServiceIF;
    }

    @ResponseBody
    @PostMapping
    public Match createMatch(@Valid @RequestBody CreateMatchRequest request) throws BadRequestException {
        return matchServiceIF.createMatch(request);
    }

    /*@GetMapping("/{matchId}")
    ResponseEntity<String> getAllGames(@PathVariable int  matchId){

        return null;
    }*/
}

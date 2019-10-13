package com.example.sports.tennis.controller;

import com.example.sports.service.MatchServiceIF;
import com.example.sports.service.data.entity.MatchSet;
import com.example.sports.service.data.entity.TennisMatch;
import com.example.sports.service.exception.BadRequestException;
import com.example.sports.tennis.controller.request.CreateMatchRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value="Tennis Match Controller", description="Operation to perform Match related operations")
@RestController
@CrossOrigin(origins = {"${supported.settings.cors_origin}"})
@RequestMapping(path = "/api/v1/sports/tennis/matches")
public class TennisMatchController {

    private MatchServiceIF matchServiceIF;


    @Autowired
    public TennisMatchController(MatchServiceIF matchServiceIF) {
        this.matchServiceIF = matchServiceIF;
    }

    @ApiOperation(value = "Creates a new match", response = TennisMatch.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created a match"),
            @ApiResponse(code = 400, message = "Bad Request. Same player names")
    })
    @ResponseBody
    @PostMapping
    public TennisMatch createMatch(@Valid @RequestBody CreateMatchRequest request) throws BadRequestException {
        return matchServiceIF.createMatch(request);
    }
}

package com.example.sports.service;

import com.example.sports.service.data.entity.Match;
import com.example.sports.service.data.entity.MatchSet;
import com.example.sports.service.data.entity.Score;
import com.example.sports.service.data.entity.repository.MatchRepository;
import com.example.sports.service.data.entity.repository.SetRepository;
import com.example.sports.service.exception.BadRequestException;
import com.example.sports.service.exception.LastSetEndedException;
import com.example.sports.service.exception.MatchAlreadyConcludedException;
import com.example.sports.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TennisScoreServiceImpl implements ScorerServiceIF {

    public static final Integer WINNING_SETS_COUNT = 6;
    private SetRepository setRepository;
    private MatchRepository matchRepository;

    @Autowired
    public TennisScoreServiceImpl(SetRepository setRepository, MatchRepository matchRepository) {
        this.setRepository = setRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public MatchSet pointWonBy(Long matchId, String scoreByPlayer) throws NotFoundException, BadRequestException, MatchAlreadyConcludedException {

        final Match match = matchRepository.findById(matchId).orElseThrow(() -> new NotFoundException("Match not found for MatchID -"+matchId));
        validate(match, scoreByPlayer);
        final MatchSet lastSet = getLastScore(matchId);
        final MatchSet newSet = generateNewMatchSet(scoreByPlayer, match);

        if(lastSet == null) {
            // First set
            newSet.setScore(Score.FIFTEEN);
            newSet.setSetId(1);
            setRepository.save(newSet);
        } else {
            int setId = lastSet.getSetId();
            if (lastSet.getScore() == Score.WIN) {
                // Next Set as the last was already won by someone
                setId ++;
            }
            List<Map<String, String>> map =  setRepository.findByNameMatchIdAndSetIdGroupByPointsWonBy(matchId, setId);

            final String currentPlayersScore= map.stream().filter(m -> scoreByPlayer.equalsIgnoreCase(m.get("player")))
                    .map(m -> m.get("score")).findFirst().orElse(Score.LOVE.toString());

            final String otherPlayersScore = map.stream().filter(m -> !scoreByPlayer.equalsIgnoreCase(m.get("player")))
                    .map(m -> m.get("score")).findFirst().orElse(Score.LOVE.toString());

            final boolean isDeuce = (Score.valueOf(currentPlayersScore) == Score.FOURTY && (Score.valueOf(otherPlayersScore) == Score.FOURTY)
                                        || Score.valueOf(currentPlayersScore) == Score.ADV || (Score.valueOf(otherPlayersScore) == Score.ADV));
            final boolean isLastScorerSameAsCurrent = scoreByPlayer.equalsIgnoreCase(lastSet.getPointsWonBy());

            Score nextScore;
            try {
                nextScore = getNextScore(Score.valueOf(currentPlayersScore), isDeuce, isLastScorerSameAsCurrent);
            } catch (LastSetEndedException e) {

                // New Set Starts .. super Safety. Might not happen
                setId++;
                nextScore = Score.FIFTEEN;
                e.printStackTrace();
            }
            newSet.setScore(nextScore);
            newSet.setSetId(setId);
            setRepository.save(newSet);
        }
        return newSet;
    }

    private void validate(final Match match, final String scoreByPlayer) throws MatchAlreadyConcludedException, BadRequestException {

        final List<Map<String, String>> winsCount= setRepository.findAllGroupByPointsWonBy(match.getId(), Arrays.asList(Score.WIN));

        final Map<String, Integer> mapOfWins = winsCount.stream().collect(
                Collectors.toMap(s -> s.get("player"), s -> (Integer.parseInt(s.get("wins")))));
        if(mapOfWins.containsValue(WINNING_SETS_COUNT)){
            throw new MatchAlreadyConcludedException("Match Already Over!");
        }

        if(!(match.getPlayer1().equalsIgnoreCase(scoreByPlayer) || match.getPlayer2().equalsIgnoreCase(scoreByPlayer))) {
            throw new BadRequestException("The Player Name Passed {"+scoreByPlayer+"} is Incorrect for match ID {"+match.getId()+"}");
        }
    }

    private MatchSet generateNewMatchSet(String scoreByPlayer, Match match) {
        MatchSet newSet = new MatchSet();
        newSet.setCreatedOn(OffsetDateTime.now());
        newSet.setPointsWonBy(scoreByPlayer);
        newSet.setMatch(match);
        return newSet;
    }

    @Override
    public Map<String, String> getScores(final Long matchId) throws NotFoundException {
        final Match match = matchRepository.findById(matchId).orElseThrow(() -> new NotFoundException("Match not found for MatchID {"+matchId+"}"));
        final List<Map<String, String>> winsCount= setRepository.findAllGroupByPointsWonBy(matchId, Arrays.asList(Score.WIN));

        final Map<String, Integer> mapOfWins = winsCount.stream().collect(
                Collectors.toMap(s -> s.get("player"), s -> (Integer.parseInt(s.get("wins")))));

        final MatchSet latestScore = getLastScore(matchId);
        final Map <String, String> responseMap= new HashMap<>();

        if(latestScore != null ) {
            int setId = latestScore.getSetId();

            List<Map<String, String>> map = setRepository.findByNameMatchIdAndSetIdGroupByPointsWonBy(matchId, setId);
            final Map<String, String> lastScoresOfCurrentSet = map.stream().collect( Collectors.toMap(s -> s.get("player"), s -> (s.get("score"))));

            final StringBuffer returnMessage = new StringBuffer();
            returnMessage.append(mapOfWins.getOrDefault(match.getPlayer1(), 0)).append(" - ");
            returnMessage.append(mapOfWins.getOrDefault(match.getPlayer2(), 0)).append(", ");

            final List<String> lastScores = new ArrayList<>(lastScoresOfCurrentSet.values());

            String str = null;

            if (mapOfWins.values().contains(WINNING_SETS_COUNT)) {
                String winningPlayer = mapOfWins.entrySet().stream().filter(entry -> (WINNING_SETS_COUNT.equals(entry.getValue()))).map(
                        Map.Entry::getKey).findFirst().orElse(null);

                str = winningPlayer +" Wins!";
            } else if(lastScores.contains(Score.ADV.toString()) || (Collections.frequency(lastScores, Score.FOURTY.toString()) == 2)) {
                str = "Deuce";
            }

            if(str == null) {
                if(lastScoresOfCurrentSet.values().contains(Score.WIN.toString())) {
                    returnMessage.append("0 - 0");
                } else {
                    returnMessage.append(Score.valueOf(lastScoresOfCurrentSet.getOrDefault(match.getPlayer1(), Score.LOVE.toString()))
                            .getValue()).append(" - ");
                    returnMessage.append(Score.valueOf(lastScoresOfCurrentSet.getOrDefault(match.getPlayer2(), Score.LOVE.toString()))
                            .getValue());
                }
            } else {
                returnMessage.append(str);
            }
            responseMap.put("scoreboard",returnMessage.toString());
        } else {
            responseMap.put("scoreboard", "Match Not Started!");
        }
        return responseMap;
    }
    private MatchSet getLastScore(Long matchId){
        MatchSet match = null;
        final List<MatchSet> optionalLastSetScorer = setRepository.findByMatchId(matchId);
        if(optionalLastSetScorer != null && optionalLastSetScorer.size() > 0) {
            match= optionalLastSetScorer.get(0);
        }
        return match;
    }
    private Score getNextScore(Score score, boolean isDeuce, boolean isLastScorerSameAsCurrent) throws LastSetEndedException {
        Score renameScore;
        switch (score) {
            case LOVE:
                renameScore = Score.FIFTEEN;
                break;

            case FIFTEEN:
                renameScore = Score.THIRTY;
                break;

            case  THIRTY:
                renameScore = Score.FOURTY;
                break;

            case FOURTY:
                renameScore = isDeuce? Score.ADV : Score.WIN;
                break;

            case ADV:
                renameScore = isLastScorerSameAsCurrent? Score.WIN : Score.ADV;
                break;

            case WIN:
            default:
                throw new LastSetEndedException();
        }

        return renameScore;
    }
}
package com.sportradar.core.scoreboard.dao.impl;

import com.sportradar.core.scoreboard.dao.ScoreBoardDao;
import com.sportradar.core.scoreboard.domain.MatchDetails;
import com.sportradar.core.scoreboard.domain.MatchScore;
import com.sportradar.core.scoreboard.domain.TeamScore;
import com.sportradar.core.scoreboard.exception.MatchAlreadyStartedException;
import com.sportradar.core.scoreboard.exception.MatchNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

public class InMemoryScoreBoardDao implements ScoreBoardDao {
    private static final Logger log = LoggerFactory.getLogger(InMemoryScoreBoardDao.class);

    private static final Comparator<MatchScore> SCORE_ARRANGER = Comparator
            .comparingInt(MatchScore::totalScore)
            .thenComparingLong(MatchScore::startTime)
            .reversed();

    private final Map<MatchDetails, MatchScore> scores;

    public InMemoryScoreBoardDao() {
        scores = new ConcurrentHashMap<>();
    }

    public InMemoryScoreBoardDao(Map<MatchDetails, MatchScore> scores) {
        this.scores = scores;
    }

    @Override
    public void startNewScore(MatchDetails matchDetails) {
        ofNullable(scores.get(matchDetails))
                .ifPresentOrElse(logAndThrowAlreadyStartedException(), initScore(matchDetails));
    }

    @Override
    public void updateScore(MatchScore matchScore) {
        ofNullable(scores.get(MatchDetails.fromMatchScore(matchScore)))
                .map(refreshScore(matchScore))
                .orElseThrow(
                        logAndThrowNotFoundException(
                                matchScore.homeScore().teamName(), matchScore.awayScore().teamName()
                        ));
    }

    @Override
    public void finishScore(MatchDetails matchDetails) {
        ofNullable(scores.get(matchDetails))
                .map(removeScore(matchDetails))
                .orElseThrow(
                        logAndThrowNotFoundException(
                                matchDetails.homeTeamName(), matchDetails.awayTeamName()
                        ));
    }

    @Override
    public List<MatchScore> listScores() {
        return scores.values()
                .stream()
                .sorted(SCORE_ARRANGER)
                .toList();
    }

    @Override
    public void cleanUp() {
        scores.clear();
    }

    public Map<MatchDetails, MatchScore> getContent() {
        return scores;
    }

    private Runnable initScore(MatchDetails matchDetails) {
        return () -> scores.put(
                matchDetails,
                new MatchScore(
                        new TeamScore(matchDetails.homeTeamName(), 0),
                        new TeamScore(matchDetails.awayTeamName(), 0),
                        Instant.now().toEpochMilli()
                )
        );
    }

    private Function<MatchScore, MatchScore> refreshScore(MatchScore matchScore) {
        return scoreToUpdate -> scores.put(
                MatchDetails.fromMatchScore(matchScore),
                new MatchScore(
                        matchScore.homeScore(),
                        matchScore.awayScore(),
                        scoreToUpdate.startTime()
                )
        );
    }

    private Function<MatchScore, MatchScore> removeScore(MatchDetails matchDetails) {
        return matchScore -> scores.remove(matchDetails);
    }

    private Consumer<MatchScore> logAndThrowAlreadyStartedException() {
        return matchScore -> {
            log.error("Match between {} - {} is already started",
                    matchScore.homeScore().teamName(),
                    matchScore.awayScore().teamName());
            throw new MatchAlreadyStartedException();
        };
    }

    private Supplier<MatchNotFoundException> logAndThrowNotFoundException(String homeTeamName,
                                                                          String awayTeamName) {
        return () -> {
            log.error("Match between {} - {} is not found", homeTeamName, awayTeamName);
            return new MatchNotFoundException();
        };
    }
}

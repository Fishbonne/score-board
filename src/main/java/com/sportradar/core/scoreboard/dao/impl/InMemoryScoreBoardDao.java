package com.sportradar.core.scoreboard.dao.impl;

import com.sportradar.core.scoreboard.dao.ScoreBoardDao;
import com.sportradar.core.scoreboard.domain.MatchDetails;
import com.sportradar.core.scoreboard.domain.MatchScore;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryScoreBoardDao implements ScoreBoardDao {

    private final Map<MatchDetails, MatchScore> scores;

    public InMemoryScoreBoardDao() {
        scores = new ConcurrentHashMap<>();
    }

    public InMemoryScoreBoardDao(Map<MatchDetails, MatchScore> scores) {
        this.scores = scores;
    }

    @Override
    public void startNewScore(MatchDetails matchDetails) {
//        fixme
    }

    @Override
    public void updateScore(MatchScore matchScore) {
//        fixme
    }

    @Override
    public void finishScore(MatchDetails matchDetails) {
//        fixme
    }

    @Override
    public List<MatchScore> listScores() {
//        fixme
        return null;
    }

    @Override
    public void cleanUp() {
        scores.clear();
    }

    public Map<MatchDetails, MatchScore> getContent() {
        return scores;
    }
}

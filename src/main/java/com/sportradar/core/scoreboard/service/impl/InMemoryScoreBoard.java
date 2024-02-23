package com.sportradar.core.scoreboard.service.impl;

import com.sportradar.core.scoreboard.dao.ScoreBoardDao;
import com.sportradar.core.scoreboard.domain.MatchDetails;
import com.sportradar.core.scoreboard.domain.MatchScore;
import com.sportradar.core.scoreboard.service.ScoreBoard;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public class InMemoryScoreBoard implements ScoreBoard {
    private final ScoreBoardDao scoreBoardDao;

    public InMemoryScoreBoard(ScoreBoardDao scoreBoardDao) {
        this.scoreBoardDao = scoreBoardDao;
    }

    @Override
    public void startNewScore(MatchDetails matchDetails) {
        scoreBoardDao.startNewScore(matchDetails);
    }

    @Override
    public void updateScore(MatchScore matchScore) {
        scoreBoardDao.updateScore(matchScore);
    }

    @Override
    public void finishScore(MatchDetails matchDetails) {
        scoreBoardDao.finishScore(matchDetails);
    }

    @Override
    public List<MatchScore> listScores() {
        return scoreBoardDao.listScores();
    }

    @Override
    public void cleanUp() {
        scoreBoardDao.cleanUp();
    }
}

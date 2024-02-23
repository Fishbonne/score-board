package com.sportradar.core.scoreboard.service;


import com.sportradar.core.scoreboard.domain.MatchDetails;
import com.sportradar.core.scoreboard.domain.MatchScore;

import java.util.List;

public interface ScoreBoard {

    void startNewScore(MatchDetails matchDetails);

    void updateScore(MatchScore matchScore);

    void finishScore(MatchDetails matchDetails);

    List<MatchScore> listScores();

    void cleanUp();
}

package com.sportradar.core.scoreboard.service;


import com.sportradar.core.scoreboard.domain.MatchDetails;
import com.sportradar.core.scoreboard.domain.MatchScore;
import jakarta.validation.Valid;

import java.util.List;

public interface ScoreBoard {

    void startNewScore(@Valid MatchDetails matchDetails);

    void updateScore(@Valid MatchScore matchScore);

    void finishScore(@Valid MatchDetails matchDetails);

    List<MatchScore> listScores();

    void cleanUp();
}

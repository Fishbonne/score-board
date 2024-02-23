package com.sportradar.core.scoreboard.service;


import com.sportradar.core.scoreboard.domain.MatchDetails;
import com.sportradar.core.scoreboard.domain.MatchScore;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

import java.util.List;

public interface ScoreBoard {

    /**
     * Creates match with score 0-0 for provided <CODE><I>matchDetails</I></CODE>
     *
     * @param matchDetails contains home and away team names {@link MatchDetails}
     * @throws ConstraintViolationException if <CODE><I>matchDetails</I></CODE> is invalid
     * @throws com.sportradar.core.scoreboard.exception.MatchAlreadyStartedException
     * if match for particular teams has been starter already
     */
    void startNewScore(@Valid MatchDetails matchDetails);

    /**
     * Updates score for specific match
     *
     * @param matchScore contains home and away team names and scores {@link MatchScore}
     * @throws ConstraintViolationException if <CODE><I>matchScore</I></CODE> is invalid
     * @throws com.sportradar.core.scoreboard.exception.MatchNotFoundException
     * if match for particular teams doesn't exist
     */
    void updateScore(@Valid MatchScore matchScore);

    /**
     * Removes match from score board
     *
     * @param matchDetails contains home and away team names {@link MatchDetails}
     * @throws ConstraintViolationException if <CODE><I>matchDetails</I></CODE> is invalid
     * @throws com.sportradar.core.scoreboard.exception.MatchNotFoundException
     * if match for particular teams doesn't exist
     */
    void finishScore(@Valid MatchDetails matchDetails);

    /**
     * Returns list of matches ordered by score and creation time in descending order
     *
     * @return ordered list of {@link MatchScore match scores}
     */
    List<MatchScore> listScores();

    /**
     * Cleans up score board
     */
    void cleanUp();
}

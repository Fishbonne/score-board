package com.sportradar.core.scoreboard.service.impl;

import com.sportradar.core.scoreboard.dao.ScoreBoardDao;
import com.sportradar.core.scoreboard.domain.MatchDetails;
import com.sportradar.core.scoreboard.domain.MatchScore;
import com.sportradar.core.scoreboard.domain.TeamScore;
import com.sportradar.core.scoreboard.exception.MatchNotFoundException;
import com.sportradar.core.scoreboard.service.ScoreBoard;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
class InMemoryScoreBoardTest {
    @SpyBean
    private ScoreBoardDao scoreBoardDao;

    @Autowired
    private ScoreBoard scoreBoard;

    @BeforeEach
    void setUp() {
        scoreBoardDao.cleanUp();
        scoreBoard.startNewScore(new MatchDetails("USA", "Poland"));
        scoreBoard.startNewScore(new MatchDetails("Italy", "Spain"));
    }

    @Test
    void startNewScore() {
        // given
        MatchDetails matchDetailsRequest = new MatchDetails("Germany", "France");
        // when
        scoreBoard.startNewScore(matchDetailsRequest);
        // then
        verify(scoreBoardDao).startNewScore(matchDetailsRequest);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void startNewScore_invalidTeamName_exceptionThrown(String teamName) {
        // given
        MatchDetails matchDetailsRequest = new MatchDetails("Germany", teamName);
        // when
        assertThatThrownBy(() -> scoreBoard.startNewScore(matchDetailsRequest))
                .isInstanceOf(ConstraintViolationException.class);
        // then
        verify(scoreBoardDao, never()).startNewScore(matchDetailsRequest);
    }

    @Test
    void updateScore() {
        // when
        MatchScore matchScore = new MatchScore(new TeamScore("USA", 1), new TeamScore("Poland", 1), null);
        scoreBoard.updateScore(matchScore);
        // then
        verify(scoreBoardDao).updateScore(matchScore);
    }

    @Test
    void updateScore_scoreDoesNotExist_exceptionThrown() {
        // given
        MatchScore matchScore = new MatchScore(new TeamScore("USA", 1), new TeamScore("Spain", 1), null);
        // when
        assertThatThrownBy(() -> scoreBoard.updateScore(matchScore))
                .isInstanceOf(MatchNotFoundException.class);
        // then
        verify(scoreBoardDao).updateScore(matchScore);
    }


    @ParameterizedTest
    @NullAndEmptySource
    void updateScore_invalidTeamName_exceptionThrown(String teamName) {
        // given
        MatchScore matchScore = new MatchScore(new TeamScore(teamName, 1), new TeamScore("Poland", 1), null);
        // when
        assertThatThrownBy(() -> scoreBoard.updateScore(matchScore))
                .isInstanceOf(ConstraintViolationException.class);
        // then
        verify(scoreBoardDao, never()).updateScore(matchScore);
    }

    @Test
    void updateScore_invalidScore_exceptionThrown() {
        // given
        MatchScore matchScore = new MatchScore(new TeamScore("USA", -1), new TeamScore("Poland", 1), null);
        // when
        assertThatThrownBy(() -> scoreBoard.updateScore(matchScore))
                .isInstanceOf(ConstraintViolationException.class);
        // then
        verify(scoreBoardDao, never()).updateScore(matchScore);
    }


    @Test
    void finishScore() {
        // given
        MatchDetails matchDetails = new MatchDetails("USA", "Poland");
        // when
        scoreBoard.finishScore(matchDetails);
        // then
        verify(scoreBoardDao).finishScore(matchDetails);
    }

    @Test
    void finishScore_scoreDoesNotExist_exceptionThrown() {
        // given
        MatchDetails matchDetails = new MatchDetails("USA", "Italy");
        // when
        assertThatThrownBy(() -> scoreBoard.finishScore(matchDetails))
                .isInstanceOf(MatchNotFoundException.class);
        // then
        verify(scoreBoardDao).finishScore(matchDetails);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void finishScore_invalidTeamName_exceptionThrown(String teamName) {
        // given
        MatchDetails matchDetails = new MatchDetails(teamName, "Poland");
        // when
        assertThatThrownBy(() -> scoreBoard.finishScore(matchDetails))
                .isInstanceOf(ConstraintViolationException.class);
        // then
        verify(scoreBoardDao, never()).finishScore(matchDetails);
    }

    @Test
    void listScores() {
        // when
        List<MatchScore> actualScores = scoreBoard.listScores();
        // then
        verify(scoreBoardDao).listScores();
        assertThat(actualScores).hasSize(2);
    }
}

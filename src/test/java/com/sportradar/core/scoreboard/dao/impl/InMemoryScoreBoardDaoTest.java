package com.sportradar.core.scoreboard.dao.impl;

import com.sportradar.core.scoreboard.dao.ScoreBoardDao;
import com.sportradar.core.scoreboard.domain.MatchDetails;
import com.sportradar.core.scoreboard.domain.MatchScore;
import com.sportradar.core.scoreboard.domain.TeamScore;
import com.sportradar.core.scoreboard.exception.MatchAlreadyStartedException;
import com.sportradar.core.scoreboard.exception.MatchNotFoundException;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMemoryScoreBoardDaoTest {

    private ScoreBoardDao scoreBoardDaoToTest = new InMemoryScoreBoardDao();

    @Test
    void startNewScore() {
        // given
        scoreBoardDaoToTest = new InMemoryScoreBoardDao();
        Map<MatchDetails, MatchScore> scores = ((InMemoryScoreBoardDao) scoreBoardDaoToTest).getContent();
        assertThat(scores).isEmpty();
        // when
        scoreBoardDaoToTest.startNewScore(new MatchDetails("USA", "Poland"));
        // then
        assertThat(scores)
                .hasSize(1)
                .containsKey(new MatchDetails("USA", "Poland"))
                .hasValueSatisfying(new Condition<>("score is 0 - 0") {
                    public boolean matches(MatchScore matchScore) {
                        return matchScore.homeScore().teamName().equals("USA")
                                && matchScore.homeScore().score() == 0
                                && matchScore.awayScore().teamName().equals("Poland")
                                && matchScore.awayScore().score() == 0;
                    }
                });
    }

    @Test
    void startNewScore_scoreAlreadyExists_exceptionThrown() {
        // given
        scoreBoardDaoToTest = new InMemoryScoreBoardDao(new HashMap<>() {{
            put(new MatchDetails("USA", "Poland"),
                    new MatchScore(new TeamScore("USA", 1), new TeamScore("Poland", 1), Instant.now().toEpochMilli()));
        }});
        Map<MatchDetails, MatchScore> scores = ((InMemoryScoreBoardDao) scoreBoardDaoToTest).getContent();
        assertThat(scores).hasSize(1);
        // when
        assertThatThrownBy(() -> scoreBoardDaoToTest.startNewScore(new MatchDetails("USA", "Poland")))
                .isInstanceOf(MatchAlreadyStartedException.class);
    }

    @Test
    void updateScore() {
        // given
        long matchStartTimestamp = Instant.now().toEpochMilli();
        scoreBoardDaoToTest = new InMemoryScoreBoardDao(new HashMap<>() {{
            put(new MatchDetails("USA", "Poland"),
                    new MatchScore(new TeamScore("USA", 1), new TeamScore("Poland", 1), matchStartTimestamp));
        }});
        Map<MatchDetails, MatchScore> scores = ((InMemoryScoreBoardDao) scoreBoardDaoToTest).getContent();
        assertThat(scores).hasSize(1);
        // when
        scoreBoardDaoToTest.updateScore(new MatchScore(new TeamScore("USA", 2), new TeamScore("Poland", 2), null));
        // then
        assertThat(scores).hasSize(1);
        MatchScore resultMatchScore = scores.entrySet().stream()
                .findFirst()
                .orElseThrow()
                .getValue();
        assertThat(resultMatchScore)
                .isEqualTo(new MatchScore(new TeamScore("USA", 2), new TeamScore("Poland", 2), matchStartTimestamp));
    }

    @Test
    void updateScore_notFoundScore_exceptionThrown() {
        // given
        long matchStartTimestamp = Instant.now().toEpochMilli();
        scoreBoardDaoToTest = new InMemoryScoreBoardDao(new HashMap<>() {{
            put(new MatchDetails("USA", "Poland"),
                    new MatchScore(new TeamScore("USA", 1), new TeamScore("Poland", 1), matchStartTimestamp));
        }});
        Map<MatchDetails, MatchScore> scores = ((InMemoryScoreBoardDao) scoreBoardDaoToTest).getContent();
        assertThat(scores).hasSize(1);
        // when
        assertThatThrownBy(() -> scoreBoardDaoToTest.updateScore(
                new MatchScore(new TeamScore("USA", 2), new TeamScore("Ukraine", 2), null)
        ))
                .isInstanceOf(MatchNotFoundException.class);
        MatchScore resultMatchScore = scores.entrySet().stream()
                .findFirst()
                .orElseThrow()
                .getValue();
        assertThat(resultMatchScore)
                .isEqualTo(new MatchScore(new TeamScore("USA", 1), new TeamScore("Poland", 1), matchStartTimestamp));
    }

    @Test
    void finishScore() {
        // given
        scoreBoardDaoToTest = new InMemoryScoreBoardDao(new HashMap<>() {{
            put(new MatchDetails("USA", "Poland"),
                    new MatchScore(new TeamScore("USA", 1), new TeamScore("Poland", 1), Instant.now().toEpochMilli()));
        }});
        Map<MatchDetails, MatchScore> scores = ((InMemoryScoreBoardDao) scoreBoardDaoToTest).getContent();
        assertThat(scores).hasSize(1);
        // when
        scoreBoardDaoToTest.finishScore(new MatchDetails("USA", "Poland"));
        // then
        assertThat(scores).isEmpty();
    }

    @Test
    void finishScore_notFoundScore_exceptionThrown() {
        // given
        scoreBoardDaoToTest = new InMemoryScoreBoardDao(new HashMap<>() {{
            put(new MatchDetails("USA", "Poland"),
                    new MatchScore(new TeamScore("USA", 1), new TeamScore("Poland", 1), Instant.now().toEpochMilli()));
        }});
        Map<MatchDetails, MatchScore> scores = ((InMemoryScoreBoardDao) scoreBoardDaoToTest).getContent();
        assertThat(scores).hasSize(1);
        // when
        assertThatThrownBy(() -> scoreBoardDaoToTest.finishScore(new MatchDetails("USA", "Ukraine")))
                .isInstanceOf(MatchNotFoundException.class);
        // then
        assertThat(scores).hasSize(1);
    }

    @Test
    void listScores() {
        // given
        scoreBoardDaoToTest = new InMemoryScoreBoardDao(new HashMap<>() {{
            put(new MatchDetails("Mexico", "Canada"),
                    new MatchScore(new TeamScore("Mexico", 2), new TeamScore("Canada", 5), Instant.now().toEpochMilli()));
            put(new MatchDetails("Spain", "Brazil"),
                    new MatchScore(new TeamScore("Spain", 10), new TeamScore("Brazil", 2), Instant.now().toEpochMilli()));
            put(new MatchDetails("Germany", "France"),
                    new MatchScore(new TeamScore("Germany", 2), new TeamScore("France", 5), Instant.now().toEpochMilli() - 1_000));
        }});
        // when
        List<MatchScore> scores = scoreBoardDaoToTest.listScores();
        // then
        assertThat(scores)
                .hasSize(3);
        assertThat(scores.get(0))
                .satisfies(matchScore -> checkScore(matchScore, "Spain", 10, "Brazil", 2));
        assertThat(scores.get(1))
                .satisfies(matchScore -> checkScore(matchScore, "Mexico", 2, "Canada", 5));
        assertThat(scores.get(2))
                .satisfies(matchScore -> checkScore(matchScore, "Germany", 2, "France", 5));
    }

    private void checkScore(MatchScore matchScore,
                            String homeTeamName,
                            int homeTeamScore,
                            String awayTeamName,
                            int awayTeamScore) {
        assertThat(matchScore.homeScore().teamName())
                .isEqualTo(homeTeamName);
        assertThat(matchScore.homeScore().score())
                .isEqualTo(homeTeamScore);
        assertThat(matchScore.awayScore().teamName())
                .isEqualTo(awayTeamName);
        assertThat(matchScore.awayScore().score())
                .isEqualTo(awayTeamScore);
    }
}

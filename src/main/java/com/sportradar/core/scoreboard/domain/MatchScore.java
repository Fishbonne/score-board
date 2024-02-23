package com.sportradar.core.scoreboard.domain;

public record MatchScore(TeamScore homeScore, TeamScore awayScore, Long startTime) {

    public int totalScore() {
        return homeScore().score() + awayScore().score();
    }
}

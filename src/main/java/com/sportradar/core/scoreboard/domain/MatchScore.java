package com.sportradar.core.scoreboard.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record MatchScore(@NotNull @Valid TeamScore homeScore, @NotNull @Valid TeamScore awayScore, Long startTime) {

    public int totalScore() {
        return homeScore().score() + awayScore().score();
    }
}

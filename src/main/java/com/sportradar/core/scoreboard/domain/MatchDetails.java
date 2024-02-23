package com.sportradar.core.scoreboard.domain;

import jakarta.validation.constraints.NotBlank;

public record MatchDetails(@NotBlank String homeTeamName, @NotBlank String awayTeamName) {
    public static MatchDetails fromMatchScore(MatchScore matchScore) {
        return new MatchDetails(matchScore.homeScore().teamName(), matchScore.awayScore().teamName());
    }
}

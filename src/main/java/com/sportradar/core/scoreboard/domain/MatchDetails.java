package com.sportradar.core.scoreboard.domain;

public record MatchDetails(String homeTeamName, String awayTeamName) {
    public static MatchDetails fromMatchScore(MatchScore matchScore) {
        return new MatchDetails(matchScore.homeScore().teamName(), matchScore.awayScore().teamName());
    }
}

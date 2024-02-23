package com.sportradar.core.scoreboard.exception;

public class MatchAlreadyStartedException extends RuntimeException {
    public MatchAlreadyStartedException() {
    }

    public MatchAlreadyStartedException(String message) {
        super(message);
    }
}

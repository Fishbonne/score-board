package com.sportradar.core.scoreboard.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record TeamScore(@NotBlank String teamName, @PositiveOrZero int score) {
}

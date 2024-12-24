package com.alura.restapiforohubchallenge.domain.answer;

import jakarta.validation.constraints.NotNull;

public record AnswerReceivedDTO(
        @NotNull
        Long userId,

        @NotNull
        Long topicId,

        @NotNull
        String message
) {
}

package com.alura.restapiforohubchallenge.domain.answer;

import jakarta.validation.constraints.NotNull;

public record AnswerUpdateDTO(

        @NotNull
        String message
) {
}

package com.alura.restapiforohubchallenge.domain.topic;

import jakarta.validation.constraints.NotNull;

public record TopicReceivedDTO(
        String title,

        @NotNull
        Long idCourse,

        @NotNull
        Long idUser,

        String message
) {
}

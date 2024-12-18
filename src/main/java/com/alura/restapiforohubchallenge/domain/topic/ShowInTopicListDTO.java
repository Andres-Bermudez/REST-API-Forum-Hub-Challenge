package com.alura.restapiforohubchallenge.domain.topic;

public record ShowInTopicListDTO(
        Long idTopic,
        String title,
        String message
) {
}

package com.alura.restapiforohubchallenge.domain.topic;

import java.time.LocalDateTime;

public record TopicSavedDTO(

        Long idTopic,
        String user,
        String title,
        String courseName,
        String message,
        LocalDateTime createdDate,
        LocalDateTime lastEditedAt
) {
}

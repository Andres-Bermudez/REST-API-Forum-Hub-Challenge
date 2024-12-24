package com.alura.restapiforohubchallenge.domain.answer;

import java.time.LocalDateTime;

public record AnswerSavedDTO(
        Long idAnswer,
        String userName,
        String  topicTitle,
        String message,
        LocalDateTime creationDate,
        LocalDateTime lastEditedAt
) {
    public AnswerSavedDTO(Long idAnswer,
                          String userName,
                          String topicTitle,
                          String message,
                          LocalDateTime creationDate,
                          LocalDateTime lastEditedAt) {
        this.idAnswer = idAnswer;
        this.userName = userName;
        this.topicTitle = topicTitle;
        this.message = message;
        this.creationDate = creationDate;
        this.lastEditedAt = lastEditedAt;
    }
}

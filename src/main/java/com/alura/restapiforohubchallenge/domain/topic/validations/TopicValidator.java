package com.alura.restapiforohubchallenge.domain.topic.validations;

import com.alura.restapiforohubchallenge.domain.topic.TopicReceivedDTO;

public interface TopicValidator {
    void validate(TopicReceivedDTO topicReceivedDTO);
}

package com.alura.restapiforohubchallenge.domain.topic.validations.bussinesrules;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.alura.restapiforohubchallenge.domain.topic.TopicRepository;
import com.alura.restapiforohubchallenge.domain.topic.TopicReceivedDTO;
import com.alura.restapiforohubchallenge.exceptions.exceptions.ValidationException;
import com.alura.restapiforohubchallenge.domain.topic.validations.TopicValidator;

@Component
public class ValidateTitleAndMessage implements TopicValidator {

    @Autowired
    private TopicRepository topicRepository;

    @Override
    public void validate(TopicReceivedDTO topicReceivedDTO) {
        // Regla de negocio: No puede haber un topico con el titulo o el mensaje repetido.
        Boolean titleExist = topicRepository.existsByTitle(topicReceivedDTO.title());
        Boolean messageExist = topicRepository.existsByMessage(topicReceivedDTO.message());

        if (titleExist || messageExist) {
            throw new ValidationException("This topic already exists.");
        }
    }
}

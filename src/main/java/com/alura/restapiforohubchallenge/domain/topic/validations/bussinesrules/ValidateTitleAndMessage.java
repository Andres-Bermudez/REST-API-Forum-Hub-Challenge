package com.alura.restapiforohubchallenge.domain.topic.validations.bussinesrules;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.alura.restapiforohubchallenge.domain.topic.TopicRepository;
import com.alura.restapiforohubchallenge.domain.topic.TopicReceivedDTO;
import com.alura.restapiforohubchallenge.exceptions.ValidationException;
import com.alura.restapiforohubchallenge.domain.topic.validations.TopicValidator;

@Component
public class ValidateTitleAndMessage implements TopicValidator {

    @Autowired
    private TopicRepository topicRepository;

    @Override
    public void validate(TopicReceivedDTO topicReceivedDTO) {
        // 1ra regla de negocio: Ni el titulo ni el mensaje puede ser null.
        if (topicReceivedDTO.title() == null) {
            throw new ValidationException("The field 'title' is null.");
        }

        if (topicReceivedDTO.message() == null) {
            throw new ValidationException("The field 'message' is null.");
        }

        // 2da regla de negocio: No puede haber un topico con el titulo o el mensaje repetido.
        Boolean titleExist = topicRepository.existsByTitle(topicReceivedDTO.title());
        Boolean messageExist = topicRepository.existsByMessage(topicReceivedDTO.message());

        if (titleExist || messageExist) {
            throw new ValidationException("This topic already exists.");
        }
    }
}

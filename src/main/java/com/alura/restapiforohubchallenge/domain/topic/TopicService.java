package com.alura.restapiforohubchallenge.domain.topic;

import java.util.List;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.alura.restapiforohubchallenge.domain.user.UserEntity;
import com.alura.restapiforohubchallenge.domain.course.CourseEntity;
import com.alura.restapiforohubchallenge.domain.user.UserRepository;
import com.alura.restapiforohubchallenge.domain.course.CourseRepository;
import com.alura.restapiforohubchallenge.domain.topic.validations.TopicValidator;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private List<TopicValidator> validators;

    // Para crear un nuevo topico en la base de datos.
    public TopicEntity createNewTopic(TopicReceivedDTO topicReceivedDTO) {
        // Para validar las reglas de negocio.
        validators.forEach(v -> v.validate(topicReceivedDTO));

        CourseEntity courseEntity = courseRepository.getReferenceById(topicReceivedDTO.idCourse());
        UserEntity userEntity = userRepository.getReferenceById(topicReceivedDTO.idUser());
        var creationDate = LocalDateTime.now();

        TopicEntity topicEntity = new TopicEntity(
                null,
                topicReceivedDTO.title(),
                courseEntity,
                userEntity,
                topicReceivedDTO.message(),
                creationDate
        );
        return topicRepository.save(topicEntity);
    }

    // Para buscar un topico por su id.
    public TopicEntity getTopicById(Long idTopic) {
        return topicRepository.getReferenceById(idTopic);
    }
}

package com.alura.restapiforohubchallenge.domain.topic;

import java.util.List;
import java.time.LocalDateTime;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import com.alura.restapiforohubchallenge.domain.user.UserEntity;
import com.alura.restapiforohubchallenge.domain.course.CourseEntity;
import com.alura.restapiforohubchallenge.domain.user.UserRepository;
import com.alura.restapiforohubchallenge.domain.course.CourseRepository;
import com.alura.restapiforohubchallenge.exceptions.ValidationException;
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

        /*
         * PENDIENTE AGREGAR ESTA REGLA DE NEGOCIO A LOS VALIDADORES.
         *
         * OJO. ESTE PUNTO ESTA PENDIENTE POR MEJORAR.
        */

        // Regla de negocio: Ni el titulo ni el mensaje puede ser null.
        if (topicReceivedDTO.title() == null) {
            throw new ValidationException("The field 'title' is null.");
        }

        if (topicReceivedDTO.idCourse() == null) {
            throw new ValidationException("The field 'idCourse' is null.");
        }

        if (topicReceivedDTO.idUser() == null) {
            throw new ValidationException("The field 'idUser' is null.");
        }

        if (topicReceivedDTO.message() == null) {
            throw new ValidationException("The field 'message' is null.");
        }

        // Para validar las reglas de negocio.
        validators.forEach(v -> v.validate(topicReceivedDTO));

        if (!courseRepository.existsById(topicReceivedDTO.idCourse())) {
            throw new ValidationException("This course does not exist.");
        }

        if (!userRepository.existsById(topicReceivedDTO.idUser())) {
            throw new ValidationException("This user does not exist.");
        }

        CourseEntity courseEntity = courseRepository.getReferenceById(topicReceivedDTO.idCourse());
        UserEntity userEntity = userRepository.getReferenceById(topicReceivedDTO.idUser());

        LocalDateTime creationDate = LocalDateTime.now();
        LocalDateTime lastEditedAt = LocalDateTime.now();

        TopicEntity topicEntity = new TopicEntity(
                null,
                topicReceivedDTO.title(),
                courseEntity,
                userEntity,
                topicReceivedDTO.message(),
                creationDate,
                lastEditedAt,
                true
        );
        return topicRepository.save(topicEntity);
    }

    // Para buscar un topico por su id.
    public TopicEntity findTopicById(Long idTopic) {
        return topicRepository.getReferenceById(idTopic);
    }

    // Para listar todos los topicos activos.
    public Page<ShowInTopicListDTO> getTopicsList(Pageable pageable) {
        return topicRepository.findByActiveStatusTrue(pageable)
                .map(topicEntity -> new ShowInTopicListDTO(
                        topicEntity.getIdTopic(),
                        topicEntity.getTitle(),
                        topicEntity.getMessage()
                ));
    }

    @Transactional
    public TopicDetailsDTO updateTopic(Long idTopic, TopicReceivedDTO topicReceivedDTO) {
        // Para validar que el topico que se quiere actualizar este activo.
        if (!topicRepository.existsById(idTopic)) {
            throw new ValidationException("This topic does not exist.");
        }

        // Para validar que el topico que se quiere actualizar este activo.
        if (!topicRepository.getReferenceById(idTopic).getActiveStatus()) {
            throw new ValidationException("This topic is not available.");
        }

        // Para validar las reglas de negocio.
        validators.forEach(v -> v.validate(topicReceivedDTO));

        // Para obtener las entidades que debemos usar de la base de datos.
        TopicEntity topicEntity = topicRepository.getReferenceById(idTopic);

        CourseEntity courseEntity =
                courseRepository.getReferenceById(topicEntity.getCourseEntity().getIdCourse());

        UserEntity userEntity =
                userRepository.getReferenceById(topicEntity.getUserEntity().getIdUser());

        // Para actualizar los datos de un topico.
        updateTopicEntity(topicReceivedDTO, topicEntity);

        // Para mostrar al usuario el topico actualizado.
        return new TopicDetailsDTO(
                topicEntity.getIdTopic(),
                userEntity.getUserName(),
                topicEntity.getTitle(),
                courseEntity.getName(),
                topicEntity.getMessage(),
                topicEntity.getCreatedDate(),
                topicEntity.getLastEditedAt()
        );
    }

    // Para actualizar el registro de un topico.
    private void updateTopicEntity(TopicReceivedDTO topicReceivedDTO, TopicEntity topicEntity) {
        if (topicReceivedDTO.title() != null) {
            topicEntity.setTitle(topicReceivedDTO.title());
        }

        if (topicReceivedDTO.idCourse() != null) {
            CourseEntity courseEntity = courseRepository.getReferenceById(topicReceivedDTO.idCourse());
            topicEntity.setCourseEntity(courseEntity);
        }

        if (topicReceivedDTO.idUser() != null) {
            UserEntity userEntity = userRepository.getReferenceById(topicReceivedDTO.idUser());
            topicEntity.setUserEntity(userEntity);
        }

        if (topicReceivedDTO.message() != null) {
            topicEntity.setMessage(topicReceivedDTO.message());
        }

        topicEntity.setLastEditedAt(LocalDateTime.now());
    }
}

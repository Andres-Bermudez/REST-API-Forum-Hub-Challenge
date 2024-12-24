package com.alura.restapiforohubchallenge.domain.topic;

// Librerias utilizadas.
import java.util.Map;
import java.util.List;
import java.time.LocalDateTime;
import java.util.function.Supplier;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import com.alura.restapiforohubchallenge.domain.course.CourseEntity;
import com.alura.restapiforohubchallenge.domain.login.user.UserEntity;
import com.alura.restapiforohubchallenge.domain.course.CourseRepository;
import com.alura.restapiforohubchallenge.domain.login.user.UserRepository;
import com.alura.restapiforohubchallenge.domain.topic.validations.TopicValidator;
import com.alura.restapiforohubchallenge.exceptions.exceptions.ValidationException;

// Servicio de la API que contiene las reglas de negocio.
@Service
public class TopicService {

    // Inyeccion de dependencias.
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

        // Para validar que ningun campo del DTO recibido sea null.
        Map<Supplier<Object>, String> validations = Map.of(
                topicReceivedDTO::title, "The field 'title' is null.",
                topicReceivedDTO::idCourse, "The field 'idCourse' is null.",
                topicReceivedDTO::idUser, "The field 'idUser' is null.",
                topicReceivedDTO::message, "The field 'message' is null."
        );

        // Ejecutar las validaciones.
        validations.forEach((supplier, errorMessage) -> {
            if (supplier.get() == null) {
                throw new ValidationException(errorMessage);
            }
        });

        // Para validar las reglas de negocio.
        validators.forEach(v -> v.validate(topicReceivedDTO));

        // Para verificar si existe el curso  al que se le crea un nuevo topico.
        if (!courseRepository.existsById(topicReceivedDTO.idCourse())) {
            throw new ValidationException("This course does not exist.");
        }

        // Para verificar si existe el usuario que crea el topico.
        if (!userRepository.existsById(topicReceivedDTO.idUser())) {
            throw new ValidationException("This user does not exist.");
        }

        // Asignar las fechas de creacion y modificacion.
        LocalDateTime creationDate = LocalDateTime.now();
        LocalDateTime lastEditedAt = LocalDateTime.now();

        // Topico que sera creado.
        TopicEntity topicEntity = new TopicEntity(
                null,
                topicReceivedDTO.title(),
                courseRepository.getReferenceById(topicReceivedDTO.idCourse()),
                userRepository.getReferenceById(topicReceivedDTO.idUser()),
                topicReceivedDTO.message(),
                creationDate,
                lastEditedAt,
                true
        );
        return topicRepository.save(topicEntity); // Guardar el topico creado en la base de datos.
    }

    // Para buscar un topico por su id.
    public TopicEntity findTopicById(Long idTopic) {
        if (!topicRepository.existsById(idTopic)) {
            throw new ValidationException("This topic does not exists.");
        }
        if (!topicRepository.getReferenceById(idTopic).getActiveStatus()) {
            throw new ValidationException("This topic is not available.");
        }
        return topicRepository.getReferenceById(idTopic);
    }

    // Para listar todos los topicos activos.
    public Page<ShowInTopicListDTO> getAllTopics(Pageable pageable) {
        return topicRepository.findByActiveStatusTrue(pageable)
                .map(topicEntity -> new ShowInTopicListDTO(
                        topicEntity.getIdTopic(),
                        topicEntity.getTitle(),
                        topicEntity.getMessage()
                ));
    }

    // Para actualizar un topico.
    @Transactional
    public TopicSavedDTO updateTopic(Long idTopic, TopicReceivedDTO topicReceivedDTO) {
        // Para validar que el topico que se quiere actualizar si existe.
        if (!topicRepository.existsById(idTopic)) {
            throw new ValidationException("This topic does not exist.");
        }
        // Para validar que el topico que se quiere actualizar este activo.
        if (!topicRepository.getReferenceById(idTopic).getActiveStatus()) {
            throw new ValidationException("This topic is not available.");
        }

        // Para validar las reglas de negocio.
        validators.forEach(v -> v.validate(topicReceivedDTO));

        // Para obtener el topico que se va a actualizar.
        TopicEntity topicEntity = topicRepository.getReferenceById(idTopic);

        // Para actualizar los datos de un topico.
        TopicEntity updatedTopic = updateTopicEntity(topicReceivedDTO, topicEntity);

        // Para mostrar al usuario el topico actualizado.
        return new TopicSavedDTO(
                updatedTopic.getIdTopic(),
                userRepository.getReferenceById(topicEntity.getUserEntity().getIdUser()).getUsername(),
                updatedTopic.getTitle(),
                courseRepository.getReferenceById(topicEntity.getCourseEntity().getIdCourse()).getName(),
                updatedTopic.getMessage(),
                updatedTopic.getCreatedDate(),
                updatedTopic.getLastEditedAt()
        );
    }

    // Metodo para actualizar el registro de un topico.
    private TopicEntity updateTopicEntity(TopicReceivedDTO topicReceivedDTO, TopicEntity topicEntity) {
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

        return topicEntity;
    }

    // Para desactivar un topico. Eliminacion logica.
    @Transactional
    public void deleteTopic(Long idTopic) {
        if (!topicRepository.existsById(idTopic)) {
            throw new ValidationException("This topic does not exists.");
        }
        // Para modificar la columna active_status del topico que se quiere "eliminar".
        // Realizamos una eliminacion logica, para almacenar el registro para control de datos.
        topicRepository.getReferenceById(idTopic).setActiveStatus(false);
    }
}

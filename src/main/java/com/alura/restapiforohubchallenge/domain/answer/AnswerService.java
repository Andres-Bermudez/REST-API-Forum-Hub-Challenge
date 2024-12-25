package com.alura.restapiforohubchallenge.domain.answer;

import java.util.List;
import java.time.LocalDateTime;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.alura.restapiforohubchallenge.domain.topic.TopicRepository;
import com.alura.restapiforohubchallenge.domain.login.user.UserRepository;
import com.alura.restapiforohubchallenge.exceptions.exceptions.ValidationException;

@Service
public class AnswerService {

    // Inyeccion de dependencias.
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    // Para crear una nueva respuesta.
    public AnswerEntity createNewAnswer(AnswerReceivedDTO answerReceivedDTO) {
        // Para validar si los ids recibidos existen.
        if (!userRepository.existsById(answerReceivedDTO.userId())) {
            throw new ValidationException("This user does not exists.");
        }
        if (!topicRepository.existsById(answerReceivedDTO.topicId())) {
            throw new ValidationException("This topic does not exists.");
        }

        // Nueva respuesta creada.
        AnswerEntity answerEntity = new AnswerEntity(
                null,
                userRepository.getReferenceById(answerReceivedDTO.userId()),
                topicRepository.getReferenceById(answerReceivedDTO.topicId()),
                answerReceivedDTO.message(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
        );
        return answerRepository.save(answerEntity); // Guardar la nueva respuesta.
    }

    // Retorna las respuestas que tiene un topico.
    public List<AnswerSavedDTO> getAnswersByTopicId(Long topicId) {
        if (topicRepository.existsById(topicId) && !topicRepository.getReferenceById(topicId).getActiveStatus()) {
            throw new ValidationException("This topic is not available.");
        }
        List<AnswerEntity> answers = answerRepository.getAnswersByIdTopic(topicId);
        return answers.stream()
            .map(answer -> new AnswerSavedDTO(
                    answer.getIdAnswer(),

                    // Estas lineas generan 2 subconsultas SQL.
                    userRepository.getReferenceById(answer.getUserEntity().getIdUser()).getUsername(),
                    topicRepository.getReferenceById(answer.getTopicEntity().getIdTopic()).getTitle(),

                    answer.getMessage(),
                    answer.getCreationDate(),
                    answer.getLastEditedAt()
            ))
            .toList();
    }

    // Para actualizar una respuesta.
    @Transactional
    public AnswerSavedDTO updateAnswer(Long idAnswer, AnswerUpdateDTO answerUpdateDTO) {
        if (!answerRepository.existsById(idAnswer) ||
                !answerRepository.getReferenceById(idAnswer).getActiveStatus())
        {
            throw new ValidationException("This answer is not available");
        }

        AnswerEntity answerEntity = answerRepository.getReferenceById(idAnswer);

        // Para validar si el topico del que se quiere actualizar la respuesta esta activo
        if (!topicRepository.getReferenceById(answerEntity.getTopicEntity().getIdTopic()).getActiveStatus()) {
            throw new ValidationException("The topic for which you want to update a reply is not available.");
        }

        // Actualizar la respuesta.
        answerEntity.setMessage(answerUpdateDTO.message());
        answerEntity.setLastEditedAt(LocalDateTime.now());

        return new AnswerSavedDTO(
                answerEntity.getIdAnswer(),
                userRepository.getReferenceById(answerEntity.getUserEntity().getIdUser()).getUsername(),
                topicRepository.getReferenceById(answerEntity.getTopicEntity().getIdTopic()).getTitle(),
                answerEntity.getMessage(),
                answerEntity.getCreationDate(),
                answerEntity.getLastEditedAt()
        );
    }

    // Para eliminar una respuesta.
    @Transactional
    public void deleteAnswer(Long idAnswer) {
        if (!answerRepository.existsById(idAnswer)) {
            throw new ValidationException("This answer cannot be deleted because it does not exist.");
        }
        answerRepository.getReferenceById(idAnswer).setActiveStatus(false);
    }
}

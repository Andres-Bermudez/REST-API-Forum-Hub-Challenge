package com.alura.restapiforohubchallenge.domain.answer;

// Librerias utilizadas.
import java.net.URI;
import java.util.List;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.alura.restapiforohubchallenge.domain.topic.TopicService;
import com.alura.restapiforohubchallenge.domain.login.user.UserService;

// Endpoint.
@RestController
@RequestMapping("/answers")
public class AnswerController {

    // Inyeccion de dependencias.
    @Autowired
    private AnswerService answerService;

    @Autowired
    UserService userService;

    @Autowired
    TopicService topicService;

    // Endpoint para crear una nueva respuesta a un topico.
    @PostMapping("/create")
    public ResponseEntity<AnswerSavedDTO> createNewAnswer(
            // Cuerpo de la solicitud donde se recibe la nueva respuesta.
            @RequestBody
            AnswerReceivedDTO answerReceivedDTO,

            // Obejeto para generar la URL de la respuesta creada.
            UriComponentsBuilder uriComponentsBuilder
    ) {
        // Para crear la nueva respuesta.
        AnswerEntity answerEntity = answerService.createNewAnswer(answerReceivedDTO);

        // Para generar la URL donde se encuentra la respuesta creada.
        URI url = uriComponentsBuilder.path("/answers/{idAnswer}")
                .buildAndExpand(answerEntity.getIdAnswer())
                .toUri();

        // Nueva respuesta creada.
        AnswerSavedDTO answerSavedDTO = new AnswerSavedDTO(
                answerEntity.getIdAnswer(),
                userService.findById(answerReceivedDTO.userId()).getUsername(),
                topicService.findTopicById(answerReceivedDTO.topicId()).getTitle(),
                answerEntity.getMessage(),
                answerEntity.getCreationDate(),
                answerEntity.getLastEditedAt()
        );
        // Retorna un 201 Created con la url y la respuesta creada.
        return ResponseEntity.created(url).body(answerSavedDTO);
    }

    @GetMapping("/topic/{idTopic}")
    public ResponseEntity<List<AnswerSavedDTO>> getAnswersByTopicId(
            @PathVariable @NotNull Long idTopic
    ) {
        List<AnswerSavedDTO> answers = answerService.getAnswersByTopicId(idTopic);

        if (answers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(answers);
    }

    // Para actualizar una respuesta.
    @PutMapping("/update/{idAnswer}")
    public ResponseEntity<AnswerSavedDTO> updateTopic(
            // Se recibe  dentro de la URL el id de la respuesta a actualizar.
            @PathVariable @NotNull Long idAnswer,

            // Se reciben los datos a actualizar en el cuerpo de la solicitud.
            @RequestBody @NotNull AnswerUpdateDTO answerUpdateDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        // Retorna un 200 Ok y el topico actualizado en el cuerpo de la respuesta.
        return ResponseEntity.ok(answerService.updateAnswer(idAnswer, answerUpdateDTO));
    }

    // Eliminar un topico por su id.
    @DeleteMapping("/delete/{idAnswer}")
    public ResponseEntity deleteAnswer(@PathVariable @NotNull Long idAnswer) {
        answerService.deleteAnswer(idAnswer);
        return ResponseEntity.ok().build();
    }
}

package com.alura.restapiforohubchallenge.domain.topic;

import java.net.URI;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    // Para crear un nuevo topico.
    @PostMapping("/create")
    public ResponseEntity<TopicDetailsDTO> createTopic(
            @RequestBody @Valid TopicReceivedDTO topicReceivedDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        //Para crear el nuevo topico y retornar su entidad.
        TopicEntity topicEntity = topicService.createNewTopic(topicReceivedDTO);

        // Para generar la URL donde puede verse este topico que fue creado.
        URI url = uriComponentsBuilder.path("/topic/{idTopic}")
                                      .buildAndExpand(topicEntity.getId())
                                      .toUri();

        // Para retornar codigo 201 Created.
        // En el cuerpo de la respuesta se incluyen los detalles del nuevo topico creado.
        return ResponseEntity.created(url).body(
                new TopicDetailsDTO(
                    topicEntity.getId(),
                    topicEntity.getTitle(),
                    topicEntity.getMessage()
                )
        );
    }

    // Para buscar un topico por su id.
    @GetMapping("/{idTopic}")
    public ResponseEntity<TopicDetailsDTO> getTopicById(
            @PathVariable Long idTopic
    ) {
        TopicEntity topicEntity = topicService.getTopicById(idTopic);
        return ResponseEntity.ok().body(
                new TopicDetailsDTO(
                    topicEntity.getId(),
                    topicEntity.getTitle(),
                    topicEntity.getMessage()
                )
        );
    }
}

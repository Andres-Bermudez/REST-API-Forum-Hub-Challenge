package com.alura.restapiforohubchallenge.domain.topic;

import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.alura.restapiforohubchallenge.domain.login.user.UserEntity;
import com.alura.restapiforohubchallenge.domain.login.user.UserService;
import com.alura.restapiforohubchallenge.domain.course.CourseEntity;
import com.alura.restapiforohubchallenge.domain.course.CourseService;

@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    // Para crear un nuevo topico.
    @PostMapping("/create")
    public ResponseEntity<TopicDetailsDTO> createTopic(
            @RequestBody TopicReceivedDTO topicReceivedDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        //Para crear el nuevo topico.
        TopicEntity topicEntity = topicService.createNewTopic(topicReceivedDTO);

        // Para obtener la entidad del usuario que creo el topico.
        UserEntity userEntity = userService
                .findById(topicEntity.getUserEntity().getIdUser());

        CourseEntity courseEntity = courseService
                .findById(topicEntity.getCourseEntity().getIdCourse());

        // Para generar la URL donde puede verse este topico que fue creado.
        URI url = uriComponentsBuilder.path("/topic/{idTopic}")
                                      .buildAndExpand(topicEntity.getIdTopic())
                                      .toUri();

        // Para retornar codigo 201 Created.
        // En el cuerpo de la respuesta se incluyen los detalles del nuevo topico creado.
        return ResponseEntity.created(url).body(
                new TopicDetailsDTO(
                        topicEntity.getIdTopic(),
                        userEntity.getUsername(),
                        topicEntity.getTitle(),
                        courseEntity.getName(),
                        topicEntity.getMessage(),
                        topicEntity.getCreatedDate(),
                        topicEntity.getLastEditedAt()
                ));
    }

    // Para buscar un topico por su id.
    @GetMapping("/{idTopic}")
    public ResponseEntity<TopicDetailsDTO> getTopicById(
            @PathVariable Long idTopic
    ) {
        //Para crear el nuevo topico y retornar su entidad.
        TopicEntity topicEntity = topicService.findTopicById(idTopic);

        UserEntity userEntity = userService
                .findById(topicEntity.getUserEntity().getIdUser());

        CourseEntity courseEntity = courseService
                .findById(topicEntity.getCourseEntity().getIdCourse());

        return ResponseEntity.ok().body(
                new TopicDetailsDTO(
                        topicEntity.getIdTopic(),
                        userEntity.getUsername(),
                        topicEntity.getTitle(),
                        courseEntity.getName(),
                        topicEntity.getMessage(),
                        topicEntity.getCreatedDate(),
                        topicEntity.getLastEditedAt()
                ));
    }

    // Lista de todos los topicos con paginacion.
    // Este método permite obtener una lista paginada y ordenada.
    @GetMapping("/list")
    public ResponseEntity<Page<ShowInTopicListDTO>> getTopicList(
            @RequestParam(defaultValue = "0") int page, // Inicia en la pagina 0.
            @RequestParam(defaultValue = "10") int size, // Muestra 10 elementos por pagina.
            @RequestParam(defaultValue = "createdDate") String sortBy // Ordena por fecha de creacion.
    ) {
        // Organiza de a 10 elementos por pagina y por fecha de creacion en orden ascendente.
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return ResponseEntity.ok(topicService.getTopicsList(pageable));
    }

    // Para actualizar un topico.
    @PutMapping("/update/{idTopic}")
    public ResponseEntity<TopicDetailsDTO> updateTopic(
            @PathVariable Long idTopic,
            @RequestBody TopicReceivedDTO topicReceivedDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        return ResponseEntity.ok(topicService.updateTopic(idTopic, topicReceivedDTO));
    }

    // Eliminar un topico.
    @DeleteMapping("/delete/{idTopic}")
    public ResponseEntity deleteTopic(@PathVariable Long idTopic) {
        topicService.deleteTopic(idTopic);
        return ResponseEntity.ok().build();
    }
}

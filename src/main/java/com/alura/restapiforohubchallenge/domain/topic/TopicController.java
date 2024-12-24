package com.alura.restapiforohubchallenge.domain.topic;

//Librerias utilizadas.
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.alura.restapiforohubchallenge.domain.course.CourseEntity;
import com.alura.restapiforohubchallenge.domain.course.CourseService;
import com.alura.restapiforohubchallenge.domain.login.user.UserService;

// Endpoint.
@RestController
@RequestMapping("/topics")
public class TopicController {

    // Inyeccion de dependencias.
    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    // Endpoint para crear un nuevo topico.
    @PostMapping("/create")
    public ResponseEntity<TopicSavedDTO> createTopic(
            @RequestBody TopicReceivedDTO topicReceivedDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        //Para crear el nuevo topico.
        TopicEntity topicEntity = topicService.createNewTopic(topicReceivedDTO);

        // Para generar la URL donde puede verse este topico que fue creado.
        URI url = uriComponentsBuilder.path("/topics/{idTopic}")
                                      .buildAndExpand(topicEntity.getIdTopic())
                                      .toUri();

        // Para retornar codigo 201 Created con la URL donde se encuentra el topico.
        // En el cuerpo de la respuesta se incluyen los detalles del nuevo topico creado.
        return ResponseEntity.created(url).body(
                new TopicSavedDTO(
                        topicEntity.getIdTopic(),
                        userService.findById(topicEntity.getUserEntity()
                                                .getIdUser())
                                                .getUsername(),
                        topicEntity.getTitle(),
                        courseService.findById(topicEntity.getCourseEntity()
                                                .getIdCourse())
                                                .getName(),
                        topicEntity.getMessage(),
                        topicEntity.getCreatedDate(),
                        topicEntity.getLastEditedAt()
                )
        );
    }

    // Para buscar un topico por su id.
    @GetMapping("/{idTopic}")
    public ResponseEntity<TopicSavedDTO> getTopicById(
            // Se recibe el id del topico dentro de la URL.
            @PathVariable @NotNull Long idTopic
    ) {
        //Para obtener el topico.
        TopicEntity topicEntity = topicService.findTopicById(idTopic);

        // Para obtener el curso al que pertenece el topico.
        CourseEntity courseEntity = courseService
                .findById(topicEntity.getCourseEntity().getIdCourse());

        // Retorna codigo 200 Ok y en el cuerpo de la respuesta se envia el topico encontrado.
        return ResponseEntity.ok().body(
                new TopicSavedDTO(
                        topicEntity.getIdTopic(),
                        userService.findById(topicEntity.getUserEntity()
                                                        .getIdUser())
                                                        .getUsername(),
                        topicEntity.getTitle(),
                        courseService.findById(topicEntity.getCourseEntity()
                                                          .getIdCourse())
                                                          .getName(),
                        topicEntity.getMessage(),
                        topicEntity.getCreatedDate(),
                        topicEntity.getLastEditedAt()
                ));
    }

    // Lista de todos los topicos con paginacion.
    // Este método permite obtener una lista paginada y ordenada.
    @GetMapping("/all")
    public ResponseEntity<Page<ShowInTopicListDTO>> getTopicList(

            // Configuraciones por defecto de cada pagina.
            @RequestParam(defaultValue = "0") int page, // Inicia en la pagina 0.
            @RequestParam(defaultValue = "10") int size, // Muestra 10 elementos por pagina.
            @RequestParam(defaultValue = "createdDate") String sortBy // Ordena por fecha de creacion.
    ) {
        // Organiza de a 10 elementos por pagina y por fecha de creacion en orden ascendente.
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return ResponseEntity.ok(topicService.getAllTopics(pageable));
    }

    // Para actualizar un topico.
    @PutMapping("/update/{idTopic}")
    public ResponseEntity<TopicSavedDTO> updateTopic(
            // Se recibe el id del topico a actualizar dentro de la URL.
            @PathVariable @NotNull Long idTopic,

            // Se reciben los datos a actualizar en el cuerpo de la solicitud.
            @RequestBody @NotNull TopicReceivedDTO topicReceivedDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        // Retorna un 200 Ok y el topico actualizado en el cuerpo de la respuesta.
        return ResponseEntity.ok(topicService.updateTopic(idTopic, topicReceivedDTO));
    }

    // Eliminar un topico por su id.
    @DeleteMapping("/delete/{idTopic}")
    public ResponseEntity deleteTopic(@PathVariable @NotNull Long idTopic) {
        topicService.deleteTopic(idTopic);
        return ResponseEntity.ok().build();
    }
}

package com.alura.restapiforohubchallenge.domain.answer;

import java.util.List;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import com.alura.restapiforohubchallenge.domain.topic.TopicEntity;
import com.alura.restapiforohubchallenge.domain.course.CourseEntity;
import com.alura.restapiforohubchallenge.domain.topic.TopicRepository;
import com.alura.restapiforohubchallenge.domain.course.CourseCategory;
import com.alura.restapiforohubchallenge.domain.login.user.UserEntity;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.alura.restapiforohubchallenge.domain.course.CourseRepository;
import com.alura.restapiforohubchallenge.domain.login.user.UserRepository;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DataJpaTest // Para hacer test de las clases que necesiten de la capa de persistencia.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Para NO utilizar una base de datos en memoria ram.
@ActiveProfiles("test") // El perfil que utiliza los tests.
class AnswerRepositoryTest {
    // Inyeccion de los repositorios utilizados.
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private AnswerRepository answerRepository;


    @Test
    @DisplayName("Caso 1: Si un topico NO tiene respuestas creadas, retorna una lista vacia.")
    void getAnswersByIdTopicTest1() {
        // Esta lista debe ser null o vacia.
        List<AnswerEntity> answersByIdTopic = answerRepository.getAnswersByIdTopic(1L);

        // Verificar si la lista es null o vacia.
        assertThat(answersByIdTopic).isNullOrEmpty();
    }


    @Test
    @DisplayName("Caso 2: Si un topico SI tiene respuestas creadas y estan activas.")
    void getAnswersByIdTopicTest2() {
        // Arrange.
        UserEntity userEntity =
                new UserEntity(
                        null,
                        "userTest",
                        "userTest@email.com",
                        "123456");
        userRepository.save(userEntity);

        CourseEntity courseEntity =
                new CourseEntity(
                        null,
                        "courseTest",
                        CourseCategory.PROGRAMMING);
        courseRepository.save(courseEntity);



        TopicEntity topicEntity1 =
                new TopicEntity(null,
                        "topicTest1",
                        courseEntity,
                        userEntity,
                        "topic message test 1",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        true);
        topicRepository.save(topicEntity1);

        TopicEntity topicEntity2 =
                new TopicEntity(
                        null,
                        "topicTest2",
                        courseEntity,
                        userEntity,
                        "topic message test 2",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        true);
        topicRepository.save(topicEntity2);

        TopicEntity topicEntity3 =
                new TopicEntity(
                        null,
                        "topicTest3",
                        courseEntity,
                        userEntity,
                        "topic message test 3",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        true);
        topicRepository.save(topicEntity3);

        // Obtener los ids de los topicos creados.
        Long idTopic1 = topicEntity1.getIdTopic();
        Long idTopic2 = topicEntity2.getIdTopic();
        Long idTopic3 = topicEntity3.getIdTopic();



        // Crear 3 respuestas.
        AnswerEntity answerEntity1 =
                new AnswerEntity(
                        null,
                        userEntity,
                        topicEntity1,    // Esta respuesta es del topico 1.
                        "answer message test 1",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        true);
        answerRepository.save(answerEntity1);

        AnswerEntity answerEntity2 =
                new AnswerEntity(
                        null,
                        userEntity,
                        topicEntity1,    // Esta respuesta es del topico 1.
                        "answer message test 2",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        true);
        answerRepository.save(answerEntity2);

        AnswerEntity answerEntity3 =
                new AnswerEntity(
                        null,
                        userEntity,
                        topicEntity3,    // Esta respuesta es del topico 3.
                        "answer message test 3",
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        false);
        answerRepository.save(answerEntity3);

        // Act.
        List<AnswerEntity> answersByIdTopic1 = answerRepository.getAnswersByIdTopic(idTopic1);
        List<AnswerEntity> answersByIdTopic2 = answerRepository.getAnswersByIdTopic(idTopic2);
        List<AnswerEntity> answersByIdTopic3 = answerRepository.getAnswersByIdTopic(idTopic3);

        // Assert.
        // Para comparar el tamaño que se esperan que tengan las listas retornadas,
        // con el valor real que retorna el metodo testeado.
        assertEquals(2, answersByIdTopic1.size()); // Existen 2 respuestas del topico 1 y las 2 estan activas.
        assertEquals(0, answersByIdTopic2.size()); // No existen respuestas del topico 2.
        assertEquals(0, answersByIdTopic3.size()); // Existe 1 respuesta del topico 3, pero esta inactiva.

        // Para verificar que todos los objetos retornados en las listas esten activos.
        assertTrue(answersByIdTopic1.stream().allMatch(AnswerEntity::getActiveStatus));
        assertTrue(answersByIdTopic2.stream().allMatch(AnswerEntity::getActiveStatus));
        assertTrue(answersByIdTopic3.stream().allMatch(AnswerEntity::getActiveStatus));
    }
}

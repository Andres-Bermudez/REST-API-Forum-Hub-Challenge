// package com.alura.restapiforohubchallenge.domain.topic;

/*
 * Este es un test unitario de un controlador.
 *
 * NO he podido ejecutarlo porque se supone que un test unitario de un controlador no debe de
 * necesitar de la capa de persistencia para pasar los tests, pero me da un SQL error,
 * entonces voy a leer documentacion y a investigar un poco mas sobre los tests en Java para
 * darle solucion en otro momento, porque los tests no estan dentro de los requerimientos del
 * proyecto y debo enviarlo dentro de la fecha establecida.
*/

//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.DisplayName;
//import org.springframework.http.MediaType;
//import org.springframework.http.HttpStatus;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.assertj.core.api.Assertions.assertThat;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//
//@WebMvcTest // Para levantar el contexto de spring necesario para testear controladores web.
//@AutoConfigureMockMvc // Para autoconfigurar una instancia de MockMvc.
//@WithMockUser // Para simular que ya se realizo autenticacion y autorizacion.
//class TopicControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    @DisplayName("400 Bad Request: Si el usuario ingresa datos incompletos o incorrectos.")
//    void createTopicTest() throws Exception {
//        // Arrange.
//        String incompleteRequestBody = "{}";
//
//        // Act.
//        var response = mockMvc.perform(post("/topics/create")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(incompleteRequestBody))
//                .andReturn()
//                .getResponse();
//
//        // Assert.
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//    }
//}
package com.alura.restapiforohubchallenge.domain.answer;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.alura.restapiforohubchallenge.domain.topic.TopicEntity;
import com.alura.restapiforohubchallenge.domain.login.user.UserEntity;

@Entity
@Table(name = "answers")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class AnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_answer")
    private Long idAnswer;

    // Foreign Key
    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity userEntity;

    // Foreign Key
    @ManyToOne
    @JoinColumn(name = "id_topic")
    private TopicEntity topicEntity;

    private String message;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "last_edited_at")
    private LocalDateTime lastEditedAt;

    @Column(name = "active_status")
    private Boolean activeStatus;
}

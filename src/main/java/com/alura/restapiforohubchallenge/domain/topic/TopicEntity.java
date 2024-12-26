package com.alura.restapiforohubchallenge.domain.topic;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.alura.restapiforohubchallenge.domain.course.CourseEntity;
import com.alura.restapiforohubchallenge.domain.login.user.UserEntity;

@Entity
@Table(name = "topics")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class TopicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_topic")
    private Long idTopic;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_course")
    private CourseEntity courseEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private UserEntity userEntity;

    private String message;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_edited_at")
    private LocalDateTime lastEditedAt;

    @Column(name = "active_status")
    private Boolean activeStatus;
}

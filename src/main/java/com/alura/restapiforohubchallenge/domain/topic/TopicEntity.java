package com.alura.restapiforohubchallenge.domain.topic;

import lombok.Getter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.alura.restapiforohubchallenge.domain.user.UserEntity;
import com.alura.restapiforohubchallenge.domain.course.CourseEntity;

@Entity
@Table(name = "topics")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class TopicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}

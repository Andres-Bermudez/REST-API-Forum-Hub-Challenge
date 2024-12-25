package com.alura.restapiforohubchallenge.domain.course;

import lombok.Getter;
import java.util.List;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.alura.restapiforohubchallenge.domain.topic.TopicEntity;

@Entity
@Table(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_course")
    private Long idCourse;

    private String name;

    @Enumerated(EnumType.STRING)
    private CourseCategory category;

    @OneToMany(mappedBy = "courseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TopicEntity> topics;


    public CourseEntity(Long idCourse, String name, CourseCategory category) {
        this.idCourse = idCourse;
        this.name = name;
        this.category = category;
    }
}

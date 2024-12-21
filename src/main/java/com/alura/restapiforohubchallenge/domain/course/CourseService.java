package com.alura.restapiforohubchallenge.domain.course;

import com.alura.restapiforohubchallenge.exceptions.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public CourseEntity findById(Long idCourse) {

        if (!courseRepository.existsById(idCourse)) {
            throw new ValidationException("This course does not exist.");
        }
        return courseRepository.getReferenceById(idCourse);
    }
}

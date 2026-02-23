package dtos;

import java.util.Set;
import java.util.stream.Collectors;

import entities.Course;
import entities.Lesson;

public record LessonUpdateDTO(Long id, String name) {

    public Lesson toEntity(Course course) {
        return new Lesson(id, name, course);
    }

    public static Set<Lesson> toEntities(Set<LessonUpdateDTO> lessonDtos, Course course) {
        return lessonDtos.stream()
                .map(lessonDto -> lessonDto.toEntity(course))
                .collect(Collectors.toSet());
    }

}

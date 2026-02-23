package dtos;

import java.util.Set;
import java.util.stream.Collectors;

import entities.Lesson;

public record LessonReadDTO(Long id, String name) {

    public static LessonReadDTO fromEntity(Lesson lesson) {
        return new LessonReadDTO(lesson.getId(), lesson.getName());
    }

    public static Set<LessonReadDTO> fromEntities(Set<Lesson> lessons) {
        return lessons.stream()
                .map(LessonReadDTO::fromEntity)
                .collect(Collectors.toSet());
    }

}

package dtos;

import entities.Lesson;

public record LessonCreateDTO(String name) {

    public Lesson toEntity() {
        return new entities.Lesson(name);
    }
    
}

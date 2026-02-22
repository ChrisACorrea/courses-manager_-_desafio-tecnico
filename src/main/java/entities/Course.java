package entities;

import java.util.Set;

public class Course {

    public Course(String name, Set<Lesson> lessons) {
        this(null, name, lessons);
    }

    public Course(Long id, String name, Set<Lesson> lessons) {
        this.id = id;
        this.name = name;
        this.lessons = lessons;
    }

    private Long id;
    private String name;
    private Set<Lesson> lessons;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }
}

package entities;

public class Lesson {

    public Lesson(String name) {
        this(null, name);
    }

    public Lesson(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private Long id;
    private String name;
    private Course course;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Course getCourse() {
        return course;
    }
}

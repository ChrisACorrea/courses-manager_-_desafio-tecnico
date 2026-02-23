package services;

import java.util.Set;
import java.util.stream.Collectors;

import entities.Course;
import entities.Lesson;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import repositories.CourseRepository;

@ApplicationScoped
public class CourseService {

    private final CourseRepository courseRepository;

    @Inject
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Set<Course> getAllCourses() {
        return courseRepository.listAll()
                .stream()
                .collect(Collectors.toSet());
    }

    public Course getCourseById(Long id) {
        return courseRepository.findByIdOptional(id)
                .orElseThrow();
    }

    public Course createCourse(Course course) {
        courseRepository.persist(course);
        return course;
    }
    
    public Course updateCourse(Course course) {
        courseRepository.findByIdOptional(course.getId())
            .orElseThrow();

        courseRepository.getEntityManager().merge(course);
        return course;
    }

    public boolean deleteCourseById(Long id) {
        return courseRepository.deleteById(id);
    }

    public Set<Lesson> getLessonsByCourseId(Long courseId) {
        Course course = courseRepository.findByIdOptional(courseId)
                .orElseThrow();
        return course.getLessons();
    }

    public void addLessonToCourse(Long courseId, Lesson lesson) {
        Course course = courseRepository.findByIdOptional(courseId)
                .orElseThrow();
        course.addLesson(lesson);
        courseRepository.getEntityManager().merge(course);
    }

}

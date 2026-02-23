package resources;

import dtos.CourseCreateDTO;
import dtos.CourseUpdateDTO;
import dtos.LessonCreateDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import services.CourseService;

@Path("/courses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class CourseResource {

    private final CourseService courseService;

    @Inject
    public CourseResource(CourseService courseService) {
        this.courseService = courseService;
    }

    @GET
    public Response getAllCourses() {
        var courses = courseService.getAllCourses();

        return Response.ok(courses).build();
    }

    @GET
    @Path("/{id}")
    public Response getCourseById(Long id) {
        var course = courseService.getCourseById(id);

        return Response.ok(course).build();
    }

    @POST
    public Response createCourse(CourseCreateDTO courseCreateDTO) {
        var course = courseCreateDTO.toEntity();
        courseService.createCourse(course);

        return Response.ok(course).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCourse(Long id, CourseUpdateDTO courseUpdateDTO) {
        if (!id.equals(courseUpdateDTO.id())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        var course = courseUpdateDTO.toEntity();
        courseService.updateCourse(course);
        return Response.ok(course).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCourse(Long id) {
        courseService.deleteCourseById(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{courseId}/lessons")
    public Response getLessonsByCourseId(Long courseId) {
        var lessons = courseService.getLessonsByCourseId(courseId);
        return Response.ok(lessons).build();
    }

    @POST
    @Path("/{courseId}/lessons")
    public Response addLessonToCourse(Long courseId, LessonCreateDTO lessonCreateDTO) {
        var lesson = lessonCreateDTO.toEntity();
        courseService.addLessonToCourse(courseId, lesson);
        return Response.ok(lesson).build();
    }
}

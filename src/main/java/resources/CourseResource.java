package resources;

import java.net.URI;

import dtos.CourseCreateDTO;
import dtos.CourseReadDTO;
import dtos.CourseUpdateDTO;
import dtos.LessonCreateDTO;
import dtos.LessonReadDTO;
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
import jakarta.ws.rs.core.Response.Status;
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
        var courseDtos = CourseReadDTO.fromEntities(courses);

        return Response.ok(courseDtos).build();
    }

    @GET
    @Path("/{id}")
    public Response getCourseById(Long id) {
        var course = courseService.getCourseById(id);
        var courseDto = CourseReadDTO.fromEntity(course);

        return Response.ok(courseDto).build();
    }

    @POST
    public Response createCourse(CourseCreateDTO courseCreateDTO) {
        var course = courseCreateDTO.toEntity();
        courseService.createCourse(course);
        var courseDto = CourseReadDTO.fromEntity(course);

        return Response.created(URI.create("/courses/" + course.getId())).entity(courseDto).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCourse(Long id, CourseUpdateDTO courseUpdateDTO) {
        if (!id.equals(courseUpdateDTO.id())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        var course = courseUpdateDTO.toEntity();
        courseService.updateCourse(course);
        var courseDto = CourseReadDTO.fromEntity(course);

        return Response.ok(courseDto).build();
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
        var lessonDtos = LessonReadDTO.fromEntities(lessons);

        return Response.ok(lessonDtos).build();
    }

    @POST
    @Path("/{courseId}/lessons")
    public Response addLessonToCourse(Long courseId, LessonCreateDTO lessonCreateDTO) {
        var lesson = lessonCreateDTO.toEntity();
        courseService.addLessonToCourse(courseId, lesson);

        return Response.status(Status.CREATED).build();
    }
}

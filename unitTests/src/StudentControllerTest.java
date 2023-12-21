import com.students2.student2.controllers.StudentController;
import com.students2.student2.dtos.StudentDTO;
import com.students2.student2.services.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StudentControllerTest {
    @InjectMocks
    private StudentController studentController;

    @Mock
    private StudentService studentService;

    @Test
    public void testGetAllStudents() {
        List<StudentDTO> expectedStudents = Arrays.asList(
                new StudentDTO(1L, "Serega", 2L),
                new StudentDTO(2L, "Anton", 1L)
        );

        when(studentService.getAllStudents()).thenReturn(expectedStudents);

        ResponseEntity<List<StudentDTO> > response = studentController.getAllStudents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStudents, response.getBody());
    }

    @Test
    public void testGetStudentById() {
        Long studentId = 1L;
        StudentDTO expectedStudent = new StudentDTO(1L, "Nastya", 2L);

        when(studentService.getStudentById(studentId)).thenReturn(ResponseEntity.ok(expectedStudent));

        ResponseEntity<StudentDTO> response = studentController.getStudentById(studentId).getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStudent, response.getBody());
    }


    @Test
    public void testCreateStudent() {StudentDTO requestStudentDTO = new StudentDTO(null, "Valik", 3L);
        StudentDTO createdStudentDTO = new StudentDTO(1L, "Valik", 3L);

        when(studentService.createStudent(requestStudentDTO)).thenReturn(createdStudentDTO);

        ResponseEntity<StudentDTO> response = studentController.createStudent(requestStudentDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdStudentDTO, response.getBody());
    }

    @Test
    public void testUpdateStudent() {
        Long studentId = 1L;
        StudentDTO requestStudentDTO = new StudentDTO(studentId, "Anton", 2L);
        StudentDTO updatedStudentDTO = new StudentDTO(studentId, "Vlad", 2L);

        when(studentService.updateStudent(studentId, requestStudentDTO)).thenReturn(updatedStudentDTO);

        ResponseEntity<StudentDTO> response = studentController.updateStudent(studentId, requestStudentDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedStudentDTO, response.getBody());
    }


    @Test
    public void testDeleteStudent() {
        Long studentId = 1L;
        when(studentService.deleteStudent(studentId)).thenReturn(true);

        ResponseEntity<Void> response = studentController.deleteStudent(studentId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}

import com.students2.student2.controllers.ClassController;
import com.students2.student2.dtos.ClassDTO;
import com.students2.student2.services.ClassService;
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
public class ClassControllerTest {
    @InjectMocks
    private ClassController classController;

    @Mock
    private ClassService classService;

    @Test
    public void testGetAllClasses() {
        List<ClassDTO> expectedClasses = Arrays.asList(
                new ClassDTO(1L, "9M"),
                new ClassDTO(2L, "11M")
        );

        when(classService.getAllClasses()).thenReturn(expectedClasses);

        ResponseEntity<List<ClassDTO>> response = classController.getAllClasses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedClasses, response.getBody());
    }

    @Test
    public void testGetClassById() {
        Long classId = 1L;
        ClassDTO expectedClass = new ClassDTO(classId, "9M");

        when(classService.getClassById(classId)).thenReturn(expectedClass);

        ResponseEntity<ClassDTO> response = classController.getClassById(classId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedClass, response.getBody());
    }


    @Test
    public void testCreateClass() {
        ClassDTO requestClassDTO = new ClassDTO(null, "11A");
        ClassDTO createdClassDTO = new ClassDTO(1L, "11A");

        when(classService.createClass(requestClassDTO)).thenReturn(createdClassDTO);

        ResponseEntity<ClassDTO> response = classController.createClass(requestClassDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdClassDTO, response.getBody());
    }

    @Test
    public void testUpdateClass() {
        Long classId = 1L;
        ClassDTO requestClassDTO = new ClassDTO(classId, "9M");
        ClassDTO updatedClassDTO = new ClassDTO(classId, "10M");

        when(classService.updateClass(classId, requestClassDTO)).thenReturn(updatedClassDTO);

        ResponseEntity<ClassDTO> response = classController.updateClass(classId, requestClassDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedClassDTO, response.getBody());
    }

    @Test
    public void testDeleteClass() {
        Long classId = 1L;

        when(classService.deleteClass(classId)).thenReturn(true);

        ResponseEntity<Void> response = classController.deleteClass(classId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}

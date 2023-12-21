import com.students2.student2.controllers.MarkController;
import com.students2.student2.dtos.MarkDTO;
import com.students2.student2.entities.Mark;
import com.students2.student2.entities.Student;
import com.students2.student2.entities.Subject;
import com.students2.student2.services.MarkService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MarkControllerTest {
    @InjectMocks
    private MarkController markController;

    @Mock
    private MarkService markService;

    @Test
    public void testGetAllMarks() {
        List<MarkDTO> expectedMarks = Arrays.asList(
                new MarkDTO(1L, 9, 1L, 1L, new Date()),
                new MarkDTO(2L, 11, 2L, 2L, new Date())
        );

        when(markService.getAllMarks()).thenReturn(expectedMarks);

        ResponseEntity<List<MarkDTO>> response = markController.getAllMarks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMarks, response.getBody());
    }

    @Test
    public void testGetMarkById() {
        Long markId = 1L;
        MarkDTO expectedMark = new MarkDTO(markId, 11, 1L, 1L, new Date());

        when(markService.getMarkById(markId)).thenReturn(expectedMark);

        ResponseEntity<MarkDTO> response = markController.getMarkById(markId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMark, response.getBody());
    }



    @Test
    public void testUpdateMark() {
        Long markId = 1L;
        MarkDTO requestMarkDTO = new MarkDTO(markId, 8, 1L, 1L, new Date());
        MarkDTO updatedMarkDTO = new MarkDTO(markId, 11, 1L, 1L, new Date());

        when(markService.updateMark(markId, requestMarkDTO)).thenReturn(updatedMarkDTO);

        ResponseEntity<MarkDTO> response = markController.updateMark(markId, requestMarkDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedMarkDTO, response.getBody());
    }

    @Test
    public void testSetMark() {
        Map<String, Object> markData = new HashMap<>();
        markData.put("studentId", 1L);
        markData.put("subjectId", 1L);
        markData.put("markValue", 9);

        Subject subject = new Subject();
        subject.setId(1L);
        Student student = new Student();
        student.setId(1L);

        Mark mark = new Mark(9, subject, student, new Date());

        when(markService.setMark(1L, 1L, 9)).thenReturn(mark);

        ResponseEntity<List<Map<String, Object>>> response = markController.setMark(markData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        Map<String, Object> responseData = response.getBody().get(0);
        assertEquals(1L, responseData.get("studentId"));
        assertEquals(1L, responseData.get("subjectId"));
        assertEquals(9, responseData.get("value"));
    }

}

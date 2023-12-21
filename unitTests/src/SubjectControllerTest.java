import com.students2.student2.controllers.SubjectController;
import com.students2.student2.dtos.SubjectDTO;
import com.students2.student2.services.SubjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubjectControllerTest {
    @InjectMocks
    private SubjectController subjectController;

    @Mock
    private SubjectService subjectService;

    @Test
    public void testGetAllSubjects() {
        List<SubjectDTO> expectedSubjects = Arrays.asList(
                new SubjectDTO(1L, "Math"),
                new SubjectDTO(2L, "Physics")
        );

        when(subjectService.getAllSubjects()).thenReturn(expectedSubjects);

        ResponseEntity<List<SubjectDTO>> response = subjectController.getAllSubjects();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSubjects, response.getBody());
    }

    @Test
    public void testGetSubjectById() {
        Long subjectId = 1L;
        SubjectDTO expectedSubject = new SubjectDTO(subjectId, "Math");

        when(subjectService.getSubjectById(subjectId)).thenReturn(ResponseEntity.ok(expectedSubject));

        ResponseEntity<SubjectDTO> response = subjectController.getSubjectById(subjectId).getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSubject, response.getBody());
    }



    @Test
    public void testCreateSubject() {
        SubjectDTO requestSubjectDTO = new SubjectDTO(null, "History");
        SubjectDTO createdSubjectDTO = new SubjectDTO(1L, "History");

        when(subjectService.createSubject(requestSubjectDTO)).thenReturn(createdSubjectDTO);

        ResponseEntity<SubjectDTO> response = subjectController.createSubject(requestSubjectDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdSubjectDTO, response.getBody());
    }

    @Test
    public void testUpdateSubject() {
        Long subjectId = 1L;
        SubjectDTO requestSubjectDTO = new SubjectDTO(subjectId, "Geography");
        SubjectDTO updatedSubjectDTO = new SubjectDTO(subjectId, "Geography");

        when(subjectService.updateSubject(subjectId, requestSubjectDTO)).thenReturn(updatedSubjectDTO);

        ResponseEntity<SubjectDTO> response = subjectController.updateSubject(subjectId, requestSubjectDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedSubjectDTO, response.getBody());
    }

    @Test
    public void testDeleteSubject() {
        Long subjectId = 1L;
        when(subjectService.deleteSubject(subjectId)).thenReturn(true);

        ResponseEntity<Void> response = subjectController.deleteSubject(subjectId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testAssociateSubjectWithClass() {
        Long subjectId = 1L;
        Long classId = 2L;
        Map<String, Long> associationData = Map.of("subjectId", subjectId, "classId", classId);

        when(subjectService.associateSubjectWithClass(subjectId, classId)).thenReturn(associationData);

        ResponseEntity<Map<String, Long>> response = subjectController.associateSubjectWithClass(subjectId, classId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(associationData, response.getBody());
    }

    @Test
    public void testRemoveSubjectFromClass() {
        Long subjectId = 1L;
        Long classId = 2L;
        Map<String, Long> disassociationData = Map.of("subjectId", subjectId, "classId", classId);

        when(subjectService.removeSubjectFromClass(subjectId, classId)).thenReturn(disassociationData);

        ResponseEntity<Map<String, Long>> response = subjectController.removeSubjectFromClass(subjectId, classId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(disassociationData, response.getBody());
    }

}

package com.students2.student2;

import com.students2.student2.dtos.SubjectDTO;
import com.students2.student2.entities.SchoolClass;
import com.students2.student2.entities.Subject;
import com.students2.student2.repositories.ClassRepository;
import com.students2.student2.repositories.SubjectRepository;
import com.students2.student2.services.subjectservice.SubjectServiceImpl;
import com.students2.student2.exceptions.SubjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private ClassRepository classRepository;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllSubjects() {
        List<Subject> subjects = Arrays.asList(
                new Subject(1L, "Math"),
                new Subject(2L, "Geography")
        );
        when(subjectRepository.findAll()).thenReturn(subjects);

        List<SubjectDTO> result = subjectService.getAllSubjects();

        assertEquals(2, result.size());
        assertEquals("Math", result.get(0).getName());
        assertEquals("Geography", result.get(1).getName());
        verify(subjectRepository).findAll();
    }

    @Test
    public void testGetSubjectById_Exists() {
        Long subjectId = 1L;
        Subject subject = new Subject(subjectId, "Math");
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));

        ResponseEntity<SubjectDTO> result = subjectService.getSubjectById(subjectId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Math", result.getBody().getName());
        verify(subjectRepository).findById(subjectId);
    }

    @Test
    public void testGetSubjectById_NotExists() {
        Long subjectId = 1L;
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());

        ResponseEntity<SubjectDTO> result = subjectService.getSubjectById(subjectId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(subjectRepository).findById(subjectId);
    }

    @Test
    public void testCreateSubject() {
        SubjectDTO subjectDTO = new SubjectDTO(null, "Physics");
        Subject savedSubject = new Subject(1L, "Physics");
        when(subjectRepository.save(any(Subject.class))).thenReturn(savedSubject);

        SubjectDTO result = subjectService.createSubject(subjectDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Physics", result.getName());
        verify(subjectRepository).save(any(Subject.class));
    }

    @Test
    public void testUpdateSubject_Exists() {
        Long subjectId = 1L;
        SubjectDTO updatedSubjectDTO = new SubjectDTO(subjectId, "Biology");
        Subject subject = new Subject(subjectId, "Physics");
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(subjectRepository.save(any(Subject.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SubjectDTO result = subjectService.updateSubject(subjectId, updatedSubjectDTO);

        assertNotNull(result);
        assertEquals(subjectId, result.getId());
        assertEquals("Biology", result.getName());
        verify(subjectRepository).findById(subjectId);
        verify(subjectRepository).save(any(Subject.class));
    }

    @Test
    public void testUpdateSubject_NotExists() {
        Long subjectId = 1L;
        SubjectDTO updatedSubjectDTO = new SubjectDTO(subjectId, "Geography");
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());

        SubjectDTO result = subjectService.updateSubject(subjectId, updatedSubjectDTO);

        assertNull(result);
        verify(subjectRepository).findById(subjectId);
        verify(subjectRepository, never()).save(any(Subject.class));
    }

    @Test
    public void testDeleteSubject_Exists() {
        Long subjectId = 1L;
        when(subjectRepository.existsById(subjectId)).thenReturn(true);

        boolean result = subjectService.deleteSubject(subjectId);

        assertTrue(result);
        verify(subjectRepository).existsById(subjectId);
        verify(subjectRepository).deleteById(subjectId);
    }

    @Test
    public void testDeleteSubject_NotExists() {
        Long subjectId = 1L;
        when(subjectRepository.existsById(subjectId)).thenReturn(false);

        assertThrows(SubjectNotFoundException.class, () ->
                subjectService.deleteSubject(subjectId)
        );

        verify(subjectRepository).existsById(subjectId);
        verify(subjectRepository, never()).deleteById(subjectId);
    }

    @Test
    public void testAssociateSubjectWithClass_BothExist() {
        Long subjectId = 1L;
        Long classId = 1L;
        Subject subject = new Subject(subjectId, "Math");
        SchoolClass classEntity = new SchoolClass(classId, "9M");
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(classRepository.findById(classId)).thenReturn(Optional.of(classEntity));

        Map<String, Long> result = subjectService.associateSubjectWithClass(subjectId, classId);

        assertNotNull(result);
        assertEquals(subjectId, result.get("subjectId"));
        assertEquals(classId, result.get("classId"));
        verify(subjectRepository).findById(subjectId);
        verify(classRepository).findById(classId);
        verify(subjectRepository).save(subject);
    }

    @Test
    public void testAssociateSubjectWithClass_SubjectOrClassNotExist() {
        Long subjectId = 1L;
        Long classId = 1L;
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());
        when(classRepository.findById(classId)).thenReturn(Optional.empty());

        Map<String, Long> result = subjectService.associateSubjectWithClass(subjectId, classId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(subjectRepository).findById(subjectId);
        verify(classRepository).findById(classId);
        verify(subjectRepository, never()).save(any(Subject.class));
    }

    @Test
    public void testRemoveSubjectFromClass_BothExistAndAssociated() {
        Long subjectId = 1L;
        Long classId = 1L;
        Subject subject = new Subject(subjectId, "Math");
        SchoolClass classEntity = new SchoolClass(classId, "9M");
        subject.addClass(classEntity);
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(classRepository.findById(classId)).thenReturn(Optional.of(classEntity));

        Map<String, Long> result = subjectService.removeSubjectFromClass(subjectId, classId);

        assertNotNull(result);
        assertEquals(subjectId, result.get("subjectId"));
        assertEquals(classId, result.get("classId"));
        verify(subjectRepository).findById(subjectId);
        verify(classRepository).findById(classId);
        verify(subjectRepository).save(subject);
    }

    @Test
    public void testRemoveSubjectFromClass_NotAssociated() {
        Long subjectId = 1L;
        Long classId = 1L;
        Subject subject = new Subject(subjectId, "Math");
        SchoolClass classEntity = new SchoolClass(classId, "9M");
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(classRepository.findById(classId)).thenReturn(Optional.of(classEntity));

        Map<String, Long> result = subjectService.removeSubjectFromClass(subjectId, classId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(subjectRepository).findById(subjectId);
        verify(classRepository).findById(classId);
        verify(subjectRepository, never()).save(any(Subject.class));
    }

    @Test
    public void testRemoveSubjectFromClass_SubjectOrClassNotExist() {
        Long subjectId = 1L;
        Long classId = 1L;
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());
        when(classRepository.findById(classId)).thenReturn(Optional.empty());

        Map<String, Long> result = subjectService.removeSubjectFromClass(subjectId, classId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(subjectRepository).findById(subjectId);
        verify(classRepository).findById(classId);
        verify(subjectRepository, never()).save(any(Subject.class));
    }
}

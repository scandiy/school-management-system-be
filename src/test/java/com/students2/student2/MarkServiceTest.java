package com.students2.student2;

import com.students2.student2.dtos.MarkDTO;
import com.students2.student2.entities.Mark;
import com.students2.student2.entities.SchoolClass;
import com.students2.student2.entities.Student;
import com.students2.student2.entities.Subject;
import com.students2.student2.exceptions.MarkNotFoundException;
import com.students2.student2.exceptions.StudentNotFoundException;
import com.students2.student2.exceptions.SubjectNotFoundException;
import com.students2.student2.repositories.MarkRepository;
import com.students2.student2.repositories.StudentRepository;
import com.students2.student2.repositories.SubjectRepository;
import com.students2.student2.services.mark.MarkServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MarkServiceTest {

    @Mock
    private MarkRepository markRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private MarkServiceImpl markService;

    public MarkServiceTest() {
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetMarkById_Exists() {
        Long markId = 1L;
        Mark mark = new Mark(markId, 12, new Subject(1L, "Math"), new Student(1L, "Andrey"), new Date());

        when(markRepository.findById(markId)).thenReturn(Optional.of(mark));

        MarkDTO result = markService.getMarkById(markId);

        assertNotNull(result);
        assertEquals(markId, result.getId());
        verify(markRepository).findById(markId);
    }

    @Test
    void testGetMarkById_NotExists() {
        Long markId = 1L;
        when(markRepository.findById(markId)).thenReturn(Optional.empty());

        MarkDTO result = markService.getMarkById(markId);

        assertNull(result);
        verify(markRepository).findById(markId);
    }

    @Test
    void testUpdateMark_Exists() {
        Long markId = 1L;
        MarkDTO updatedMarkDTO = new MarkDTO(markId, 10, 1L, 1L, new Date());
        Mark mark = new Mark(markId, 12, new Subject(1L, "Math"), new Student(1L, "Andrey"), new Date());

        when(markRepository.findById(markId)).thenReturn(Optional.of(mark));
        when(markRepository.save(any(Mark.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MarkDTO result = markService.updateMark(markId, updatedMarkDTO);

        assertNotNull(result);
        assertEquals(markId, result.getId());
        assertEquals(updatedMarkDTO.getValue(), result.getValue());
        verify(markRepository).findById(markId);
        verify(markRepository).save(any(Mark.class));
    }

    @Test
    void testUpdateMark_NotExists() {
        Long markId = 1L;
        MarkDTO updatedMarkDTO = new MarkDTO(markId, 10, 1L, 1L, new Date());

        when(markRepository.findById(markId)).thenReturn(Optional.empty());

        MarkDTO result = markService.updateMark(markId, updatedMarkDTO);

        assertNull(result);
        verify(markRepository).findById(markId);
        verify(markRepository, never()).save(any(Mark.class));
    }

    @Test
    void testDeleteMark_Exists() {
        Long markId = 1L;
        when(markRepository.existsById(markId)).thenReturn(true);

        boolean result = markService.deleteMark(markId);

        assertTrue(result);
        verify(markRepository).existsById(markId);
        verify(markRepository).deleteById(markId);
    }

    @Test
    void testDeleteMark_NotExists() {
        Long markId = 1L;
        when(markRepository.existsById(markId)).thenReturn(false);

        assertThrows(MarkNotFoundException.class, () ->
                markService.deleteMark(markId)
        );

        verify(markRepository).existsById(markId);
        verify(markRepository, never()).deleteById(markId);
    }

    @Test
    void testSetMark_Success() {
        Long studentId = 1L;
        Long subjectId = 1L;
        int markValue = 12;

        Student student = new Student(studentId, "Andrey");
        Subject subject = new Subject(subjectId, "Math");

        List<Subject> subjectList = Collections.singletonList(subject);
        Set<Subject> subjectSet = new HashSet<>(subjectList);
        student.setStudentClass(new SchoolClass(1L, "10M", subjectSet));

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(markRepository.save(any(Mark.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Mark result = markService.setMark(studentId, subjectId, markValue);

        assertNotNull(result);
        assertEquals(markValue, result.getValue());
        assertEquals(studentId, result.getStudent().getId());
        assertEquals(subjectId, result.getSubject().getId());
        verify(studentRepository).findById(studentId);
        verify(subjectRepository).findById(subjectId);
        verify(markRepository).save(any(Mark.class));
    }

    @Test
    void testSetMark_StudentNotFound() {
        Long studentId = 1L;
        Long subjectId = 1L;
        int markValue = 12;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () ->
                markService.setMark(studentId, subjectId, markValue)
        );

        verify(studentRepository).findById(studentId);
        verify(subjectRepository, never()).findById(subjectId);
        verify(markRepository, never()).save(any(Mark.class));
    }

    @Test
    void testSetMark_SubjectNotFound() {
        Long studentId = 1L;
        Long subjectId = 1L;
        int markValue = 12;

        Student student = new Student(studentId, "Andrey");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());

        assertThrows(SubjectNotFoundException.class, () ->
                markService.setMark(studentId, subjectId, markValue)
        );

        verify(studentRepository).findById(studentId);
        verify(subjectRepository).findById(subjectId);
        verify(markRepository, never()).save(any(Mark.class));
    }
}

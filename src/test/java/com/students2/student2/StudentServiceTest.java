package com.students2.student2;

import com.students2.student2.dtos.StudentDTO;
import com.students2.student2.entities.Mark;
import com.students2.student2.entities.SchoolClass;
import com.students2.student2.entities.Student;
import com.students2.student2.entities.Subject;
import com.students2.student2.exceptions.StudentMovingToAnotherClassException;
import com.students2.student2.exceptions.StudentNotFoundException;
import com.students2.student2.repositories.ClassRepository;
import com.students2.student2.repositories.MarkRepository;
import com.students2.student2.repositories.StudentRepository;
import com.students2.student2.services.student.StudentServiceImpl;
import org.junit.jupiter.api.Assertions;
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

public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ClassRepository classRepository;

    @Mock
    private MarkRepository markRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testMoveStudentToClass_Success() {
        Long studentId = 1L;
        Long classId = 2L;
        Subject subject = new Subject("Math");
        Student student = new Student(studentId, "Andrey");
        SchoolClass newClass = new SchoolClass(classId, "9A");

        List<Mark> marks = Arrays.asList(
                new Mark(4, subject, student, new Date()),
                new Mark(3, subject, student, new Date()),
                new Mark(3, subject, student, new Date())
        );

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(classRepository.findById(classId)).thenReturn(Optional.of(newClass));
        when(markRepository.findByStudentId(studentId)).thenReturn(marks);

        StudentDTO result = studentService.moveStudentToClass(studentId, classId);

        assertNotNull(result);
        assertEquals(studentId, result.getId());
        assertEquals(classId, result.getClassId());
        verify(studentRepository).findById(studentId);
        verify(classRepository).findById(classId);
        verify(markRepository).findByStudentId(studentId);
        verify(studentRepository).save(student);
    }

    @Test
    void testMoveStudentToClass_AverageMarksLessThen3() {
        Long studentId = 1L;
        Long classId = 2L;
        Subject subject = new Subject("Math");
        Student student = new Student(studentId, "Andrey");
        SchoolClass newClass = new SchoolClass(classId, "9A");

        List<Mark> marks = Arrays.asList(
                new Mark(2, subject, student, new Date()),
                new Mark(3, subject, student, new Date()),
                new Mark(2, subject, student, new Date())
        );

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(classRepository.findById(classId)).thenReturn(Optional.of(newClass));
        when(markRepository.findByStudentId(studentId)).thenReturn(marks);

        assertThrows(StudentMovingToAnotherClassException.class, () ->
                studentService.moveStudentToClass(studentId, classId)
        );

        verify(studentRepository).findById(studentId);
        verify(classRepository, never()).findById(classId);
        verify(markRepository).findByStudentId(studentId);
        verify(studentRepository, never()).save(student);
    }

    @Test
    void testMoveStudentToClass_StudentNotFound() {
        Long studentId = 1L;
        Long classId = 2L;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () ->
                studentService.moveStudentToClass(studentId, classId)
        );

        verify(studentRepository).findById(studentId);
        verify(markRepository, never()).findByStudentId(anyLong());
        verify(classRepository, never()).findById(anyLong());
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    public void testMoveStudentToClass_ClassNotFound() {
        Long studentId = 1L;
        Long classId = 2L;

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(new Student()));
        when(markRepository.findByStudentId(studentId)).thenReturn(new ArrayList<>());
        when(classRepository.findById(classId)).thenReturn(Optional.empty());

        Assertions.assertThrows(StudentMovingToAnotherClassException.class, () -> {
            studentService.moveStudentToClass(studentId, classId);
        });

        verify(studentRepository, never()).save(any());
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = Arrays.asList(
                new Student(1L, "Andrey", new SchoolClass(1L, "9M")),
                new Student(2L, "Sergey", new SchoolClass(2L, "10M"))
        );
        when(studentRepository.findAll()).thenReturn(students);

        List<StudentDTO> result = studentService.getAllStudents();

        assertEquals(2, result.size());
        verify(studentRepository).findAll();
    }

    @Test
    void testGetStudentById_Exists() {
        Long studentId = 1L;
        Student student = new Student(studentId, "Andrey", new SchoolClass(1L, "9M"));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        ResponseEntity<StudentDTO> result = studentService.getStudentById(studentId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(studentId, result.getBody().getId());
        verify(studentRepository).findById(studentId);
    }

    @Test
    void testGetStudentById_NotExists() {
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        ResponseEntity<StudentDTO> result = studentService.getStudentById(studentId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(studentRepository).findById(studentId);
    }

    @Test
    void testCreateStudent() {
        StudentDTO studentDTO = new StudentDTO(null, "Andrey", 1L);
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student createdStudent = invocation.getArgument(0);
            createdStudent.setId(1L);
            return createdStudent;
        });

        StudentDTO result = studentService.createStudent(studentDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(studentDTO.getName(), result.getName());
        assertEquals(studentDTO.getClassId(), result.getClassId());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void testUpdateStudent_Exists() {
        Long studentId = 1L;
        StudentDTO updatedStudentDTO = new StudentDTO(studentId, "Sergey", 2L);
        Student student = new Student(studentId, "Andrey", new SchoolClass(1L, "9M"));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StudentDTO result = studentService.updateStudent(studentId, updatedStudentDTO);

        assertNotNull(result);
        assertEquals(updatedStudentDTO.getId(), result.getId());
        assertEquals(updatedStudentDTO.getName(), result.getName());
        assertEquals(updatedStudentDTO.getClassId(), result.getClassId());
        verify(studentRepository).findById(studentId);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void testUpdateStudent_NotExists() {
        Long studentId = 1L;
        StudentDTO updatedStudentDTO = new StudentDTO(studentId, "Andrey", 2L);
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        StudentDTO result = studentService.updateStudent(studentId, updatedStudentDTO);

        assertNull(result);
        verify(studentRepository).findById(studentId);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testDeleteStudent_Exists() {
        Long studentId = 1L;
        when(studentRepository.existsById(studentId)).thenReturn(true);

        boolean result = studentService.deleteStudent(studentId);

        assertTrue(result);
        verify(studentRepository).existsById(studentId);
        verify(studentRepository).deleteById(studentId);
    }

    @Test
    void testDeleteStudent_NotExists() {
        Long studentId = 1L;
        when(studentRepository.existsById(studentId)).thenReturn(false);

        assertThrows(StudentNotFoundException.class, () ->
                studentService.deleteStudent(studentId)
        );

        verify(studentRepository).existsById(studentId);
        verify(studentRepository, never()).deleteById(studentId);
    }
}

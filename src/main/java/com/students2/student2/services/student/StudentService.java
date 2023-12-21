package com.students2.student2.services.student;

import com.students2.student2.dtos.StudentDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StudentService {
    List<StudentDTO> getAllStudents();
    ResponseEntity<StudentDTO> getStudentById(Long id);
    StudentDTO createStudent(StudentDTO studentDTO);
    StudentDTO updateStudent(Long id, StudentDTO studentDTO);
    boolean deleteStudent(Long id);
    StudentDTO moveStudentToClass(Long studentId, Long classId);
}

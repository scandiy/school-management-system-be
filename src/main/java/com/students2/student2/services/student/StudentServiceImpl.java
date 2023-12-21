package com.students2.student2.services.student;

import com.students2.student2.dtos.StudentDTO;
import com.students2.student2.entities.Class;
import com.students2.student2.entities.Mark;
import com.students2.student2.entities.Student;
import com.students2.student2.repositories.ClassRepository;
import com.students2.student2.repositories.MarkRepository;
import com.students2.student2.repositories.StudentRepository;
import com.students2.student2.exceptions.StudentMovingToAnotherClassException;
import com.students2.student2.exceptions.StudentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final MarkRepository markRepository;
    private final ClassRepository classRepository;
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    public StudentServiceImpl(StudentRepository studentRepository, MarkRepository markRepository, ClassRepository classRepository) {
        this.studentRepository = studentRepository;
        this.markRepository  = markRepository;
        this.classRepository = classRepository;
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        List<StudentDTO> studentDTOs = students.stream()
                .map(student -> new StudentDTO(student.getId(), student.getName(), student.getStudentClass().getId()))
                .collect(Collectors.toList());
        logger.info("Retreived all students: {}", studentDTOs);
        return studentDTOs;
    }

    @Override
    public ResponseEntity<StudentDTO> getStudentById(Long studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            StudentDTO studentDTO = new StudentDTO(student.getId(), student.getName(), student.getStudentClass().getId());
            logger.info("Retreived student by ID {}: {}", studentId, studentDTO);
            return ResponseEntity.ok(studentDTO);
        } else {
            logger.info("Student with ID {} not found", studentId);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = new Student();
        student.setName(studentDTO.getName());

        if (studentDTO.getClassId() != null) {
            Class studentClass = new Class();
            studentClass.setId(studentDTO.getClassId());
            student.setStudentClass(studentClass);
        }

        student = studentRepository.save(student);
        StudentDTO createdStudentDTO = new StudentDTO(student.getId(), student.getName(), student.getStudentClass().getId());
        logger.info("Created student: {}", createdStudentDTO);
        return createdStudentDTO;
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        return studentRepository.findById(id).map(student -> {
            student.setName(studentDTO.getName());

            if (studentDTO.getClassId() != null) {
                Class studentClass = new Class();
                studentClass.setId(studentDTO.getClassId());
                student.setStudentClass(studentClass);
            }

            student = studentRepository.save(student);
            StudentDTO updatedStudentDTO = new StudentDTO(student.getId(), student.getName(), student.getStudentClass().getId());
            logger.info("Updated student with ID {}: {}", id, updatedStudentDTO);
            return updatedStudentDTO;
        }).orElse(null);
    }

    @Override
    @Transactional
    public boolean deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            logger.info("Deleted student with ID: {}", id);
            return true;
        }
        logger.info("Student with ID {} not found for deletion", id);
        throw new StudentNotFoundException();
    }

    @Override
    @Transactional
    public StudentDTO moveStudentToClass(Long studentId, Long classId) {
        Student student = studentRepository.findById(studentId).orElse(null);

        if (student == null) {
            logger.error("Student with ID {} not found for moving to class", studentId);
            throw new StudentNotFoundException();
        }

        double averageMarks = calculateAverageMarks(student);

        if (averageMarks >= 3) {
            clearStudentMarks(student);

            Class newClass = classRepository.findById(classId).orElse(null);
            if (newClass != null) {
                student.setStudentClass(newClass);
                studentRepository.save(student);

                StudentDTO studentDTO = new StudentDTO(student.getId(), student.getName(), newClass.getId());
                logger.info("Moved student with ID {} to class with ID {}: {}", studentId, classId, studentDTO);
                return studentDTO;
            }
        }

        logger.error("Moving student with ID {} to class with ID {} failed due to invalid conditions", studentId, classId);
        throw new StudentMovingToAnotherClassException();
    }

    public double calculateAverageMarks(Student student) {
        List<Mark> marks = markRepository.findByStudentId(student.getId());

        if (marks.isEmpty()) {
            return 0.0;
        }

        int totalMarks = 0;
        for (Mark mark : marks) {
            totalMarks += mark.getValue();
        }

        return (double) totalMarks / marks.size();
    }

    public void clearStudentMarks(Student student) {
        List<Mark> marks = markRepository.findByStudentId(student.getId());

        if (!marks.isEmpty()) {
            for (Mark mark : marks) {
                markRepository.delete(mark);
            }
        }
    }
}

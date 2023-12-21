package com.students2.student2.services.mark;

import com.students2.student2.dtos.MarkDTO;
import com.students2.student2.entities.Mark;
import com.students2.student2.entities.Student;
import com.students2.student2.entities.Subject;
import com.students2.student2.repositories.MarkRepository;
import com.students2.student2.repositories.StudentRepository;
import com.students2.student2.repositories.SubjectRepository;
import com.students2.student2.exceptions.MarkNotFoundException;
import com.students2.student2.exceptions.SubjectNotFoundException;
import com.students2.student2.exceptions.SubjectNotRelatedToStudentException;
import com.students2.student2.exceptions.StudentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class    MarkServiceImpl implements MarkService {

    private final MarkRepository markRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private static final Logger logger = LoggerFactory.getLogger(MarkServiceImpl.class);

    public MarkServiceImpl(MarkRepository markRepository, StudentRepository studentRepository, SubjectRepository subjectRepository1) {
        this.markRepository = markRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository1;
    }

    @Override
    public MarkDTO getMarkById(Long id) {
        return markRepository.findById(id).map(mark -> {
            MarkDTO markDTO = new MarkDTO(mark.getId(), mark.getValue(), mark.getSubject().getId(), mark.getStudent().getId(), mark.getDate());
            logger.info("Retreived mark by ID {}: {}", id, markDTO);
            return markDTO;
        }).orElse(null);
    }

    @Override
    public MarkDTO updateMark(Long id, MarkDTO markDTO) {
        return markRepository.findById(id).map(mark -> {
            mark.setValue(markDTO.getValue());
            mark = markRepository.save(mark);
            MarkDTO updatedMarkDTO = new MarkDTO(mark.getId(), mark.getValue(), mark.getSubject().getId(), mark.getStudent().getId(), mark.getDate());
            logger.info("Updated mark with ID {}: {}", id, updatedMarkDTO);
            return updatedMarkDTO;
        }).orElse(null);
    }

    @Override
    @Transactional
    public boolean deleteMark(Long id) {
        if (markRepository.existsById(id)) {
            markRepository.deleteById(id);
            logger.info("Deleted mark with ID: {}", id);
            return true;
        } else {
            logger.info("Mark with ID {} not found for deletion", id);
            throw new MarkNotFoundException();
        }
    }

    @Transactional
    public Mark setMark(Long studentId, Long subjectId, int markValue) {
        Student student = studentRepository.findById(studentId).orElse(null);

        if (student == null) {
            logger.error("Student with ID {} not found for setting a mark", studentId);
            throw new StudentNotFoundException();
        }
        Subject subject = subjectRepository.findById(subjectId).orElse(null);

        if (subject == null) {
            logger.error("Subject with ID {} not found for setting a mark", subjectId);
            throw new SubjectNotFoundException();
        }
        if (!student.getStudentClass().getSubjects().contains(subject)) {
            logger.error("Student is not related to the subject with ID {}", subjectId);
            throw new SubjectNotRelatedToStudentException();
        }
        Mark mark = new Mark(markValue, subject, student, new Date());
        markRepository.save(mark);
        logger.info("Created a new mark for student with ID {} and subject with ID {}: {}", studentId, subjectId, mark);
        return mark;
    }

    @Override
    public List<MarkDTO> getAllMarksForStudent(Long studentId) {
        List<Mark> marks = markRepository.findAllByStudentId(studentId);

        List<MarkDTO> markDTOs = marks.stream()
                .map(mark -> new MarkDTO(
                        mark.getId(),
                        mark.getValue(),
                        mark.getSubject().getId(),
                        mark.getStudent().getId(),
                        mark.getDate()
                ))
                .collect(Collectors.toList());

        logger.info("Retrieved {} marks for student with Id {}", markDTOs.size(), studentId);
        return markDTOs;
    }
}

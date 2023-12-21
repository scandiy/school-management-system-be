/*
package com.students2.student2.jobs;

import com.students2.student2.entities.Student;
import com.students2.student2.entities.Subject;
import com.students2.student2.repositories.StudentRepository;
import com.students2.student2.services.mark.MarkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
public class SetMarkJob {
    private final StudentRepository studentRepository;

    private final MarkService markService;
    private static final Logger logger = LoggerFactory.getLogger(SetMarkJob.class);

    public SetMarkJob(StudentRepository studentRepository, MarkService markService) {
        this.studentRepository = studentRepository;
        this.markService = markService;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void setMarkJob() {
        List<Student> students = studentRepository.findAll();

        if (students.isEmpty()) {
            logger.error("No students found. Cannot set a mark.");
            return;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(students.size());
        Student randomStudent = students.get(randomIndex);

        Long studentId = randomStudent.getId();

        Set<Subject> subjectSet = randomStudent.getStudentClass().getSubjects();

        List<Subject> subjects = new ArrayList<>(subjectSet);
        if (subjects.isEmpty()) {
            logger.error("No subjects found for the random student. Cannot set a mark.");
            return;
        }
        int randomSubjectIndex = random.nextInt(subjects.size());
        Subject randomSubject = subjects.get(randomSubjectIndex);
        Long subjectId = randomSubject.getId();

        int markValue = random.nextInt(12 - 7 + 1) + 7;

        this.markService.setMark(studentId, subjectId, markValue);

        logger.info("Setting a mark for a random student (ID: {}) in subject (ID: {}) with mark: {}", studentId, subjectId, markValue);
    }
}
*/

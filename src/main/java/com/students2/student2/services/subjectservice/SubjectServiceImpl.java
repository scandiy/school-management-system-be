package com.students2.student2.services.subjectservice;

import com.students2.student2.dtos.SubjectDTO;
import com.students2.student2.entities.Subject;
import com.students2.student2.entities.SchoolClass;
import com.students2.student2.repositories.ClassRepository;
import com.students2.student2.repositories.SubjectRepository;
import jakarta.transaction.Transactional;
import com.students2.student2.exceptions.SubjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final ClassRepository classRepository;
    private static final Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

    public SubjectServiceImpl(SubjectRepository subjectRepository, ClassRepository classRepository) {
        this.subjectRepository = subjectRepository;
        this.classRepository = classRepository;
    }

    @Override
    public List<SubjectDTO> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        List<SubjectDTO> subjectDTOs = subjects.stream()
                .map(subject -> new SubjectDTO(subject.getId(), subject.getName()))
                .collect(Collectors.toList());
        logger.info("Retreived all subjects: {}", subjectDTOs);
        return subjectDTOs;
    }

    @Override
    public ResponseEntity<SubjectDTO> getSubjectById(Long subjectId) {
        Optional<Subject> subjectOptional = subjectRepository.findById(subjectId);

        if (subjectOptional.isPresent()) {
            Subject subject = subjectOptional.get();
            SubjectDTO subjectDTO = new SubjectDTO(subject.getId(), subject.getName());
            logger.info("Retreived subject by ID {}: {}", subjectId, subjectDTO);
            return ResponseEntity.ok(subjectDTO);
        } else {
            logger.info("Subject with ID {} not found", subjectId);
            return ResponseEntity.notFound().build();
        }
    }



    @Override
    public SubjectDTO createSubject(SubjectDTO subjectDTO) {
        Subject subject = new Subject();
        subject.setName(subjectDTO.getName());
        subject = subjectRepository.save(subject);
        SubjectDTO createdSubjectDTO = new SubjectDTO(subject.getId(), subject.getName());
        logger.info("Created subject: {}", createdSubjectDTO);
        return createdSubjectDTO;
    }

    @Override
    public SubjectDTO updateSubject(Long id, SubjectDTO subjectDTO) {
        return subjectRepository.findById(id).map(subject -> {
            subject.setName(subjectDTO.getName());
            subject = subjectRepository.save(subject);
            SubjectDTO updatedSubjectDTO = new SubjectDTO(subject.getId(), subject.getName());
            logger.info("Updated subject with ID {}: {}", id, updatedSubjectDTO);
            return updatedSubjectDTO;
        }).orElse(null);
    }

    @Override
    @Transactional
    public boolean deleteSubject(Long id) {
        if (subjectRepository.existsById(id)) {
            subjectRepository.deleteById(id);
            logger.info("Deleted subject with ID: {}", id);
            return true;
        }
        logger.info("Subject with ID {} not found for deletion", id);
        throw new SubjectNotFoundException();
    }

    @Override
    @Transactional
    public Map<String, Long> associateSubjectWithClass(Long subjectId, Long classId) {
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        SchoolClass classEntity = classRepository.findById(classId).orElse(null);
        Map<String, Long> associationData = new HashMap<>();

        if (subject != null && classEntity != null) {
            if (!subject.getClasses().contains(classEntity)) {
                subject.addClass(classEntity);
                subjectRepository.save(subject);
                associationData.put("subjectId", subjectId);
                associationData.put("classId", classId);
                logger.info("Associated subject with ID {} to class with ID {}: {}", subjectId, classId, associationData);
            }
        }
        return associationData;
    }

    @Override
    @Transactional
    public Map<String, Long> removeSubjectFromClass(Long subjectId, Long classId) {
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        SchoolClass classEntity = classRepository.findById(classId).orElse(null);
        Map<String, Long> disassociationData = new HashMap<>();

        if (subject != null && classEntity != null && subject.getClasses().contains(classEntity)) {
            subject.removeClass(classEntity);
            subjectRepository.save(subject);
            disassociationData.put("subjectId", subjectId);
            disassociationData.put("classId", classId);
            logger.info("Removed subject with ID {} from class with ID {}: {}", subjectId, classId, disassociationData);
        }
        return disassociationData;
    }
}

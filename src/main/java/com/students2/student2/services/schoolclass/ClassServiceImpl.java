package com.students2.student2.services.schoolclass;

import com.students2.student2.dtos.ClassDTO;
import com.students2.student2.entities.SchoolClass;
import com.students2.student2.repositories.ClassRepository;
import com.students2.student2.exceptions.StudentNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

@Service
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private static final Logger logger = LoggerFactory.getLogger(ClassServiceImpl.class);

    public ClassServiceImpl(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }
    @Override
    public List<ClassDTO> getAllClasses() {
        List<SchoolClass> classes = classRepository.findAll();
        List<ClassDTO> classDTOs = classes.stream().map(classEntity -> new ClassDTO(
                classEntity.getId(),
                classEntity.getName()
        )).collect(Collectors.toList());
        logger.info("Get all classes: {}", classDTOs);
        return classDTOs;
    }

    @Override
    public ClassDTO getClassById(Long id) {
        Optional<SchoolClass> classOptional = classRepository.findById(id);
        ClassDTO classDTO = classOptional.map(classEntity -> new ClassDTO(
                classEntity.getId(),
                classEntity.getName()
        )).orElse(null);
        if (classDTO != null) {
            logger.info("Retreived class by ID {}: {}", id, classDTO);
        } else {
            logger.info("Class with ID {} not found", id);
        }
        return classDTO;
    }

    @Override
    public ClassDTO createClass(ClassDTO classDTO) {
        SchoolClass classEntity = new SchoolClass();
        classEntity.setName(classDTO.getName());

        classEntity = classRepository.save(classEntity);
        ClassDTO createdClassDTO = new ClassDTO(classEntity.getId(), classEntity.getName());
        logger.info("Created class: {}", createdClassDTO);
        return createdClassDTO;
    }

    @Override
    public ClassDTO updateClass(Long id, ClassDTO classDTO) {
        return classRepository.findById(id).map(classEntity -> {
            classEntity.setName(classDTO.getName());
            classEntity = classRepository.save(classEntity);
            ClassDTO updatedClassDTO = new ClassDTO(classEntity.getId(), classEntity.getName());
            logger.info("Updated class with ID {}: {}", id, updatedClassDTO);
            return updatedClassDTO;
        }).orElse(null);
    }

    @Override
    @Transactional
    public boolean deleteClass(Long id) {
        if (classRepository.existsById(id)) {
            classRepository.deleteById(id);
            logger.info("Deleted class with ID: {}", id);
            return true;
        } else {
            logger.info("Class with ID {} not found for deletion", id);
            throw new StudentNotFoundException();
        }
    }

}

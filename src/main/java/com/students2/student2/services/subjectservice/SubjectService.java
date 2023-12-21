package com.students2.student2.services.subjectservice;

import com.students2.student2.dtos.SubjectDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface SubjectService {
    List<SubjectDTO> getAllSubjects();
    ResponseEntity<SubjectDTO> getSubjectById(Long id);
    SubjectDTO createSubject(SubjectDTO subjectDTO);
    SubjectDTO updateSubject(Long id, SubjectDTO subjectDTO);
    boolean deleteSubject(Long id);
    Map<String, Long> associateSubjectWithClass(Long subjectId, Long classId);
    Map<String, Long> removeSubjectFromClass(Long subjectId, Long classId);
}

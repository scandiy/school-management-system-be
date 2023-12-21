package com.students2.student2.controllers;

import com.students2.student2.dtos.SubjectDTO;
import com.students2.student2.services.subjectservice.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        List<SubjectDTO> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Long id) {
        ResponseEntity<SubjectDTO> subject = subjectService.getSubjectById(id);
        if (subject.getBody() != null) {
            return ResponseEntity.ok(subject.getBody());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectDTO> createSubject(@RequestBody SubjectDTO subjectDTO) {
        SubjectDTO createdSubject = subjectService.createSubject(subjectDTO);
        return ResponseEntity.status(201).body(createdSubject);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<SubjectDTO> updateSubject(@PathVariable Long id, @RequestBody SubjectDTO subjectDTO) {
        SubjectDTO updatedSubject = subjectService.updateSubject(id, subjectDTO);
        if (updatedSubject != null) {
            return ResponseEntity.ok(updatedSubject);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        boolean deleted = subjectService.deleteSubject(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{subjectId}/classes/{classId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> associateSubjectWithClass(@PathVariable Long subjectId, @PathVariable Long classId) {
        Map<String, Long> associationData = subjectService.associateSubjectWithClass(subjectId, classId);

        if (!associationData.isEmpty()) {
            return ResponseEntity.ok(associationData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{subjectId}/classes/{classId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> removeSubjectFromClass(@PathVariable Long subjectId, @PathVariable Long classId) {
        Map<String, Long> disassociationData = subjectService.removeSubjectFromClass(subjectId, classId);

        if (!disassociationData.isEmpty()) {
            return ResponseEntity.ok(disassociationData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

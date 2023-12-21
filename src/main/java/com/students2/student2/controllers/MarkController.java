package com.students2.student2.controllers;

import com.students2.student2.dtos.MarkDTO;
import com.students2.student2.entities.Mark;
import com.students2.student2.services.mark.MarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/marks")
public class MarkController {

    private final MarkService markService;

    public MarkController(MarkService markService) {
        this.markService = markService;
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<MarkDTO>> getAllMarksForStudent(@PathVariable("studentId") Long studentId) {
        List<MarkDTO> marks = markService.getAllMarksForStudent(studentId);

        List<MarkDTO> markDTOs = new ArrayList<>();
        for (MarkDTO mark : marks) {
            MarkDTO markDTO = new MarkDTO(mark.getId(), mark.getValue(), mark.getSubjectId(), mark.getStudentId(),mark.getDate());
            markDTOs.add(markDTO);
        }

        return ResponseEntity.ok(markDTOs);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<MarkDTO> getMarkById(@PathVariable Long id) {
        MarkDTO mark = markService.getMarkById(id);
        if (mark != null) {
            return ResponseEntity.ok(mark);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<MarkDTO> updateMark(@PathVariable Long id, @RequestBody MarkDTO markDTO) {
        MarkDTO updatedMark = markService.updateMark(id, markDTO);
        if (updatedMark != null) {
            return ResponseEntity.ok(updatedMark);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMark(@PathVariable Long id) {
        boolean deleted = markService.deleteMark(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/setmark")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> setMark(@RequestBody Map<String, Object> markData) {
        Long studentId = Long.valueOf(markData.get("studentId").toString());
        Long subjectId = Long.valueOf(markData.get("subjectId").toString());
        int markValue = Integer.parseInt(markData.get("markValue").toString());

        Mark mark = markService.setMark(studentId, subjectId, markValue);

        List<Map<String, Object>> responseDataList = new ArrayList<>();

        if (mark != null) {
            Map<String, Object> markDataMap = new HashMap<>();
            markDataMap.put("studentId", mark.getStudent().getId());
            markDataMap.put("subjectId", mark.getSubject().getId());
            markDataMap.put("value", mark.getValue());

            responseDataList.add(markDataMap);

            return ResponseEntity.ok(responseDataList);
        } else {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to set the mark. Check the subject and class relationship or student/subject doesn't exist.");
            responseDataList.add(error);

            return ResponseEntity.badRequest().body(responseDataList);
        }
    }
}

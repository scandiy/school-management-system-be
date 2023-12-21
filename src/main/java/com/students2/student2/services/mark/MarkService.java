package com.students2.student2.services.mark;

import com.students2.student2.dtos.MarkDTO;
import com.students2.student2.entities.Mark;

import java.util.List;
public interface MarkService {
    MarkDTO getMarkById(Long id);
    MarkDTO updateMark(Long id, MarkDTO markDTO);
    boolean deleteMark(Long id);
    Mark setMark(Long studentId, Long subjectId, int markValue);
    List<MarkDTO> getAllMarksForStudent(Long studentId);
}

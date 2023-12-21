package com.students2.student2.services.schoolclass;

import com.students2.student2.dtos.ClassDTO;
import java.util.List;

public interface ClassService {
    List<ClassDTO> getAllClasses();
    ClassDTO getClassById(Long id);
    ClassDTO createClass(ClassDTO classDTO);
    ClassDTO updateClass(Long id, ClassDTO classDTO);
    boolean deleteClass(Long id);

}

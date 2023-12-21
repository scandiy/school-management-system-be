package com.students2.student2.repositories;

import com.students2.student2.entities.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {
    List<Mark> findByStudentId(Long id);
    List<Mark> findAllByStudentId(Long studentId);
}

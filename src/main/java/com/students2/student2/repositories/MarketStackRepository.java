package com.students2.student2.repositories;

import com.students2.student2.entities.EndOfDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketStackRepository extends JpaRepository<EndOfDay, Long> {
}

package com.teste.controledeponto.repository;

import com.teste.controledeponto.model.ClockIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClockInRepository extends JpaRepository<ClockIn, Long> {
}

package com.teste.controledeponto.repository;

import com.teste.controledeponto.model.ClockIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ClockInRepository extends JpaRepository<ClockIn, Long> {

    @Query(value = "select * from pnt_clockin c " +
        "where c.user_id = :userId and DATE(c.clock_in) = :date ", nativeQuery = true)
    List<ClockIn> findByUserIdAndDate(Long userId, LocalDate date);
}

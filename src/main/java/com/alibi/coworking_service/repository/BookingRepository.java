package com.alibi.coworking_service.repository;

import com.alibi.coworking_service.domain.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Booking b WHERE b.resourceId = :resourceId " +
            "AND ((b.startTime < :endTime AND b.endTime > :startTime) " +
            "OR (b.startTime < :endTime AND b.endTime > :startTime))")
    boolean isConflict(@Param("resourceId") Long resourceId,
                       @Param("startTime") LocalDateTime startTime,
                       @Param("endTime") LocalDateTime endTime);
}

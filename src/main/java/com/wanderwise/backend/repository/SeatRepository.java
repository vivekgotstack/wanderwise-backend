package com.wanderwise.backend.repository;

import com.wanderwise.backend.entity.Seat;
import com.wanderwise.backend.entity.SeatStatus;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByFlightIdAndStatus(Long flightId, SeatStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id IN :ids")
    List<Seat> findByIdInForUpdate(@Param("ids") List<Long> ids);

    @Query("SELECT s FROM Seat s WHERE s.status = 'LOCKED' AND s.lockedAt < :expiryTime")
    List<Seat> findExpiredLockedSeats(LocalDateTime expiryTime);

    @Modifying
    @Query("DELETE FROM Seat s WHERE DATE(s.flight.departureTime) = :date")
    void deleteByFlightDepartureDate(LocalDate date);

    List<Seat> findByFlightId(Long flightId);

    long countByFlightIdAndStatus(Long flightId, SeatStatus status);

    @Modifying
    @Query(value = "TRUNCATE TABLE seats RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncateSeats();
}
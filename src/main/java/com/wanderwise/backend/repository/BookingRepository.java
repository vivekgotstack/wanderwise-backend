package com.wanderwise.backend.repository;

import com.wanderwise.backend.entity.Booking;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Booking b WHERE b.id = :id")
    Optional<Booking> findByIdForUpdate(@Param("id") Long id);

    @Query("SELECT b FROM Booking b WHERE b.id = :id AND b.userId = :userId")
    Optional<Booking> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query(value = "TRUNCATE TABLE booking RESTART IDENTITY CASCADE", nativeQuery = true)
    void truncateBookings();
}
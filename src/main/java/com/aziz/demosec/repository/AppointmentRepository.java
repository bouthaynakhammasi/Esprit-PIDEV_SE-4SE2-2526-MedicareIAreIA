package com.aziz.demosec.repository;

import com.aziz.demosec.Entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.status = 'UPCOMING' AND a.reminderSent = false AND a.availability.startTime BETWEEN :now AND :endTime")
    List<Appointment> findUpcomingAppointmentsForReminder(
            @Param("now") LocalDateTime now, 
            @Param("endTime") LocalDateTime endTime);
    @Query("""
      SELECT a FROM Appointment a
      JOIN FETCH a.patient p
      WHERE a.status IN :statuses
      AND a.startTime >= :now
      AND a.startTime <= :cutoff
      AND (a.reminderSent = false OR a.reminderSent IS NULL)
      ORDER BY a.startTime ASC
    """)
    List<Appointment> findUpcomingAppointmentsInWindow(
      @Param("statuses") List<com.aziz.demosec.Entities.AppointmentStatus> statuses,
      @Param("now") LocalDateTime now,
      @Param("cutoff") LocalDateTime cutoff);
}

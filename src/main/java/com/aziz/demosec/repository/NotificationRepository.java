package com.aziz.demosec.repository;

import com.aziz.demosec.Entities.DoctorNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<DoctorNotification, Long> {
    List<DoctorNotification> findByDoctorIdOrderByCreatedAtDesc(Long doctorId);
    List<DoctorNotification> findByDoctorIdAndReadFalseOrderByCreatedAtDesc(Long doctorId);
    long countByDoctorIdAndReadFalse(Long doctorId);
    boolean existsByAppointmentId(Long appointmentId);

    @Modifying
    @Query("UPDATE DoctorNotification n SET n.read = true WHERE n.doctorId = :doctorId")
    void markAllReadByDoctorId(@Param("doctorId") Long doctorId);
}

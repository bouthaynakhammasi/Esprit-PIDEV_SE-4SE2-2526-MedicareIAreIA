package com.aziz.demosec.service;

import com.aziz.demosec.Entities.DoctorNotification;
import com.aziz.demosec.dto.NotificationDTO;
import com.aziz.demosec.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public DoctorNotification createNotification(Long doctorId, Long appointmentId, String message) {
        if (notificationRepository.existsByAppointmentId(appointmentId)) {
            return null;
        }
        DoctorNotification notification = DoctorNotification.builder()
                .doctorId(doctorId)
                .appointmentId(appointmentId)
                .message(message)
                .read(false)
                .build();
        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsForDoctor(Long doctorId) {
        return notificationRepository.findByDoctorIdOrderByCreatedAtDesc(doctorId)
                .stream()
                .map(NotificationDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadForDoctor(Long doctorId) {
        return notificationRepository.findByDoctorIdAndReadFalseOrderByCreatedAtDesc(doctorId)
                .stream()
                .map(NotificationDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Long doctorId) {
        return notificationRepository.countByDoctorIdAndReadFalse(doctorId);
    }

    @Transactional
    public NotificationDTO markAsRead(Long notificationId) {
        DoctorNotification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        return NotificationDTO.from(notificationRepository.save(notification));
    }

    @Transactional
    public void markAllAsRead(Long doctorId) {
        notificationRepository.markAllReadByDoctorId(doctorId);
    }

    @Transactional(readOnly = true)
    public boolean alreadyNotified(Long appointmentId) {
        return notificationRepository.existsByAppointmentId(appointmentId);
    }
}

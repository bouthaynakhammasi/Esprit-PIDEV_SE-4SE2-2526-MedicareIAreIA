package com.aziz.demosec.dto;

import com.aziz.demosec.Entities.DoctorNotification;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDTO {
    private Long id;
    private Long doctorId;
    private Long appointmentId;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;

    public static NotificationDTO from(DoctorNotification n) {
        return NotificationDTO.builder()
                .id(n.getId())
                .doctorId(n.getDoctorId())
                .appointmentId(n.getAppointmentId())
                .message(n.getMessage())
                .read(n.isRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}

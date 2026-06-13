package com.aziz.demosec.Entities;

import com.aziz.demosec.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

 

    @OneToOne
    @JoinColumn(name = "availability_id", nullable = false, unique = true)
    private CalendarAvailability availability;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    private AvailabilityMode mode;
    private String meetingLink;
    private String visitAddress;
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Builder.Default
    @Column(name = "reminder_sent", nullable = false)
    private boolean reminderSent = false;

    public Long getDoctorId() {
        return provider != null ? provider.getId() : null;
    }

}
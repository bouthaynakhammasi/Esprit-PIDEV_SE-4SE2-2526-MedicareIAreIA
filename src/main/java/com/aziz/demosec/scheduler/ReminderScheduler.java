package com.aziz.demosec.scheduler;

import com.aziz.demosec.Entities.Appointment;
import com.aziz.demosec.Entities.AppointmentStatus;
import com.aziz.demosec.domain.User;
import com.aziz.demosec.repository.AppointmentRepository;
import com.aziz.demosec.repository.UserRepository;
import com.aziz.demosec.service.NotificationService;
import com.aziz.demosec.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReminderScheduler {

    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;
    private final SmsService smsService;
    private final UserRepository userRepository;

    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void scheduleReminders() {
        // Use UTC to match the database stored times
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Africa/Tunis"));
        LocalDateTime cutoff = now.plusMinutes(30);

        List<AppointmentStatus> statuses = List.of(
            AppointmentStatus.BOOKED,
            AppointmentStatus.CONFIRMED,
            AppointmentStatus.UPCOMING
        );

        List<Appointment> appointments =
            appointmentRepository.findUpcomingAppointmentsInWindow(statuses, now, cutoff);

        // --- DEBUG BLOCK ---
        try {
            log.info("--- DEBUG APPOINTMENTS IN DB ---");
            List<Appointment> allAppts = appointmentRepository.findAll();
            log.info("Total appointments in DB: {}", allAppts.size());
            for (Appointment a : allAppts) {
                log.info("ID: {}, Status: {}, reminderSent: {}, startTime: {}, availability: {}", 
                    a.getId(), a.getStatus(), a.isReminderSent(), a.getStartTime(), a.getAvailability() != null ? a.getAvailability().getId() : "NULL");
            }
            log.info("---------------------------------");
        } catch(Exception e) {
            log.error("Debug block error", e);
        }
        // --- END DEBUG BLOCK ---

        if (appointments.isEmpty()) {
            log.debug("[ReminderScheduler] No upcoming appointments in next 30 min.");
            return;
        }

        log.info("[ReminderScheduler] Found {} appointment(s). Processing...", appointments.size());

        int smsCount = 0, notifCount = 0, skippedCount = 0;
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        for (Appointment appt : appointments) {
            // Duplicate guard
            if (notificationService.alreadyNotified(appt.getId()) || appt.isReminderSent()) {
                log.debug("[ReminderScheduler] Already notified appt {}", appt.getId());
                skippedCount++;
                continue;
            }

            // Patient null check
            User patient = appt.getPatient();
            if (patient == null) {
                log.warn("[ReminderScheduler] Patient null for appt {}. Skipping.", appt.getId());
                continue;
            }

            String patientName = patient.getFullName() != null
                ? patient.getFullName() : "Patient";

            // Use appt.getStartTime() — NOT getAvailability()
            String date  = appt.getStartTime().format(dateFmt);
            String heure = appt.getStartTime().format(timeFmt);

            // Phone null check
            String phone = patient.getPhone();
            if (phone == null || phone.isBlank()) {
                log.warn("[ReminderScheduler] No phone for {}. Skipping SMS.", patientName);
                continue;
            }

            // Doctor name
            String doctorName = getDoctorName(appt.getDoctorId());

            // SMS in French
            String smsBody = String.format(
                "Bonjour %s, vous avez un rendez-vous médical le %s à %s avec Dr. %s. " +
                "Merci de vous présenter 10 minutes à l'avance. — MedicareAI",
                patientName, date, heure, doctorName
            );

            // Correct order: 1. Send SMS
            smsService.sendSms(phone, smsBody);

            // 2. Notify
            String notifMessage = "⏰ Upcoming: " + patientName + " at " + heure + " (~30 min)";
            notificationService.createNotification(appt.getDoctorId(), appt.getId(), notifMessage);
            notifCount++;

            // 3. Mark done last & persist
            appt.setReminderSent(true);
            appointmentRepository.save(appt);

            // Mask phone in log
            String masked = phone.length() > 4
                ? phone.substring(0, 4) + "****" + phone.substring(phone.length() - 2)
                : phone;
            log.info("[ReminderScheduler] SMS sent to {}", masked);
            smsCount++;
        }

        log.info("[ReminderScheduler] Done — SMS: {}, Notifications: {}, Skipped: {}",
            smsCount, notifCount, skippedCount);
    }

    private String getDoctorName(Long doctorId) {
        return userRepository.findById(doctorId)
            .map(User::getFullName)
            .orElse("votre médecin");
    }
}

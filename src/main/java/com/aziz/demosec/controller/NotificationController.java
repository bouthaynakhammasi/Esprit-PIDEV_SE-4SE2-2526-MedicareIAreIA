package com.aziz.demosec.controller;

import com.aziz.demosec.dto.NotificationDTO;
import com.aziz.demosec.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/doctors/{doctorId}/notifications")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("")
    public ResponseEntity<List<NotificationDTO>> getNotifications(@PathVariable Long doctorId) {
        return ResponseEntity.ok(notificationService.getNotificationsForDoctor(doctorId));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadForDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(notificationService.getUnreadForDoctor(doctorId));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable Long doctorId) {
        long count = notificationService.getUnreadCount(doctorId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long doctorId, @PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.markAsRead(notificationId));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long doctorId) {
        notificationService.markAllAsRead(doctorId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long doctorId, @PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAllNotifications(@PathVariable Long doctorId) {
        notificationService.deleteAllNotifications(doctorId);
        return ResponseEntity.noContent().build();
    }
}

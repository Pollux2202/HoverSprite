package com.example.demo.Notification;

import com.example.demo.Security.Filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @GetMapping("/all")
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/farmer")
    @PreAuthorize("hasRole('FARMER')")
    public List<Notification> getNotificationForEachFarmer(HttpServletRequest request){
        String farmerEmail = jwtAuthenticationFilter.extractEmailFromToken(request);

        return notificationService.getNotification(farmerEmail);
    }

    @GetMapping("/sprayer")
    @PreAuthorize("hasRole('SPRAYER')")
    public List<Notification> getNotificationForEachSprayer(HttpServletRequest request){
        String sprayerEmail = jwtAuthenticationFilter.extractEmailFromToken(request);

        return notificationService.getNotification(sprayerEmail);
    }

    public void notifyFarmer(String farmerEmail, String message) {
        messagingTemplate.convertAndSend("/topic/notifications/" + farmerEmail, message);
    }

    public void notifySprayers(List<String> sprayerEmails, String message) {
        for (String email : sprayerEmails) {
            messagingTemplate.convertAndSend("/topic/notifications/" + email, message);
        }
    }
}

package com.example.demo.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public void sendMockEmail(String to, String subject, String body) {
        Notification notification = new Notification();
        notification.setRecipient(to);
        notification.setSubject(subject);
        notification.setBody(body);
        notification.setSent(false); // Mark as "not sent" for now, since it's mock data

        notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<Notification> getNotification(String recipient) {
        return notificationRepository.findByRecipient(recipient);
    }

}

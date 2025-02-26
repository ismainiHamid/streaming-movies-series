package ma.streaming.upload.notification;

import ma.streaming.upload.notification.dto.NotificationResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void sendMovieProgressNotification(NotificationResponse notificationResponse) {
        String destination = "/topic/notify/progress/movies";
        this.simpMessagingTemplate.convertAndSend(destination, notificationResponse);
    }
}

package ma.streaming.upload.notification;

import ma.streaming.upload.notification.dto.NotificationResponse;

public interface NotificationService {
    void sendMovieProgressNotification(NotificationResponse notificationResponse);
}

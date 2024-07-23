package com.example.notification.repository;

import com.example.notification.entity.Notification;
import com.example.notification.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByUser(User user, Pageable pageable);
}

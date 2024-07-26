package com.example.notification.repository;

import com.example.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n from Notification n WHERE n.args.targetUserId=:userId ORDER BY n.id DESC")
    Page<Notification> findAllByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.notificationType like '%POST%' AND n.args.contentId=:postId")
    void deleteByPostId(@Param("postId") Integer postId);
}

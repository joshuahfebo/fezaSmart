package com.fezaschools.fezasmart.notification;

import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    Notification findFirstByRecipientUserId(Integer id);

}

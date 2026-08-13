package com.fezaschools.fezasmart.notification;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NotificationDTO {

    private Integer id;

    @NotNull
    private String title;

    private String message;

    private String type;

    private Boolean isRead;

    private OffsetDateTime createdAt;

    @NotNull
    private Integer recipientUser;

}

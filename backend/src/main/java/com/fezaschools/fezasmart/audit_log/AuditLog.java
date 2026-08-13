package com.fezaschools.fezasmart.audit_log;

import com.fezaschools.fezasmart.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class AuditLog {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String entity;

    @Column
    private Integer entityId;

    @Column(nullable = false)
    private String action;

    @Column(columnDefinition = "longtext")
    private String oldValue;

    @Column(columnDefinition = "longtext")
    private String newValue;

    @Column(length = 45)
    private String ipAddress;

    @Column
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}

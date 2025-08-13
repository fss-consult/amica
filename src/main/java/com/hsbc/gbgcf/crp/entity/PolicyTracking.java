package com.hsbc.gbgcf.crp.entity;

import com.hsbc.gbgcf.crp.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity representing policy tracking
 */
@Entity
@Table(name = "policy_tracking")
@Data
@NoArgsConstructor
public class PolicyTracking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "legal_entity_id")
    private LegalEntity legalEntity;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private Policy policy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "journey_type")
    private String journeyType;

    @Column(name = "form_data_content", columnDefinition = "TEXT")
    private String formDataContent;

    @Column(name = "retake")
    private String retake;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
}
package com.hsbc.gbgcf.crp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entity representing a policy
 */
@Entity
@Table(name = "policies")
@Data
@NoArgsConstructor
public class Policy implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_code")
    private String policyCode;

    @Column(name = "policy_name")
    private String policyName;

    @Column(name = "description")
    private String description;
}
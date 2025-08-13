package com.hsbc.gbgcf.crp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Entity representing a client (master group)
 */
@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "master_group_id", unique = true)
    private String masterGroupId;

    @Column(name = "client_name")
    private String clientName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "client_policies",
        joinColumns = @JoinColumn(name = "client_id"),
        inverseJoinColumns = @JoinColumn(name = "policy_id")
    )
    private List<Policy> policies;
}
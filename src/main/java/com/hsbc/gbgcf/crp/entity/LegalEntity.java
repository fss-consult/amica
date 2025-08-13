package com.hsbc.gbgcf.crp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Entity representing a legal entity
 */
@Entity
@Table(name = "legal_entities")
@Data
@NoArgsConstructor
public class LegalEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_id", unique = true)
    private String entityId;

    @Column(name = "entity_name")
    private String entityName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "legal_entity_policies",
        joinColumns = @JoinColumn(name = "legal_entity_id"),
        inverseJoinColumns = @JoinColumn(name = "policy_id")
    )
    private List<Policy> policies;
}
package com.hsbc.gbgcf.crp.repository;

import com.hsbc.gbgcf.crp.entity.LegalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for LegalEntity entities
 */
@Repository
public interface LegalEntitiesRepository extends JpaRepository<LegalEntity, Long> {
    
    /**
     * Find a legal entity by its entity ID
     * 
     * @param entityId the entity ID
     * @return an Optional containing the legal entity if found
     */
    Optional<LegalEntity> findByEntityId(String entityId);
}
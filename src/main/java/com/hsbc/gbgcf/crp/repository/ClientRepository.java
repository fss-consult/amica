package com.hsbc.gbgcf.crp.repository;

import com.hsbc.gbgcf.crp.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Client entities
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    /**
     * Find a client by its master group ID
     * 
     * @param masterGroupId the master group ID
     * @return an Optional containing the client if found
     */
    Optional<Client> findByMasterGroupId(String masterGroupId);
}
package com.hsbc.gbgcf.crp.repository;

import com.hsbc.gbgcf.crp.entity.PolicyTracking;
import com.hsbc.gbgcf.crp.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyTrackingRepository extends JpaRepository<PolicyTracking, Long> {

    /**
     * Find policy tracking by legal entity ID and multiple statuses
     * 
     * @param legalEntityId the legal entity ID
     * @param statuses the statuses to filter by
     * @return the policy tracking
     */
    @Query("SELECT pt FROM PolicyTracking pt WHERE pt.legalEntity.entityId = :legalEntityId AND pt.status IN (:statuses)")
    PolicyTracking findByLegalEntityIdAndMultipleStatuses(@Param("legalEntityId") String legalEntityId, @Param("statuses") Status... statuses);

    /**
     * Find policy tracking by client ID and multiple statuses
     * 
     * @param clientId the client ID
     * @param statuses the statuses to filter by
     * @return the policy tracking
     */
    @Query("SELECT pt FROM PolicyTracking pt WHERE pt.client.masterGroupId = :clientId AND pt.status IN (:statuses)")
    PolicyTracking findByClientIdAndMultipleStatuses(@Param("clientId") String clientId, @Param("statuses") Status... statuses);

    /**
     * Find policy tracking by legal entity ID and statuses
     * 
     * @param legalEntityId the legal entity ID
     * @param statuses the statuses to filter by
     * @return the policy tracking
     */
    @Query("SELECT pt FROM PolicyTracking pt WHERE pt.legalEntity.entityId = :legalEntityId AND pt.status IN (:statuses)")
    PolicyTracking findByLegalEntityIdAndStatuses(@Param("legalEntityId") String legalEntityId, @Param("statuses") Status... statuses);

    /**
     * Find policy tracking by client ID and statuses
     * 
     * @param clientId the client ID
     * @param statuses the statuses to filter by
     * @return the policy tracking
     */
    @Query("SELECT pt FROM PolicyTracking pt WHERE pt.client.masterGroupId = :clientId AND pt.status IN (:statuses)")
    PolicyTracking findByClientIdAndStatuses(@Param("clientId") String clientId, @Param("statuses") Status... statuses);

    /**
     * Find latest policy tracking by legal entity ID and statuses
     * 
     * @param legalEntityId the legal entity ID
     * @param statuses the statuses to filter by
     * @return list of policy tracking ordered by creation date
     */
    @Query("SELECT pt FROM PolicyTracking pt WHERE pt.legalEntity.entityId = :legalEntityId AND pt.status IN (:statuses) ORDER BY pt.createdDate DESC")
    List<PolicyTracking> findByLegalEntityIdAndStatusesLatest(@Param("legalEntityId") String legalEntityId, @Param("statuses") Status... statuses);

    /**
     * Find latest policy tracking by client ID and statuses
     * 
     * @param clientId the client ID
     * @param statuses the statuses to filter by
     * @return list of policy tracking ordered by creation date
     */
    @Query("SELECT pt FROM PolicyTracking pt WHERE pt.client.masterGroupId = :clientId AND pt.status IN (:statuses) ORDER BY pt.createdDate DESC")
    List<PolicyTracking> findByClientIdAndStatusesLatest(@Param("clientId") String clientId, @Param("statuses") Status... statuses);

    /**
     * Find policy tracking by legal entity and journey type (latest)
     * 
     * @param legalEntityId the legal entity ID
     * @param journeyType the journey type
     * @return list of policy tracking
     */
    @Query("SELECT pt FROM PolicyTracking pt WHERE pt.legalEntity.entityId = :legalEntityId AND pt.journeyType = :journeyType ORDER BY pt.createdDate DESC")
    List<PolicyTracking> findByLEAndJourneyTypeLatest(@Param("legalEntityId") String legalEntityId, @Param("journeyType") String journeyType);

    /**
     * Find policy tracking by legal entity and journey type (previous)
     * 
     * @param legalEntityId the legal entity ID
     * @param journeyType the journey type
     * @return list of policy tracking
     */
    @Query("SELECT pt FROM PolicyTracking pt WHERE pt.legalEntity.entityId = :legalEntityId AND pt.journeyType = :journeyType AND pt.formDataContent IS NOT NULL ORDER BY pt.createdDate DESC")
    List<PolicyTracking> findByLEAndJourneyTypePrevious(@Param("legalEntityId") String legalEntityId, @Param("journeyType") String journeyType);

    /**
     * Find policy tracking by master group and journey type (latest)
     * 
     * @param masterGroupId the master group ID
     * @param journeyType the journey type
     * @return list of policy tracking
     */
    @Query("SELECT pt FROM PolicyTracking pt WHERE pt.client.masterGroupId = :masterGroupId AND pt.journeyType = :journeyType ORDER BY pt.createdDate DESC")
    List<PolicyTracking> findByMgAndJourneyTypeLatest(@Param("masterGroupId") String masterGroupId, @Param("journeyType") String journeyType);

    /**
     * Find policy tracking by master group and journey type (previous)
     * 
     * @param masterGroupId the master group ID
     * @param journeyType the journey type
     * @return list of policy tracking
     */
    @Query("SELECT pt FROM PolicyTracking pt WHERE pt.client.masterGroupId = :masterGroupId AND pt.journeyType = :journeyType AND pt.formDataContent IS NOT NULL ORDER BY pt.createdDate DESC")
    List<PolicyTracking> findByMgAndJourneyTypePrevious(@Param("masterGroupId") String masterGroupId, @Param("journeyType") String journeyType);
}
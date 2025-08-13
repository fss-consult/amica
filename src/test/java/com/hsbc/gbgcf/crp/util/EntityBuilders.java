package com.hsbc.gbgcf.crp.util;

import com.hsbc.gbgcf.crp.entity.Client;
import com.hsbc.gbgcf.crp.entity.LegalEntity;
import com.hsbc.gbgcf.crp.entity.Policy;
import com.hsbc.gbgcf.crp.entity.PolicyTracking;
import com.hsbc.gbgcf.crp.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Utility class for building test entities with a fluent API.
 */
public class EntityBuilders {

    // Client builders
    public static Client aClient() {
        return new Client();
    }

    public static Client aClientWithId(Long id) {
        Client client = aClient();
        client.setId(id);
        return client;
    }

    public static Client aClientWithMasterGroupId(String masterGroupId) {
        Client client = aClient();
        client.setMasterGroupId(masterGroupId);
        return client;
    }

    public static Client aClientWithName(String clientName) {
        Client client = aClient();
        client.setClientName(clientName);
        return client;
    }

    public static Client aClientWithPolicies(List<Policy> policies) {
        Client client = aClient();
        client.setPolicies(policies);
        return client;
    }

    public static Client anyClientThat(Consumer<Client> customizer) {
        Client client = aClient();
        customizer.accept(client);
        return client;
    }

    // LegalEntity builders
    public static LegalEntity aLegalEntity() {
        return new LegalEntity();
    }

    public static LegalEntity aLegalEntityWithId(Long id) {
        LegalEntity legalEntity = aLegalEntity();
        legalEntity.setId(id);
        return legalEntity;
    }

    public static LegalEntity aLegalEntityWithEntityId(String entityId) {
        LegalEntity legalEntity = aLegalEntity();
        legalEntity.setEntityId(entityId);
        return legalEntity;
    }

    public static LegalEntity aLegalEntityWithName(String entityName) {
        LegalEntity legalEntity = aLegalEntity();
        legalEntity.setEntityName(entityName);
        return legalEntity;
    }

    public static LegalEntity aLegalEntityWithPolicies(List<Policy> policies) {
        LegalEntity legalEntity = aLegalEntity();
        legalEntity.setPolicies(policies);
        return legalEntity;
    }

    public static LegalEntity anyLegalEntityThat(Consumer<LegalEntity> customizer) {
        LegalEntity legalEntity = aLegalEntity();
        customizer.accept(legalEntity);
        return legalEntity;
    }

    // Policy builders
    public static Policy aPolicy() {
        return new Policy();
    }

    public static Policy aPolicyWithId(Long id) {
        Policy policy = aPolicy();
        policy.setId(id);
        return policy;
    }

    public static Policy aPolicyWithCode(String policyCode) {
        Policy policy = aPolicy();
        policy.setPolicyCode(policyCode);
        return policy;
    }

    public static Policy aPolicyWithName(String policyName) {
        Policy policy = aPolicy();
        policy.setPolicyName(policyName);
        return policy;
    }

    public static Policy aPolicyWithDescription(String description) {
        Policy policy = aPolicy();
        policy.setDescription(description);
        return policy;
    }

    public static Policy anyPolicyThat(Consumer<Policy> customizer) {
        Policy policy = aPolicy();
        customizer.accept(policy);
        return policy;
    }

    // PolicyTracking builders
    public static PolicyTracking aPolicyTracking() {
        return new PolicyTracking();
    }

    public static PolicyTracking aPolicyTrackingWithId(Long id) {
        PolicyTracking policyTracking = aPolicyTracking();
        policyTracking.setId(id);
        return policyTracking;
    }

    public static PolicyTracking aPolicyTrackingWithStatus(Status status) {
        PolicyTracking policyTracking = aPolicyTracking();
        policyTracking.setStatus(status);
        return policyTracking;
    }

    public static PolicyTracking aPolicyTrackingWithLegalEntity(LegalEntity legalEntity) {
        PolicyTracking policyTracking = aPolicyTracking();
        policyTracking.setLegalEntity(legalEntity);
        return policyTracking;
    }

    public static PolicyTracking aPolicyTrackingWithClient(Client client) {
        PolicyTracking policyTracking = aPolicyTracking();
        policyTracking.setClient(client);
        return policyTracking;
    }

    public static PolicyTracking aPolicyTrackingWithPolicy(Policy policy) {
        PolicyTracking policyTracking = aPolicyTracking();
        policyTracking.setPolicy(policy);
        return policyTracking;
    }

    public static PolicyTracking aPolicyTrackingWithJourneyType(String journeyType) {
        PolicyTracking policyTracking = aPolicyTracking();
        policyTracking.setJourneyType(journeyType);
        return policyTracking;
    }

    public static PolicyTracking aPolicyTrackingWithFormDataContent(String formDataContent) {
        PolicyTracking policyTracking = aPolicyTracking();
        policyTracking.setFormDataContent(formDataContent);
        return policyTracking;
    }

    public static PolicyTracking aPolicyTrackingWithRetake(String retake) {
        PolicyTracking policyTracking = aPolicyTracking();
        policyTracking.setRetake(retake);
        return policyTracking;
    }

    public static PolicyTracking aPolicyTrackingWithCreatedDate(LocalDateTime createdDate) {
        PolicyTracking policyTracking = aPolicyTracking();
        policyTracking.setCreatedDate(createdDate);
        return policyTracking;
    }

    public static PolicyTracking anyPolicyTrackingThat(Consumer<PolicyTracking> customizer) {
        PolicyTracking policyTracking = aPolicyTracking();
        customizer.accept(policyTracking);
        return policyTracking;
    }

    // Helper methods
    public static List<Policy> somePolicies(int count) {
        List<Policy> policies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            policies.add(aPolicyWithCode("POLICY" + i));
        }
        return policies;
    }

    public static List<Policy> somePoliciesWith(Consumer<Policy> customizer, int count) {
        List<Policy> policies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Policy policy = aPolicy();
            customizer.accept(policy);
            policies.add(policy);
        }
        return policies;
    }
}
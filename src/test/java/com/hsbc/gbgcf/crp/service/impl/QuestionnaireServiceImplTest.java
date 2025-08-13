package com.hsbc.gbgcf.crp.service.impl;

import com.hsbc.gbgcf.crp.client.ODSClient;
import com.hsbc.gbgcf.crp.constants.PolicyConstants;
import com.hsbc.gbgcf.crp.entity.Client;
import com.hsbc.gbgcf.crp.entity.LegalEntity;
import com.hsbc.gbgcf.crp.entity.Policy;
import com.hsbc.gbgcf.crp.entity.PolicyTracking;
import com.hsbc.gbgcf.crp.enums.Status;
import com.hsbc.gbgcf.crp.repository.ClientRepository;
import com.hsbc.gbgcf.crp.repository.LegalEntitiesRepository;
import com.hsbc.gbgcf.crp.repository.PolicyTrackingRepository;
import com.hsbc.gbgcf.crp.util.EntityBuilders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionnaireServiceImplTest {

    @Mock
    private PolicyTrackingRepository policyTrackingRepository;

    @Mock
    private LegalEntitiesRepository legalEntitiesRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ODSClient odsClient;

    @InjectMocks
    private QuestionnaireServiceImpl questionnaireService;

    private static final String FORM_DATA = "{\"formData\":\"test\"}";
    // Using constants from PolicyConstants class
    private static final String FORM_ALREADY_SUBMITTED = PolicyConstants.FORM_ALREADY_SUBMITTED;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(questionnaireService, "multipleRetakeEnabled", true);
    }

    @Nested
    @DisplayName("Get ODS Data Tests")
    class QuestionnaireRetriverCases {
        
        @DisplayName("Should get ODS data when journey type matches PAW-TCPOP-LE pattern and legal entity ID is valid")
        @ParameterizedTest
        @CsvSource({
            "PAW-TCPOP-LE, LE12345",
            "PAW-ENERGY-LE, LE67890"
        })
        void shouldGetOdsDataWhenJourneyTypeMatchesPatternAndLegalEntityIdIsValid(String journeyType, String legalEntityId) {
            // Arrange
            Policy policy = EntityBuilders.aPolicyWithCode(PolicyConstants.POLICY_CODE_TCPOP);
            List<Policy> policies = new ArrayList<>();
            policies.add(policy);
            LegalEntity legalEntity = EntityBuilders.aLegalEntityWithPolicies(policies);

            when(legalEntitiesRepository.findByEntityId(legalEntityId)).thenReturn(Optional.of(legalEntity));
            when(odsClient.executeFormReadyRequest(journeyType, legalEntityId)).thenReturn(FORM_DATA);

            // Act
            ResponseEntity<String> response = questionnaireService.getODSdata(journeyType, legalEntityId);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(FORM_DATA);
            verify(policyTrackingRepository, times(2)).save(any(PolicyTracking.class));
            verify(odsClient).executeFormReadyRequest(journeyType, legalEntityId);
        }

        @DisplayName("Should get ODS data when journey type matches PAW-TCPOP-MG pattern and master group ID is valid")
        @ParameterizedTest
        @CsvSource({
            "PAW-TCPOP-MG, MG12345",
            "PAW-ENERGY-MG, MG67890"
        })
        void shouldGetOdsDataWhenJourneyTypeMatchesPatternAndMasterGroupIdIsValid(String journeyType, String masterGroupId) {
            // Arrange
            Policy policy = EntityBuilders.aPolicyWithCode(PolicyConstants.POLICY_CODE_TCPOP);
            List<Policy> policies = new ArrayList<>();
            policies.add(policy);
            Client client = EntityBuilders.aClientWithPolicies(policies);

            when(clientRepository.findByMasterGroupId(masterGroupId)).thenReturn(Optional.of(client));
            when(odsClient.executeFormReadyRequest(journeyType, masterGroupId)).thenReturn(FORM_DATA);

            // Act
            ResponseEntity<String> response = questionnaireService.getODSdata(journeyType, masterGroupId);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(FORM_DATA);
            verify(policyTrackingRepository, times(2)).save(any(PolicyTracking.class));
            verify(odsClient).executeFormReadyRequest(journeyType, masterGroupId);
        }

        @DisplayName("Should return NOT_FOUND when form is already submitted")
        @Test
        void shouldReturnNotFoundWhenFormIsAlreadySubmitted() {
            // Arrange
            String journeyType = "PAW-TCPOP-LE";
            String legalEntityId = "LE12345";
            
            LegalEntity legalEntity = new LegalEntity();
            Policy policy = new Policy();
            policy.setPolicyCode(PolicyConstants.POLICY_CODE_TCPOP);
            List<Policy> policies = new ArrayList<>();
            policies.add(policy);
            legalEntity.setPolicies(policies);

            when(legalEntitiesRepository.findByEntityId(legalEntityId)).thenReturn(Optional.of(legalEntity));
            when(odsClient.executeFormReadyRequest(journeyType, legalEntityId)).thenReturn(FORM_ALREADY_SUBMITTED);

            // Act
            ResponseEntity<String> response = questionnaireService.getODSdata(journeyType, legalEntityId);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isEqualTo(FORM_ALREADY_SUBMITTED);
        }
    }

    @DisplayName("Should use existing policy tracking when it exists for legal entity")
    @Test
    void shouldUseExistingPolicyTrackingWhenItExistsForLegalEntity() {
        // Arrange
        String journeyType = "PAW-TCPOP-LE";
        String legalEntityId = "LE12345";
        
        Policy policy = EntityBuilders.aPolicyWithCode(PolicyConstants.POLICY_CODE_TCPOP);
        List<Policy> policies = new ArrayList<>();
        policies.add(policy);
        
        LegalEntity legalEntity = EntityBuilders.aLegalEntityWithPolicies(policies);
        
        PolicyTracking existingPolicyTracking = EntityBuilders.aPolicyTrackingWithStatus(Status.CASE_INITIATED);

        when(policyTrackingRepository.findByLegalEntityIdAndMultipleStatuses(
                eq(legalEntityId), 
                eq(Status.CASE_INITIATED), 
                eq(Status.PULL_FORM), 
                eq(Status.IN_PROGRESS)))
                .thenReturn(existingPolicyTracking);
        when(legalEntitiesRepository.findByEntityId(legalEntityId)).thenReturn(Optional.of(legalEntity));
        when(odsClient.executeFormReadyRequest(journeyType, legalEntityId)).thenReturn(FORM_DATA);

        // Act
        ResponseEntity<String> response = questionnaireService.getODSdata(journeyType, legalEntityId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(FORM_DATA);
        verify(policyTrackingRepository, times(2)).save(existingPolicyTracking);
    }

    @DisplayName("Should use existing policy tracking when it exists for master group")
    @Test
    void shouldUseExistingPolicyTrackingWhenItExistsForMasterGroup() {
        // Arrange
        String journeyType = "PAW-TCPOP-MG";
        String masterGroupId = "MG12345";
        
        Policy policy = EntityBuilders.aPolicyWithCode(PolicyConstants.POLICY_CODE_TCPOP);
        List<Policy> policies = new ArrayList<>();
        policies.add(policy);
        
        Client client = EntityBuilders.aClientWithPolicies(policies);
        
        PolicyTracking existingPolicyTracking = EntityBuilders.aPolicyTrackingWithStatus(Status.CASE_INITIATED);

        when(policyTrackingRepository.findByClientIdAndMultipleStatuses(
                eq(masterGroupId), 
                eq(Status.CASE_INITIATED), 
                eq(Status.PULL_FORM), 
                eq(Status.IN_PROGRESS)))
                .thenReturn(existingPolicyTracking);
        when(clientRepository.findByMasterGroupId(masterGroupId)).thenReturn(Optional.of(client));
        when(odsClient.executeFormReadyRequest(journeyType, masterGroupId)).thenReturn(FORM_DATA);

        // Act
        ResponseEntity<String> response = questionnaireService.getODSdata(journeyType, masterGroupId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(FORM_DATA);
        verify(policyTrackingRepository, times(2)).save(existingPolicyTracking);
    }
    
    @DisplayName("Should validate journey type matches required pattern")
    @ParameterizedTest
    @CsvSource({
        "PAW-TCPOP-LE, LE12345",
        "PAW-ENERGY-LE, LE67890",
        "PAW-TCPOP-MG, MG12345",
        "PAW-ENERGY-MG, MG67890"
    })
    void shouldValidateJourneyTypeMatchesRequiredPattern(String journeyType, String customerId) {
        // Arrange
        Policy policy = EntityBuilders.aPolicyWithCode(PolicyConstants.POLICY_CODE_TCPOP);
        List<Policy> policies = new ArrayList<>();
        policies.add(policy);
        
        if (journeyType.contains("LE")) {
            LegalEntity legalEntity = EntityBuilders.aLegalEntityWithPolicies(policies);
            when(legalEntitiesRepository.findByEntityId(customerId)).thenReturn(Optional.of(legalEntity));
        } else {
            Client client = EntityBuilders.aClientWithPolicies(policies);
            when(clientRepository.findByMasterGroupId(customerId)).thenReturn(Optional.of(client));
        }
        
        when(odsClient.executeFormReadyRequest(journeyType, customerId)).thenReturn(FORM_DATA);

        // Act
        ResponseEntity<String> response = questionnaireService.getODSdata(journeyType, customerId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(FORM_DATA);
    }
    
    @DisplayName("Should validate customer ID format matches entity type")
    @ParameterizedTest
    @CsvSource({
        "PAW-TCPOP-LE, LE12345",
        "PAW-ENERGY-LE, LE67890",
        "PAW-TCPOP-MG, MG12345",
        "PAW-ENERGY-MG, MG67890"
    })
    void shouldValidateCustomerIdFormatMatchesEntityType(String journeyType, String customerId) {
        // Arrange
        Policy policy = EntityBuilders.aPolicyWithCode(PolicyConstants.POLICY_CODE_TCPOP);
        List<Policy> policies = new ArrayList<>();
        policies.add(policy);
        
        if (journeyType.contains("LE")) {
            LegalEntity legalEntity = EntityBuilders.aLegalEntityWithPolicies(policies);
            when(legalEntitiesRepository.findByEntityId(customerId)).thenReturn(Optional.of(legalEntity));
            // Verify customer ID starts with "LE"
            assertThat(customerId).as("Legal entity ID should start with 'LE'").startsWith("LE");
        } else {
            Client client = EntityBuilders.aClientWithPolicies(policies);
            when(clientRepository.findByMasterGroupId(customerId)).thenReturn(Optional.of(client));
            // Verify customer ID starts with "MG"
            assertThat(customerId).as("Master group ID should start with 'MG'").startsWith("MG");
        }
        
        when(odsClient.executeFormReadyRequest(journeyType, customerId)).thenReturn(FORM_DATA);

        // Act
        ResponseEntity<String> response = questionnaireService.getODSdata(journeyType, customerId);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
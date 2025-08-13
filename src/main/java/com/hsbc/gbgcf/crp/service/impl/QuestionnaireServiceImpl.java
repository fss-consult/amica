package com.hsbc.gbgcf.crp.service.impl;

import com.hsbc.gbgcf.crp.service.QuestionnaireService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class QuestionnaireServiceImpl implements QuestionnaireService {
    @Autowired
    private PolicyTrackingRepository policyTrackingRepository;

    @Autowired
    private LegalEntitiesRepository legalEntitiesRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DspTokenClient dspTokenClient;

    @Autowired
    private ODSClient odsClient;

    @Value("${multiple.retake.enable}")
    Boolean multipleRetakeEnabled;

    @Override
    public ResponseEntity<String> getODSdata(String journeyType, String customerIdentificationId) {
        PolicyTracking policyTracking = new PolicyTracking();
        List<String> journeyTypeParts = Arrays.asList(journeyType.split("-"));
        if (journeyTypeParts.contains("LE")) {
            policyTracking = policyTrackingRepository.findByLegalEntityIdAndMultipleStatuses(customerIdentificationId, Status.CASE_INITIATED, Status.PULL_FORM, Status.IN_PROGRESS);
            Optional<LegalEntity> legalEntity = legalEntitiesRepository.findByEntityId(customerIdentificationId);
            if (policyTracking == null) {
                policyTracking = new PolicyTracking();
                policyTracking.setStatus(Status.CASE_INITIATED);
                policyTracking.setLegalEntity(legalEntity.get());
                policyTracking.setPolicy(legalEntity.get().getPolicies().stream().filter(x -> x.getPolicyCode().equals("TCPOP")).findAny().get());
                policyTrackingRepository.save(policyTracking);
            } else {
                policyTracking.setPolicy(legalEntity.get().getPolicies().stream().filter(x -> x.getPolicyCode().equals("TCPOP")).findAny().get());
                policyTrackingRepository.save(policyTracking);
            }
        } else if (journeyTypeParts.contains("MG")) {
            policyTracking = policyTrackingRepository.findByClientIdAndMultipleStatuses(customerIdentificationId, Status.CASE_INITIATED, Status.PULL_FORM, Status.IN_PROGRESS);
            Optional<Client> masterGroup = clientRepository.findByMasterGroupId(customerIdentificationId);
            if (policyTracking == null) {
                policyTracking = new PolicyTracking();
                policyTracking.setStatus(Status.CASE_INITIATED);
                policyTracking.setClient(masterGroup.get());
                policyTracking.setPolicy(masterGroup.get().getPolicies().stream().filter(x -> x.getPolicyCode().equals("TCPOP")).findAny().get());
                policyTrackingRepository.save(policyTracking);
            } else {
                policyTracking.setPolicy(masterGroup.get().getPolicies().stream().filter(x -> x.getPolicyCode().equals("TCPOP")).findAny().get());
                policyTrackingRepository.save(policyTracking);
            }
        }

        String form = odsClient.executeFormReadyRequest(journeyType, customerIdentificationId);

        if (form != null && !form.equals("Form Already Submitted")) {
            policyTracking.setJourneyType(journeyType);
            policyTracking.setFormDataContent(form);
            policyTracking.setStatus(Status.PULL_FORM);
            policyTrackingRepository.save(policyTracking);
            return ResponseEntity.ok(form);
        } else {
            return new ResponseEntity<>("Form Already Submitted", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> saveOdsData(String journeyType, String customerIdentifier, String formData) {
        log.info("Entering saveOdsData with journeyType: {}, customerIdentifier: {}", journeyType, customerIdentifier);
        try {
            PolicyTracking policyTracking = new PolicyTracking();
            List<String> journeyTypeParts =
                    Arrays.asList(journeyType.split("-"));
            if (journeyTypeParts.contains("LE")) {
                log.info("fetching data for LE");
                policyTracking = policyTrackingRepository.findByLegalEntityIdAndStatuses(customerIdentifier, Status.PULL_FORM, Status.IN_PROGRESS);
                if (policyTracking != null) {
                    log.info("policy tracking is not null");
                }
            } else if (journeyTypeParts.contains("MG")) {
                log.info("fetching data for MG");
                policyTracking = policyTrackingRepository.findByClientIdAndStatuses(customerIdentifier, Status.PULL_FORM, Status.IN_PROGRESS);
                if (policyTracking != null) {
                    log.info("policy tracking is not null");
                }
            }
            String form = odsClient.executeSaveRequest(journeyType, customerIdentifier, formData);
            if (form != null) {
                if (policyTracking != null) {
                    log.info("form received from ODS");
                    policyTracking.setFormDataContent(form);
                    policyTracking.setStatus(Status.IN_PROGRESS);
                    policyTrackingRepository.save(policyTracking);
                }
                return ResponseEntity.ok(form);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Exception in saveOdsData: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<String> submitOdsData(String journeyType, String customerIdentifier, String formData) {
        log.info("Entering submitOdsData with journeyType: {}, customerIdentifier: {}", journeyType, customerIdentifier);
        try {
            PolicyTracking policyTracking = new PolicyTracking();
            List<String> journeyTypeParts = Arrays.asList(journeyType.split("-"));
            if (journeyTypeParts.contains("LE")) {
                log.info(" submit for LE");
                policyTracking = policyTrackingRepository.findByLegalEntityIdAndStatuses(customerIdentifier, Status.PULL_FORM, Status.IN_PROGRESS);
            } else if (journeyTypeParts.contains("MG")) {
                log.info(" submit for MG");
                policyTracking = policyTrackingRepository.findByClientIdAndStatuses(customerIdentifier, Status.PULL_FORM, Status.IN_PROGRESS);
            }
            policyTracking.setStatus(Status.SUBMITTED);
            policyTrackingRepository.save(policyTracking);
            String form = odsClient.executeSubmitRequest(journeyType, customerIdentifier, formData);
            if (form != null && !form.equals("Failed")) {
                policyTracking.setFormDataContent(form);
                if (multipleRetakeEnabled)
                    policyTracking.setRetake("enable");
                policyTrackingRepository.save(policyTracking);
                return ResponseEntity.ok("SUCCESS");
            } else {
                return new ResponseEntity<>("FAILED", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Exception in submit0dsData: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<String> viewForm(String journeyType, String customerIdentificationId) {
        log.info("in view Form - assessment");
        List<String> journeyTypeParts = Arrays.asList(journeyType.split("-"));
        List<PolicyTracking> policyTrackings = new ArrayList<>();
        if (journeyTypeParts.contains("LE")) {
            policyTrackings = policyTrackingRepository.findByLEAndJourneyTypeLatest(customerIdentificationId, journeyType);
            if (policyTrackings != null && !policyTrackings.isEmpty()) {
                if (policyTrackings.get(0).getFormDataContent() == null) {
                    policyTrackings = policyTrackingRepository.findByLEAndJourneyTypePrevious(customerIdentificationId, journeyType);
                }
                return ResponseEntity.ok(policyTrackings.get(0).getFormDataContent());
            }
        } else if (journeyTypeParts.contains("MG")) {
            log.info("feting form for MG " + journeyType);
            policyTrackings = policyTrackingRepository.findByMgAndJourneyTypeLatest(customerIdentificationId, journeyType);
            if (policyTrackings != null && !policyTrackings.isEmpty()) {
                log.info("policy tracking not null");
                if (policyTrackings.get(0).getFormDataContent() == null) {
                    log.info("latest form is null");
                    policyTrackings = policyTrackingRepository.findByMgAndJourneyTypePrevious(customerIdentificationId, journeyType);
                }
                log.info("policy tracking is" + policyTrackings.get(0));
                return ResponseEntity.ok(policyTrackings.get(0).getFormDataContent());
            }
        }
        return new ResponseEntity<>("Form Not available", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<String> getInitialForm(String journeyType, String customerIdentificationId) {
        log.info("Entering getInitialForm with journeyType: {}, customerIdentificationId: {}", journeyType, customerIdentificationId);
        try {
            log.info("Calling ODSClient to execute reset request");
            String response = odsClient.executeResetRequest(journeyType, customerIdentificationId);
            log.info("Reset request executed successfully for customerIdentificationId: {}", customerIdentificationId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Exception in getInitialForm for customerIdentificationId: {}, journeyType: {}. Error: {}",
                    customerIdentificationId, journeyType, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while resetting the form");
        }
    }

    @Override
    public ResponseEntity<String> retakeQuestionnaire(String journeyType, String customerIdentificationId) {
        log.info("Entering retakeQuestionnaire with journeyType: {}, customerIdentifier: {}", journeyType, customerIdentificationId);
        try {
            PolicyTracking policyTracking = new PolicyTracking();
            PolicyTracking policyTrackingNew = new PolicyTracking();
            List<String> journeyTypeParts = Arrays.asList(journeyType.split("-"));
            if (journeyTypeParts.contains("LE")) {
                log.info(" retake for LE");
                policyTracking = policyTrackingRepository.findByLegalEntityIdAndStatusesLatest(customerIdentificationId, Status.SUBMITTED, Status.DECISION_RECEIVED).get(0);
            } else if (journeyTypeParts.contains("MG")) {
                log.info(" retake for MG");
                policyTracking = policyTrackingRepository.findByClientIdAndStatusesLatest(customerIdentificationId, Status.SUBMITTED, Status.DECISION_RECEIVED).get(0);
            }
            policyTrackingNew.setJourneyType(journeyType);
            policyTrackingNew.setClient(policyTracking.getClient());
            policyTrackingNew.setLegalEntity(policyTracking.getLegalEntity());
            policyTrackingNew.setPolicy(policyTracking.getPolicy());
            policyTrackingNew.setStatus(Status.CASE_INITIATED);
            if (!multipleRetakeEnabled)
                policyTrackingNew.setRetake("disable");
            policyTrackingRepository.save(policyTrackingNew);
            String form = odsClient.executeRetakeRequest(journeyType, customerIdentificationId);
            if (form != null && !form.equals("Failed")) {
                return ResponseEntity.ok("Collect Data Initiated");
            } else {
                return new ResponseEntity<>("FAILED", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Exception in submitOdsData: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<String> getQuestionnaireError(String journeyType, String customerIdentificationId) {
        try {
            log.info("Executing getQuestionnaireError with journeyType: {}, customerIdentificationId: {}", journeyType, customerIdentificationId);
            String response = odsClient.executeQuestionnaireErrorRequest(journeyType, customerIdentificationId);
            log.info("Successfully executed getQuestionnaireError for customerIdentificationId: {}", customerIdentificationId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error occurred in getQuestionnaireError for journeyType: {}, customerIdentificationId: {}. Exception: {}",
                    journeyType, customerIdentificationId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while processing the request");
        }
    }
}
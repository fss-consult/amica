package com.hsbc.gbgcf.crp.controller;

import com.hsbc.gbgcf.crp.service.QuestionaireService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class QuestionnaireController {
    @Autowired
    private QuestionaireService questionnaireService;

    @GetMapping("/form-data/{journeyType}/{customerIdentificationId}")
    public ResponseEntity<String> getODSData(@PathVariable("journeyType") String journeyType, @PathVariable("customerIdentificationId") String customerIdentificationId) {
        log.info("in /form-data {}", journeyType, customerIdentificationId);
        return questionnaireService.getODSdata(journeyType, customerIdentificationId);
    }

    @PutMapping("/saveODSData")
    public ResponseEntity<String> saveODSData(@RequestParam("journeyType") String journeyType, @RequestParam("customerIdentificationId") String customerIdentificationId, @RequestBody String formData) {
        log.info("Entering /saveODSData with journeyType: {}, customerIdentificationId: {}", journeyType, customerIdentificationId);
        try {
            ResponseEntity<String> response = questionnaireService.saveOdsData(journeyType, customerIdentificationId, formData);
            log.info("Exiting saveODSData with response status: {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            log.error("Exception in saveODSData: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/submitODSData")
    public ResponseEntity<String> submitODSData(@RequestParam("journeyType") String journeyType, @RequestParam("customer IdentificationId") String customerIdentificationId, @RequestBody String formData) {
        log.info("Entering /submitODSData with journeyType: {}, customerIdentificationId: {}", journeyType, customerIdentificationId);
        try {
            ResponseEntity<String> response = questionnaireService.submitOdsData(journeyType, customerIdentificationId, formData);
            log.info("Exiting submitODSData with response status: {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            log.error("Exception in submitODSData: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/viewForm")
    public ResponseEntity<String> viewForm(@RequestParam("journeyType") String journeyType, @RequestParam("customerIdentificationId") String customerIdentificationId) {
        try {
            log.info("in /viewForm {}", journeyType, customerIdentificationId);
            ResponseEntity<String> response = questionnaireService.viewForm(journeyType, customerIdentificationId);
            log.info("Exiting viewForm with response status: {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            log.error("Exception in viewForm: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/reset")
    public ResponseEntity<String> getInitialForm(@RequestParam("journeyType") String journeyType, @RequestParam("customerIdentificationId") String customerIdentificationId) {
        try {
            log.info("in /reset {}", journeyType, customerIdentificationId);
            ResponseEntity<String> response = questionnaireService.getInitialForm(journeyType, customerIdentificationId);
            log.info("Exiting getInitialForm with response status: {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            log.error("Exception in getInitialForm: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/retake")
    public ResponseEntity<String> retakeQuestionnaire(@RequestParam("journeyType") String journeyType, @RequestParam("customerIdentificationId") String customerIdentificationId) {
        try {
            log.info("in /retake {}", journeyType, customerIdentificationId);
            ResponseEntity<String> response = questionnaireService.retakeQuestionnaire(journeyType, customerIdentificationId);
            log.info("/retake :: Exiting viewForm with response status: {}", response.getStatusCode());
            return response;
        } catch (Exception e) {
            log.error("Exception in viewForm: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/questionnaire-error/{journeyType}/{customerIdentificationId}")
    public ResponseEntity<String> getQuestionnaireError(@PathVariable("journeyType") String journeyType, @PathVariable("customerIdentificationId") String customerIdentificationId) {
        return questionnaireService.getQuestionnaireError(journeyType, customerIdentificationId);
    }
}

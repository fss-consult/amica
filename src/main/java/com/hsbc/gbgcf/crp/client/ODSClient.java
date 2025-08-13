package com.hsbc.gbgcf.crp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Client for interacting with the ODS (Operational Data Store) service
 */
@FeignClient(name = "ods-service", url = "${ods.service.url}")
public interface ODSClient {
    
    /**
     * Execute a form ready request to retrieve form data
     * 
     * @param journeyType the type of journey
     * @param customerIdentificationId the customer identification ID
     * @return the form data
     */
    @GetMapping("/form/ready/{journeyType}/{customerIdentificationId}")
    String executeFormReadyRequest(@PathVariable("journeyType") String journeyType, 
                                  @PathVariable("customerIdentificationId") String customerIdentificationId);
    
    /**
     * Execute a save request to save form data
     * 
     * @param journeyType the type of journey
     * @param customerIdentificationId the customer identification ID
     * @param formData the form data to save
     * @return the result of the save operation
     */
    @PostMapping("/form/save/{journeyType}/{customerIdentificationId}")
    String executeSaveRequest(@PathVariable("journeyType") String journeyType, 
                             @PathVariable("customerIdentificationId") String customerIdentificationId,
                             @RequestBody String formData);
    
    /**
     * Execute a submit request to submit form data
     * 
     * @param journeyType the type of journey
     * @param customerIdentificationId the customer identification ID
     * @param formData the form data to submit
     * @return the result of the submit operation
     */
    @PostMapping("/form/submit/{journeyType}/{customerIdentificationId}")
    String executeSubmitRequest(@PathVariable("journeyType") String journeyType, 
                               @PathVariable("customerIdentificationId") String customerIdentificationId,
                               @RequestBody String formData);
    
    /**
     * Execute a reset request to reset form data
     * 
     * @param journeyType the type of journey
     * @param customerIdentificationId the customer identification ID
     * @return the result of the reset operation
     */
    @GetMapping("/form/reset/{journeyType}/{customerIdentificationId}")
    String executeResetRequest(@PathVariable("journeyType") String journeyType, 
                              @PathVariable("customerIdentificationId") String customerIdentificationId);
    
    /**
     * Execute a retake request to retake a questionnaire
     * 
     * @param journeyType the type of journey
     * @param customerIdentificationId the customer identification ID
     * @return the result of the retake operation
     */
    @GetMapping("/form/retake/{journeyType}/{customerIdentificationId}")
    String executeRetakeRequest(@PathVariable("journeyType") String journeyType, 
                               @PathVariable("customerIdentificationId") String customerIdentificationId);
    
    /**
     * Execute a questionnaire error request to get error information
     * 
     * @param journeyType the type of journey
     * @param customerIdentificationId the customer identification ID
     * @return the error information
     */
    @GetMapping("/form/error/{journeyType}/{customerIdentificationId}")
    String executeQuestionnaireErrorRequest(@PathVariable("journeyType") String journeyType, 
                                           @PathVariable("customerIdentificationId") String customerIdentificationId);
}
package com.hsbc.gbgcf.crp.service;

import org.springframework.http.ResponseEntity;

public interface QuestionnaireService {
    
    /**
     * Retrieves ODS data for a specific journey type and customer identification
     * 
     * @param journeyType the type of journey
     * @param customerIdentificationId the customer identification ID
     * @return ResponseEntity containing the ODS data
     */
    ResponseEntity<String> getODSdata(String journeyType, String customerIdentificationId);
    
    /**
     * Saves ODS data for a specific journey type and customer identification
     * 
     * @param journeyType the type of journey
     * @param customerIdentificationId the customer identification ID
     * @param formData the form data to save
     * @return ResponseEntity containing the result of the save operation
     */
    ResponseEntity<String> saveOdsData(String journeyType, String customerIdentificationId, String formData);
    
    /**
     * Submits ODS data for a specific journey type and customer identification
     * 
     * @param journeyType the type of journey
     * @param customerIdentificationId the customer identification ID
     * @param formData the form data to submit
     * @return ResponseEntity containing the result of the submit operation
     */
    ResponseEntity<String> submitOdsData(String journeyType, String customerIdentificationId, String formData);
    
    /**
     * Retrieves a form for viewing
     * 
     * @param journeyType the type of journey
     * @param customerIdentificationId the customer identification ID
     * @return ResponseEntity containing the form data
     */
    ResponseEntity<String> viewForm(String journeyType, String customerIdentificationId);
    
    /**
     * Gets the initial form for a specific journey type and customer identification
     * 
     * @param journeyType the type of journey
     * @param customerIdentificationId the customer identification ID
     * @return ResponseEntity containing the initial form data
     */
    ResponseEntity<String> getInitialForm(String journeyType, String customerIdentificationId);
    
    /**
     * Allows a customer to retake a questionnaire
     * 
     * @param journeyType the type of journey
     * @param customerIdentificationId the customer identification ID
     * @return ResponseEntity containing the result of the retake operation
     */
    ResponseEntity<String> retakeQuestionnaire(String journeyType, String customerIdentificationId);
    
    /**
     * Retrieves questionnaire error information
     * 
     * @param journeyType the type of journey
     * @param customerIdentificationId the customer identification ID
     * @return ResponseEntity containing the error information
     */
    ResponseEntity<String> getQuestionnaireError(String journeyType, String customerIdentificationId);
}
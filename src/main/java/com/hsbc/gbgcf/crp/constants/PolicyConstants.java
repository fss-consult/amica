package com.hsbc.gbgcf.crp.constants;

/**
 * Constants class for policy codes and other repeatable literals used across the application.
 * This helps maintain consistency and makes it easier to update values in one place.
 */
public final class PolicyConstants {
    
    // Private constructor to prevent instantiation
    private PolicyConstants() {
        // Utility class should not be instantiated
    }
    
    // Policy Codes
    public static final String POLICY_CODE_TCPOP = "TCPOP";
    
    // Form Status Messages
    public static final String FORM_ALREADY_SUBMITTED = "Form Already Submitted";
    public static final String FORM_NOT_AVAILABLE = "Form Not available";
    public static final String COLLECT_DATA_INITIATED = "Collect Data Initiated";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    
    // Error Messages
    public static final String ERROR_RESETTING_FORM = "Error occurred while resetting the form";
    public static final String ERROR_PROCESSING_REQUEST = "Error occurred while processing the request";
    
    // Journey Type Parts
    public static final String JOURNEY_TYPE_LE = "LE";
    public static final String JOURNEY_TYPE_MG = "MG";
}
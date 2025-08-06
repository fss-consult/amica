package com.aisconsult.positions.model;

/**
 * Enum representing headers used in the application.
 * Each enum constant has a description that represents the actual header text.
 */
public enum Header {
    CASE_ID("Case_ID"),
    MASTERGROUP_ID("Mastergroup_ID"),
    MASTER_GROUP_NAME("Master_Group_Name"),
    GRID_CIN("GRID/CIN"),
    LEGAL_ENTITY_NAME("Legal_Entity_Name"),
    BOOKING_COUNTRY("Booking_Country"),
    INDUSTRY("Industry"),
    SUBSECTOR("SubSector"),
    MANAGED_REGION("Managed_Region"),
    MANAGED_COUNTRY("Managed_Country"),
    GRO_STAFF_ID("GRO_Staff_ID"),
    GRO_STAFF_NAME("GRO_Staff_Name"),
    POLICY_CODE("Policy_Code"),
    SCREENING_RESULT("Screening_Result"),
    REVIEW_DECISION("Review_Decision"),
    DECISION_RATIONALE("Decision_Rationale"),
    NACE("Nace"),
    FLAGGED_NACE("FLAGGED_NACE"),
    LINE_OF_BUSINESS("Line_of_Business"),
    EXTERNAL_VENDOR_DATA("External_Vendor_Data"),
    ASSET_RESOLUTION_FLAG("Asset_Resolution_Flag"),
    URGEWALD_FLAG("Urgewald_Flag"),
    GLOBAL_ENERGY_MONITOR_FLAG("Global_Energy_Monitor_Flag"),
    BLOOMBERG_FLAG("Bloomberg_Flag"),
    SP_FLAG("S&P_Flag"),
    TRASE_FLAG("Trase_Flag"),
    ZOOLOGICAL_SOCIETY_OF_LONDON_FLAG("Zoological Society_of_London_Flag"),
    WORLD_WIDE_FUND_FOR_NATURE_FLAG("World_Wide_Fund_for_Nature_Flag"),
    ROUNDTABLE_ON_SUSTAINABLE_PALM_OIL_FLAG("Roundtable_on_Sustainable_Palm_Oil_Flag"),
    ROUNDTABLE_ON_RESPONSIBLE_SOY_ASSOCIATION_FLAG("Roundtable_on_Responsible_Soy_Association_Flag"),
    PROGRAMME_FOR_THE_ENDORSEMENT_OF_FOREST_CERTIFICATION_FLAG("Programme_for_the_Endorsement_of_Forest_Certification_Flag"),
    FOREST_500_FLAG("Forest_500_Flag"),
    FOREST_STEWARDSHIP_COUNCIL_FLAG("Forest Stewardship_Council_Flag"),
    MAKER_ID("Maker_ID"),
    MAKER_NAME("Maker_Name"),
    MAKER_REVIEW_DATE("Maker_Review_Date"),
    CHECKER_ID("Checker_ID"),
    CHECKER_NAME("Checker_Name"),
    CHECKER_REVIEW_DATE("Checker_Review_Date"),
    SAU_SME_ID("SAU_SME_ID"),
    SAU_SME_NAME("SAU_SME_Name"),
    SAU_SME_REVIEW_DATE("SAU_SME_Review_Date"),
    WSBR_SME_ID("WSBR_SME_ID"),
    WSBR_SME_NAME("WSBR_SME_Name"),
    WSBR_REVIEW_DATE("WSBR_Review_Date"),
    RSR_SME_ID("RSR_SME_ID"),
    RSR_SME_NAME("RSR_SME_Name"),
    RSR_REVIEW_DATE("RSR_Review_Date"),
    REJECTED_BY_CHECKER("Rejected_by_Checker"),
    STATUS("Status"),
    LATEST_REVIEW_DATE("Latest_Review_Date"),
    LATEST_REVIEWER_ID("Latest_Reviewer_ID"),
    LATEST_REVIEWER_NAME("Latest_Reviewer_Name"),
    CREATED_DATE("Created_Date"),
    UNDRAWN_LIMIT("Undrawn Limit"),
    ACTUAL_LIMIT("Actual_Limit"),
    CHECKER_COMMENT("Checker_Comment"),
    HASE("HASE"),
    LINKED_BOOKING_COUNTRY("Linked_Booking_Country"),
    TRIGGER_TYPE("Trigger_Type"),
    REQUESTOR_STAFF_ID("Requestor_Staff_Id");

    private final String description;

    Header(String description) {
        this.description = description;
    }

    /**
     * Get the description (header text) of this enum constant.
     *
     * @return The header text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get all header descriptions as a String array.
     *
     * @return Array of all header descriptions
     */
    public static String[] getAllDescriptions() {
        Header[] headers = values();
        String[] descriptions = new String[headers.length];
        
        for (int i = 0; i < headers.length; i++) {
            descriptions[i] = headers[i].getDescription();
        }
        
        return descriptions;
    }
    
    /**
     * Get the ordinal number for LINKED_BOOKING_COUNTRY.
     * 
     * @return The ordinal number (position) of LINKED_BOOKING_COUNTRY in the enum
     */
    public static int getLinkedBookingCountryOrdinal() {
        return LINKED_BOOKING_COUNTRY.ordinal();
    }
}
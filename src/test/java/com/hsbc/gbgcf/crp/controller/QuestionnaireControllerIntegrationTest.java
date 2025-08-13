package com.hsbc.gbgcf.crp.controller;

import com.hsbc.gbgcf.crp.service.QuestionnaireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the QuestionnaireController.
 * These tests verify that the controller correctly interacts with the service layer
 * and properly handles HTTP requests and responses.
 */
@WebMvcTest(QuestionnaireController.class)
@Import(TestConfig.class)
public class QuestionnaireControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionnaireService questionnaireService;

    private static final String TEST_JOURNEY_TYPE = "testJourney";
    private static final String TEST_CUSTOMER_ID = "12345";
    private static final String TEST_FORM_DATA = "{\"key\":\"value\"}";
    private static final String TEST_RESPONSE = "{\"status\":\"success\"}";

    @BeforeEach
    public void setup() {
        // Default mock responses
        when(questionnaireService.getODSdata(anyString(), anyString()))
                .thenReturn(ResponseEntity.ok(TEST_RESPONSE));
        
        when(questionnaireService.saveOdsData(anyString(), anyString(), anyString()))
                .thenReturn(ResponseEntity.ok(TEST_RESPONSE));
        
        when(questionnaireService.submitOdsData(anyString(), anyString(), anyString()))
                .thenReturn(ResponseEntity.ok(TEST_RESPONSE));
        
        when(questionnaireService.viewForm(anyString(), anyString()))
                .thenReturn(ResponseEntity.ok(TEST_RESPONSE));
        
        when(questionnaireService.getInitialForm(anyString(), anyString()))
                .thenReturn(ResponseEntity.ok(TEST_RESPONSE));
        
        when(questionnaireService.retakeQuestionnaire(anyString(), anyString()))
                .thenReturn(ResponseEntity.ok(TEST_RESPONSE));
        
        when(questionnaireService.getQuestionnaireError(anyString(), anyString()))
                .thenReturn(ResponseEntity.ok(TEST_RESPONSE));
    }

    @Test
    public void shouldReturnODSDataWhenRequestIsValid() throws Exception {
        mockMvc.perform(get("/api/v1/form-data/{journeyType}/{customerIdentificationId}", 
                TEST_JOURNEY_TYPE, TEST_CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_RESPONSE));
    }

    @Test
    public void shouldSaveODSDataWhenRequestIsValid() throws Exception {
        mockMvc.perform(put("/api/v1/saveODSData")
                .param("journeyType", TEST_JOURNEY_TYPE)
                .param("customerIdentificationId", TEST_CUSTOMER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TEST_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_RESPONSE));
    }

    @Test
    public void shouldReturnErrorWhenSaveODSDataServiceThrowsException() throws Exception {
        // Mock service to throw exception
        when(questionnaireService.saveOdsData(eq(TEST_JOURNEY_TYPE), eq(TEST_CUSTOMER_ID), anyString()))
                .thenThrow(new RuntimeException("Test exception"));

        mockMvc.perform(put("/api/v1/saveODSData")
                .param("journeyType", TEST_JOURNEY_TYPE)
                .param("customerIdentificationId", TEST_CUSTOMER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TEST_FORM_DATA))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldSubmitODSDataWhenRequestIsValid() throws Exception {
        mockMvc.perform(post("/api/v1/submitODSData")
                .param("journeyType", TEST_JOURNEY_TYPE)
                .param("customer IdentificationId", TEST_CUSTOMER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TEST_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_RESPONSE));
    }

    @Test
    public void shouldReturnErrorWhenSubmitODSDataServiceThrowsException() throws Exception {
        // Mock service to throw exception
        when(questionnaireService.submitOdsData(eq(TEST_JOURNEY_TYPE), eq(TEST_CUSTOMER_ID), anyString()))
                .thenThrow(new RuntimeException("Test exception"));

        mockMvc.perform(post("/api/v1/submitODSData")
                .param("journeyType", TEST_JOURNEY_TYPE)
                .param("customer IdentificationId", TEST_CUSTOMER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TEST_FORM_DATA))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldReturnFormDataWhenViewFormRequestIsValid() throws Exception {
        mockMvc.perform(get("/api/v1/viewForm")
                .param("journeyType", TEST_JOURNEY_TYPE)
                .param("customerIdentificationId", TEST_CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_RESPONSE));
    }

    @Test
    public void shouldReturnErrorWhenViewFormServiceThrowsException() throws Exception {
        // Mock service to throw exception
        when(questionnaireService.viewForm(eq(TEST_JOURNEY_TYPE), eq(TEST_CUSTOMER_ID)))
                .thenThrow(new RuntimeException("Test exception"));

        mockMvc.perform(get("/api/v1/viewForm")
                .param("journeyType", TEST_JOURNEY_TYPE)
                .param("customerIdentificationId", TEST_CUSTOMER_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldReturnInitialFormWhenResetRequestIsValid() throws Exception {
        mockMvc.perform(get("/api/v1/reset")
                .param("journeyType", TEST_JOURNEY_TYPE)
                .param("customerIdentificationId", TEST_CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_RESPONSE));
    }

    @Test
    public void shouldReturnErrorWhenGetInitialFormServiceThrowsException() throws Exception {
        // Mock service to throw exception
        when(questionnaireService.getInitialForm(eq(TEST_JOURNEY_TYPE), eq(TEST_CUSTOMER_ID)))
                .thenThrow(new RuntimeException("Test exception"));

        mockMvc.perform(get("/api/v1/reset")
                .param("journeyType", TEST_JOURNEY_TYPE)
                .param("customerIdentificationId", TEST_CUSTOMER_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldRetakeQuestionnaireWhenRequestIsValid() throws Exception {
        mockMvc.perform(post("/api/v1/retake")
                .param("journeyType", TEST_JOURNEY_TYPE)
                .param("customerIdentificationId", TEST_CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_RESPONSE));
    }

    @Test
    public void shouldReturnErrorWhenRetakeQuestionnaireServiceThrowsException() throws Exception {
        // Mock service to throw exception
        when(questionnaireService.retakeQuestionnaire(eq(TEST_JOURNEY_TYPE), eq(TEST_CUSTOMER_ID)))
                .thenThrow(new RuntimeException("Test exception"));

        mockMvc.perform(post("/api/v1/retake")
                .param("journeyType", TEST_JOURNEY_TYPE)
                .param("customerIdentificationId", TEST_CUSTOMER_ID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldReturnQuestionnaireErrorWhenRequestIsValid() throws Exception {
        mockMvc.perform(get("/api/v1/questionnaire-error/{journeyType}/{customerIdentificationId}", 
                TEST_JOURNEY_TYPE, TEST_CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_RESPONSE));
    }

    @Test
    public void shouldReturnNonSuccessResponseWhenQuestionnaireErrorServiceReturnsError() throws Exception {
        // Mock service to return non-success response
        when(questionnaireService.getQuestionnaireError(eq(TEST_JOURNEY_TYPE), eq(TEST_CUSTOMER_ID)))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Not found\"}"));

        mockMvc.perform(get("/api/v1/questionnaire-error/{journeyType}/{customerIdentificationId}", 
                TEST_JOURNEY_TYPE, TEST_CUSTOMER_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":\"Not found\"}"));
    }
}
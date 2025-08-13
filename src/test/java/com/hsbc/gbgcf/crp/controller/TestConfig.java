package com.hsbc.gbgcf.crp.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * Test configuration for integration tests.
 * This class provides configuration beans needed for the integration tests.
 */
@TestConfiguration
public class TestConfig {

    /**
     * Creates a character encoding filter to ensure proper encoding in test responses.
     * 
     * @return CharacterEncodingFilter configured for UTF-8 encoding
     */
    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }
}
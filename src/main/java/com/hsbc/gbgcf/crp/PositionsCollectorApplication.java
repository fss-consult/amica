package com.hsbc.gbgcf.crp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for the Positions Collector application.
 * This class serves as the entry point for the Spring Boot application.
 */
@SpringBootApplication
public class PositionsCollectorApplication {

    /**
     * Main method that starts the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(PositionsCollectorApplication.class, args);
    }
}
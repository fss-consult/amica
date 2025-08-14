package com.hsbc.gbgcf.crp.util;

import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Integration tests for {@link ImageProcessingRunner}.
 * Tests the functionality to merge and extract Java code from actual pictures in the service impl directory.
 * Unlike the unit tests, this integration test uses the real {@link ImageProcessingUtil} to process actual images.
 */
class ImageProcessingRunnerIntegrationTest {

    private ImageProcessingUtil imageProcessingUtil;
    private ImageProcessingRunner imageProcessingRunner;

    private String imageDirectoryPath;
    private String targetPackage;
    private String className;
    private String expectedModulePath;
    private Path outputFilePath;

    private boolean tesseractAvailable = false;
    private Path tessdataPath;

    @BeforeEach
    void setUp() {
        // Create real instances (not mocks)
        imageProcessingUtil = new ImageProcessingUtil();
        imageProcessingRunner = new ImageProcessingRunner(imageProcessingUtil);

        // Set up the paths and parameters - same as in the actual runner
        imageDirectoryPath = Paths.get("src", "main", "java", "com", "hsbc", "gbgcf", "crp", 
                "service", "impl", "QuestionnaireServiceImpl").toAbsolutePath().toString();
        targetPackage = "com.hsbc.gbgcf.crp.service.impl.QuestionnaireServiceImpl";
        className = "ExtractedModule";
        expectedModulePath = Paths.get("src", "main", "java", "com", "hsbc", "gbgcf", "crp", 
                "service", "impl", "QuestionnaireServiceImpl", "ExtractedModule.java").toString();
        outputFilePath = Paths.get(expectedModulePath);
        
        // Check if tessdata directory exists and contains required files
        tessdataPath = Paths.get("tessdata");
        if (Files.exists(tessdataPath)) {
            Path engDataFile = tessdataPath.resolve("eng.traineddata");
            tesseractAvailable = Files.exists(engDataFile);
            if (!tesseractAvailable) {
                System.out.println("WARNING: Tesseract language data file 'eng.traineddata' not found in tessdata directory.");
                System.out.println("OCR functionality will not work correctly. Tests will be skipped.");
                System.out.println("Please download eng.traineddata from https://github.com/tesseract-ocr/tessdata");
                System.out.println("and place it in the tessdata directory.");
            }
        } else {
            try {
                // Create tessdata directory for future use
                Files.createDirectory(tessdataPath);
                System.out.println("Created tessdata directory at: " + tessdataPath.toAbsolutePath());
                System.out.println("Please download eng.traineddata from https://github.com/tesseract-ocr/tessdata");
                System.out.println("and place it in the tessdata directory.");
            } catch (IOException e) {
                System.err.println("Failed to create tessdata directory: " + e.getMessage());
            }
            tesseractAvailable = false;
            System.out.println("WARNING: tessdata directory not found. OCR functionality will not work correctly.");
            System.out.println("Tests will be skipped.");
        }
    }

    void tearDown() throws IOException {
        // Clean up any files created during the test
        if (Files.exists(outputFilePath)) {
            Files.delete(outputFilePath);
            System.out.println("Deleted test output file: " + outputFilePath);
        }
        
        // Clean up temp directory
        Path tempDir = Paths.get("temp");
        if (Files.exists(tempDir)) {
            Files.list(tempDir).forEach(file -> {
                try {
                    Files.delete(file);
                } catch (IOException e) {
                    System.err.println("Failed to delete temp file: " + file);
                }
            });
            Files.delete(tempDir);
            System.out.println("Deleted temp directory");
        }
    }

    @Test
    @DisplayName("Integration test: Should process actual images and create module")
    void shouldProcessActualImagesAndCreateModule() throws IOException, TesseractException {
        // Skip test if Tesseract OCR is not available
        assumeTrue(tesseractAvailable, "Skipping test because Tesseract OCR language data files are not available");
        
        // Act - run the actual image processing
        imageProcessingRunner.run();

        // Assert - verify the output file exists
        assertThat(Files.exists(outputFilePath))
                .as("Output file should exist at " + outputFilePath)
                .isTrue();

        // Read the content of the created file
        String fileContent = Files.readString(outputFilePath);
        
        // Verify the content has expected elements
        assertThat(fileContent)
                .as("File should contain package declaration")
                .contains("package " + targetPackage);
                
        // Verify the content has Java code elements
        // This is a basic check - the actual content will depend on what's in the images
        assertThat(fileContent)
                .as("File should contain Java code elements")
                .containsAnyOf("class", "interface", "enum");
    }

    @Test
    @DisplayName("Integration test: Should directly call processImagesAndCreateModule")
    void shouldDirectlyCallProcessImagesAndCreateModule() throws IOException, TesseractException {
        // Skip test if Tesseract OCR is not available
        assumeTrue(tesseractAvailable, "Skipping test because Tesseract OCR language data files are not available");
        
        // Act - directly call the method that processes images
        String modulePath = imageProcessingUtil.processImagesAndCreateModule(
                imageDirectoryPath, targetPackage, className);

        // Assert - verify the returned path is as expected
        assertThat(modulePath)
                .as("Returned module path should match expected path")
                .isEqualTo(expectedModulePath);

        // Verify the output file exists
        assertThat(Files.exists(outputFilePath))
                .as("Output file should exist at " + outputFilePath)
                .isTrue();

        // Read the content of the created file
        String fileContent = Files.readString(outputFilePath);
        
        // Verify the content has expected elements
        assertThat(fileContent)
                .as("File should contain package declaration")
                .contains("package " + targetPackage);
                
        // Verify the content has Java code elements
        assertThat(fileContent)
                .as("File should contain Java code elements")
                .containsAnyOf("class", "interface", "enum");
                
        // Verify the temp directory and merged image were created
        Path tempDir = Paths.get("temp");
        Path mergedImagePath = tempDir.resolve("merged_image.png");
        
        assertThat(Files.exists(tempDir))
                .as("Temp directory should exist")
                .isTrue();
                
        assertThat(Files.exists(mergedImagePath))
                .as("Merged image should exist")
                .isTrue();
    }
    
    @Test
    @DisplayName("Integration test: Should verify images exist in the source directory")
    void shouldVerifyImagesExistInSourceDirectory() throws IOException {
        // This test doesn't require Tesseract OCR, so it can always run
        
        // Verify that the source directory exists
        File directory = new File(imageDirectoryPath);
        assertThat(directory.exists())
                .as("Source directory should exist at " + imageDirectoryPath)
                .isTrue();
        
        // Verify that the directory contains PNG images
        File[] imageFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
        assertThat(imageFiles)
                .as("Source directory should contain PNG images")
                .isNotNull()
                .isNotEmpty();
        
        // Print information about the images
        System.out.println("Found " + imageFiles.length + " PNG images in " + imageDirectoryPath);
        for (File imageFile : imageFiles) {
            System.out.println("  - " + imageFile.getName() + " (" + imageFile.length() + " bytes)");
        }
    }
}
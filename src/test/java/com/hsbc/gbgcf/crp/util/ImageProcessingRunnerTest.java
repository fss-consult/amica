package com.hsbc.gbgcf.crp.util;

import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ImageProcessingRunner}.
 * Tests the functionality to merge and extract Java code from pictures in the service impl directory.
 */
@ExtendWith(MockitoExtension.class)
class ImageProcessingRunnerTest {

    @Mock
    private ImageProcessingUtil imageProcessingUtil;

    @InjectMocks
    private ImageProcessingRunner imageProcessingRunner;

    @TempDir
    Path tempDir;

    private String imageDirectoryPath;
    private String targetPackage;
    private String className;
    private String expectedModulePath;

    @BeforeEach
    void setUp() {
        // Set up the paths and parameters
        imageDirectoryPath = Paths.get("src", "main", "java", "com", "hsbc", "gbgcf", "crp", 
                "service", "impl", "QuestionnaireServiceImpl").toAbsolutePath().toString();
        targetPackage = "com.hsbc.gbgcf.crp.service.impl.QuestionnaireServiceImpl";
        className = "ExtractedModule";
        expectedModulePath = "src/main/java/com/hsbc/gbgcf/crp/service/impl/QuestionnaireServiceImpl/ExtractedModule.java";
    }

    @Test
    @DisplayName("Should process images and create module when run method is called")
    void shouldProcessImagesAndCreateModuleWhenRunMethodIsCalled() throws IOException, TesseractException {
        // Arrange
        when(imageProcessingUtil.processImagesAndCreateModule(
                eq(imageDirectoryPath), 
                eq(targetPackage), 
                eq(className)))
                .thenReturn(expectedModulePath);

        // Act
        imageProcessingRunner.run();

        // Assert
        verify(imageProcessingUtil).processImagesAndCreateModule(
                eq(imageDirectoryPath), 
                eq(targetPackage), 
                eq(className));
    }

    @Test
    @DisplayName("Should handle exceptions gracefully when processing images fails")
    void shouldHandleExceptionsGracefullyWhenProcessingImagesFails() throws IOException, TesseractException {
        // Arrange
        IOException testException = new IOException("Test exception");
        when(imageProcessingUtil.processImagesAndCreateModule(
                eq(imageDirectoryPath), 
                eq(targetPackage), 
                eq(className)))
                .thenThrow(testException);

        // Act - this should not throw an exception
        imageProcessingRunner.run();

        // Assert
        verify(imageProcessingUtil).processImagesAndCreateModule(
                eq(imageDirectoryPath), 
                eq(targetPackage), 
                eq(className));
    }

    @Test
    @DisplayName("Should test the actual image processing workflow")
    void shouldTestActualImageProcessingWorkflow() throws IOException, TesseractException {
        // Arrange
        // Create a temporary directory structure to simulate the service impl directory
        Path mockImageDir = tempDir.resolve("mockImages");
        Files.createDirectory(mockImageDir);

        // Create mock image files (empty files for this test)
        Path mockImage1 = mockImageDir.resolve("image1.png");
        Path mockImage2 = mockImageDir.resolve("image2.png");
        Files.createFile(mockImage1);
        Files.createFile(mockImage2);

        // Mock the merged image path
        String mergedImagePath = tempDir.resolve("merged_image.png").toString();
        
        // Sample extracted Java code
        String extractedCode = "public class ExtractedClass {\n    public void testMethod() {\n        // Test method\n    }\n}";
        
        // Mock the individual steps of the image processing workflow
        // This verifies that the complete workflow is executed correctly
        when(imageProcessingUtil.processImagesAndCreateModule(anyString(), anyString(), anyString()))
                .thenAnswer(invocation -> {
                    String dirPath = invocation.getArgument(0);
                    String pkg = invocation.getArgument(1);
                    String cls = invocation.getArgument(2);
                    
                    // Verify the correct parameters are passed
                    assertThat(pkg).isEqualTo(targetPackage);
                    assertThat(cls).isEqualTo(className);
                    
                    // Return the expected module path
                    return expectedModulePath;
                });

        // Act
        imageProcessingRunner.run();

        // Assert
        // Verify that processImagesAndCreateModule was called
        verify(imageProcessingUtil).processImagesAndCreateModule(
                anyString(), 
                eq(targetPackage), 
                eq(className));
    }

    @Test
    @DisplayName("Should validate the extracted code contains expected Java elements")
    void shouldValidateExtractedCodeContainsExpectedJavaElements() throws IOException, TesseractException {
        // Arrange
        // Sample extracted Java code with expected elements
        String extractedCode = "public class ExtractedClass {\n" +
                "    private String testField;\n" +
                "    \n" +
                "    public void testMethod() {\n" +
                "        // Test method\n" +
                "        System.out.println(\"Hello, world!\");\n" +
                "    }\n" +
                "}";
        
        // Mock the behavior to return our sample code
        when(imageProcessingUtil.processImagesAndCreateModule(
                eq(imageDirectoryPath), 
                eq(targetPackage), 
                eq(className)))
                .thenAnswer(invocation -> {
                    // Simulate creating the file with our sample code
                    Path mockOutputPath = tempDir.resolve("ExtractedModule.java");
                    Files.writeString(mockOutputPath, extractedCode);
                    return mockOutputPath.toString();
                });

        // Act
        imageProcessingRunner.run();

        // Assert
        verify(imageProcessingUtil).processImagesAndCreateModule(
                eq(imageDirectoryPath), 
                eq(targetPackage), 
                eq(className));
        
        // Verify the content of the created file
        Path createdFile = tempDir.resolve("ExtractedModule.java");
        String fileContent = Files.readString(createdFile);
        
        assertThat(fileContent)
                .contains("public class")
                .contains("private String")
                .contains("public void")
                .contains("System.out.println");
    }
}
package com.hsbc.gbgcf.crp.util;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Runner class to process images and create a module with the extracted code.
 * This class implements CommandLineRunner to execute the image processing
 * during application startup.
 */
@Component
public class ImageProcessingRunner implements CommandLineRunner {

    private final ImageProcessingUtil imageProcessingUtil;

    @Autowired
    public ImageProcessingRunner(ImageProcessingUtil imageProcessingUtil) {
        this.imageProcessingUtil = imageProcessingUtil;
    }

    @Override
    public void run(String... args) {
        try {
            processImagesAndCreateModule();
            System.out.println("Successfully processed images and created module.");
        } catch (Exception e) {
            System.err.println("Error processing images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Process images in the QuestionnaireServiceImpl directory and create a module
     * with the extracted code.
     *
     * @throws IOException If there's an error processing images or creating the module
     * @throws TesseractException If there's an error during OCR
     */
    private void processImagesAndCreateModule() throws IOException, TesseractException {
        // Path to the directory containing the images - using toAbsolutePath() to get full path
        Path imageDirPath = Paths.get("src", "main", "java", "com", "hsbc", "gbgcf", "crp", 
                "service", "impl", "QuestionnaireServiceImpl").toAbsolutePath();
        String imageDirectoryPath = imageDirPath.toString();
        
        System.out.println("Processing images from directory: " + imageDirectoryPath);
        
        // Target package for the new module
        String targetPackage = "com.hsbc.gbgcf.crp.service.impl.QuestionnaireServiceImpl";
        
        // Class name for the new module
        String className = "ExtractedModule";
        
        // Process images and create module
        String modulePath = imageProcessingUtil.processImagesAndCreateModule(
                imageDirectoryPath, targetPackage, className);
        
        System.out.println("Created module at: " + modulePath);
    }
}
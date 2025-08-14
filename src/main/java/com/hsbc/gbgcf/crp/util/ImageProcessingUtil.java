package com.hsbc.gbgcf.crp.util;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for processing images, merging them vertically by creation time,
 * and extracting Java code using OCR.
 * 
 * NOTE: This class requires Tesseract OCR to be installed and configured properly.
 * For OCR to work, you need to:
 * 1. Install Tesseract OCR on your system (https://github.com/tesseract-ocr/tesseract)
 * 2. Download language data files for English (eng.traineddata) from 
 *    https://github.com/tesseract-ocr/tessdata
 * 3. Place the language data files in the "tessdata" directory that will be created
 *    in the project root.
 */
@Component
public class ImageProcessingUtil {

    /**
     * Merges images from the specified directory vertically by creation time.
     * 
     * @param directoryPath Path to the directory containing images
     * @param outputPath Path where the merged image will be saved
     * @return The path to the merged image
     * @throws IOException If there's an error processing the images
     */
    public String mergeImagesVertically(String directoryPath, String outputPath) throws IOException {
        // Get all PNG files in the directory
        File directory = new File(directoryPath);
        File[] imageFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
        
        if (imageFiles == null || imageFiles.length == 0) {
            throw new IOException("No PNG images found in directory: " + directoryPath);
        }
        
        // Sort files by creation time
        List<File> sortedFiles = Arrays.stream(imageFiles)
                .sorted(Comparator.comparing(File::lastModified))
                .collect(Collectors.toList());
        
        // Load all images
        List<BufferedImage> images = sortedFiles.stream()
                .map(file -> {
                    try {
                        return ImageIO.read(file);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to read image: " + file.getName(), e);
                    }
                })
                .collect(Collectors.toList());
        
        // Calculate dimensions of the merged image
        int maxWidth = images.stream().mapToInt(BufferedImage::getWidth).max().orElse(0);
        int totalHeight = images.stream().mapToInt(BufferedImage::getHeight).sum();
        
        // Create a new image with the calculated dimensions
        BufferedImage mergedImage = new BufferedImage(maxWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mergedImage.createGraphics();
        
        // Draw each image vertically
        int currentY = 0;
        for (BufferedImage image : images) {
            g2d.drawImage(image, 0, currentY, null);
            currentY += image.getHeight();
        }
        g2d.dispose();
        
        // Save the merged image
        File outputFile = new File(outputPath);
        ImageIO.write(mergedImage, "png", outputFile);
        
        return outputPath;
    }
    
    /**
     * Extracts text from an image using OCR.
     * 
     * @param imagePath Path to the image
     * @return Extracted text
     * @throws TesseractException If there's an error during OCR
     */
    public String extractTextFromImage(String imagePath) throws TesseractException {
        Tesseract tesseract = new Tesseract();
        
        // Set the tessdata path - we'll use a relative path that will be created if it doesn't exist
        Path tessdataPath = Paths.get("tessdata");
        if (!Files.exists(tessdataPath)) {
            try {
                Files.createDirectory(tessdataPath);
                System.out.println("Created tessdata directory at: " + tessdataPath.toAbsolutePath());
                System.out.println("Please ensure you have Tesseract language data files in this directory.");
            } catch (IOException e) {
                System.err.println("Failed to create tessdata directory: " + e.getMessage());
            }
        }
        tesseract.setDatapath(tessdataPath.toAbsolutePath().toString());
        
        // Configure Tesseract for Java code
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(1); // Automatic page segmentation with OSD
        tesseract.setOcrEngineMode(1); // Neural net LSTM engine
        
        // Perform OCR
        File imageFile = new File(imagePath);
        return tesseract.doOCR(imageFile);
    }
    
    /**
     * Creates a Java file with the extracted code.
     * 
     * @param packageName Package where the module will be created
     * @param className Name of the class to create
     * @param code Java code to write to the file
     * @return Path to the created file
     * @throws IOException If there's an error creating the file
     */
    public String createJavaModule(String packageName, String className, String code) throws IOException {
        // Create package directory structure
        String packagePath = packageName.replace('.', File.separatorChar);
        Path directoryPath = Paths.get("src", "main", "java", packagePath);
        Files.createDirectories(directoryPath);
        
        // Create Java file
        Path filePath = directoryPath.resolve(className + ".java");
        
        // Format the code with proper package declaration
        String formattedCode = "package " + packageName + ";\n\n" + code;
        
        // Write code to file
        Files.writeString(filePath, formattedCode);
        
        return filePath.toString();
    }
    
    /**
     * Cleans and formats the extracted text to ensure it's valid Java code.
     * 
     * @param extractedText The raw text extracted from OCR
     * @return Cleaned and formatted Java code
     */
    public String cleanExtractedCode(String extractedText) {
        if (extractedText == null || extractedText.trim().isEmpty()) {
            return "// No code was extracted from the images\n\npublic class EmptyClass {\n    // This is a placeholder\n}";
        }
        
        // Remove common OCR errors and fix formatting issues
        String cleanedCode = extractedText
                // Replace common OCR errors
                .replace("publlc", "public")
                .replace("pubIic", "public")
                .replace("ciass", "class")
                .replace("cIass", "class")
                .replace("privale", "private")
                .replace("prolected", "protected")
                .replace("stalc", "static")
                .replace("volid", "void")
                .replace("relurn", "return")
                .replace("lf", "if")
                .replace("lnt", "int")
                .replace("booleam", "boolean")
                .replace("Strlng", "String")
                .replace("ArraylList", "ArrayList")
                .replace("sif4j", "slf4j")
                .replace("S1f4j", "Slf4j")
                // Fix common bracket issues
                .replace("[ ", "[")
                .replace(" ]", "]")
                .replace("{ ", "{")
                .replace(" }", "}")
                .replace("( ", "(")
                .replace(" )", ")");
        
        // Check if the code contains basic Java elements
        if (!cleanedCode.contains("class") && !cleanedCode.contains("interface") && !cleanedCode.contains("enum")) {
            cleanedCode = "public class ExtractedCode {\n    // Extracted content may not be valid Java code\n    \n    /**\n     * Raw extracted content:\n     */\n    private static final String EXTRACTED_CONTENT = \"\"\"\n        " 
                + cleanedCode.replace("\n", "\n        ") 
                + "\n    \"\"\";    \n}";
        }
        
        return cleanedCode;
    }
    
    /**
     * Process images in a directory, merge them, extract code, and create a module.
     * 
     * @param imageDirectoryPath Path to directory containing images
     * @param targetPackage Package where the module will be created
     * @param className Name of the class to create
     * @return Path to the created module
     * @throws IOException If there's an error processing images or creating the module
     * @throws TesseractException If there's an error during OCR
     */
    public String processImagesAndCreateModule(String imageDirectoryPath, String targetPackage, String className) 
            throws IOException, TesseractException {
        // Create temp directory for merged image if it doesn't exist
        Path tempDir = Paths.get("temp");
        if (!Files.exists(tempDir)) {
            Files.createDirectory(tempDir);
        }
        
        // Merge images
        String mergedImagePath = tempDir.resolve("merged_image.png").toString();
        mergeImagesVertically(imageDirectoryPath, mergedImagePath);
        
        System.out.println("Images merged successfully. Performing OCR...");
        
        // Extract code using OCR
        String rawExtractedText = extractTextFromImage(mergedImagePath);
        System.out.println("OCR completed. Cleaning and formatting extracted code...");
        
        // Clean and format the extracted code
        String cleanedCode = cleanExtractedCode(rawExtractedText);
        
        // Save the raw OCR output for reference
        Path rawTextPath = tempDir.resolve("raw_ocr_output.txt");
        Files.writeString(rawTextPath, rawExtractedText);
        System.out.println("Raw OCR output saved to: " + rawTextPath.toAbsolutePath());
        
        // Create module with cleaned code
        return createJavaModule(targetPackage, className, cleanedCode);
    }
}
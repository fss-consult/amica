# Image Processing and OCR Module

This project includes functionality to merge images horizontally by creation time, extract Java code using OCR, and create a new module with the extracted code.

## Prerequisites

Before running the application, ensure you have the following installed:

1. **Java 17** or higher
2. **Maven** for building the project
3. **Tesseract OCR** for text extraction:
   - Download and install Tesseract OCR from [GitHub](https://github.com/tesseract-ocr/tesseract)
   - Download language data files (specifically `eng.traineddata`) from [tessdata repository](https://github.com/tesseract-ocr/tessdata)
   - Place the language data files in a directory named `tessdata` in the project root

## How It Works

The application performs the following steps:

1. Finds all PNG images in the `src/main/java/com/hsbc/gbgcf/crp/service/impl/QuestionnaireServiceImpl` directory
2. Sorts the images by creation time
3. Merges the images horizontally (first image on the left, second on the right, etc.)
4. Uses Tesseract OCR to extract text from the merged image
5. Cleans and formats the extracted text to ensure it's valid Java code
6. Creates a new Java module named `ExtractedModule.java` in the `com.hsbc.gbgcf.crp.service.impl.QuestionnaireServiceImpl` package

## Building and Running

To build and run the application:

```bash
# Build the project
mvn clean package

# Run the application
java -jar target/positions-collector-0.0.1-SNAPSHOT.jar
```

## Troubleshooting

If you encounter OCR-related issues:

1. **Tesseract not found**: Ensure Tesseract is installed and in your system PATH
2. **Language data not found**: Make sure you have placed the `eng.traineddata` file in the `tessdata` directory
3. **Poor OCR results**: The application saves the raw OCR output to `temp/raw_ocr_output.txt` for inspection

## Implementation Details

The implementation consists of two main classes:

1. **ImageProcessingUtil**: Handles image merging, OCR, and module creation
2. **ImageProcessingRunner**: Executes the image processing during application startup

The raw OCR output is saved to `temp/raw_ocr_output.txt` for reference, and the merged image is saved to `temp/merged_image.png`.

## Notes

- The OCR process may not be perfect, especially for code with complex formatting
- The application includes logic to clean common OCR errors in Java code
- If the extracted text doesn't contain basic Java elements (class, interface, enum), it will be wrapped in a proper class structure
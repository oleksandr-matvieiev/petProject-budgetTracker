package com.example.petprojectbudgettracker.utils;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SimpleImageProcessor {
    private static void processImage(String imagePath, String outputPath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));


            BufferedImage greyImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

            Graphics g = greyImage.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();

            ImageIO.write(greyImage, "jpg", new File(outputPath));
            System.out.println("Image processed successfully");
        } catch (IOException e) {
            System.out.println("Image processing failed: " + e.getMessage());
        }
    }

    public static String getTotalAmountFromImage(String imagePath) {
        String outputPath = "C:\\Users\\Oleksandr\\Desktop\\petProject-budgetTracker\\src\\main\\resources\\templates\\" + System.currentTimeMillis() + "image.jpg";
        processImage(imagePath, outputPath);

        File imageFile = new File(outputPath);


        if (!imageFile.exists()) {
            throw new RuntimeException("Image file not found: " + outputPath);
        }

        Tesseract tesseract = new Tesseract();
        try {
            tesseract.setDatapath("C:\\Users\\Oleksandr\\AppData\\Local\\Programs\\Tesseract-OCR\\tessdata");
            tesseract.setLanguage("deu");

            String result = tesseract.doOCR(imageFile);


            Pattern pattern = Pattern.compile("(?i)(?:Total|ToraL)\\s*CHF\\s*([\\d.,]+)");
            Matcher matcher = pattern.matcher(result);
            if (matcher.find()) {
                Files.deleteIfExists(Path.of(outputPath));
                return matcher.group(1).replace(",", ".");
            }
        } catch (TesseractException | IOException e) {
            throw new RuntimeException("OCR processing failed: " + e.getMessage());
        }

        return null;

    }


    public static String saveImage(MultipartFile image) throws IOException {
        if (!Objects.requireNonNull(image.getContentType()).startsWith("image/")) {
            throw new RuntimeException("Invalid image type");
        }
        String folder = "src/main/resources/templates/";
        String filename = System.currentTimeMillis() + "_" + Objects.requireNonNull(image.getOriginalFilename());

        Path uploadPath = Paths.get(folder);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, image.getBytes());

        return filePath.toAbsolutePath().toString();
    }

}

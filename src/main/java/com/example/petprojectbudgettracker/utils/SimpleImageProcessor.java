package com.example.petprojectbudgettracker.utils;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SimpleImageProcessor {
    private void processImage(String imagePath, String outputPath) {
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

    public String getTotalAmountFromImage(String imagePath) {
        String outputPath = "src/main/resources/templates/new_image_" + System.currentTimeMillis() + ".jpg";
        processImage(imagePath, outputPath);
        File imageFile = new File(outputPath);

        Tesseract tesseract = new Tesseract();

        try {
            tesseract.setDatapath("C:\\Users\\Oleksandr\\AppData\\Local\\Programs\\Tesseract-OCR\\tessdata");
            tesseract.setLanguage("deu");

            String result = tesseract.doOCR(imageFile);

            Pattern pattern = Pattern.compile("(?i)(?:Total|ToraL)\\s*CHF\\s*([\\d.,]+)");
            Matcher matcher = pattern.matcher(result);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}

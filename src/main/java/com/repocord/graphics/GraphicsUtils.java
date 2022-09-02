package com.repocord.graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GraphicsUtils {
    public static void overlayImages(BufferedImage image1, BufferedImage image2, int x, int y) {
        Graphics2D g = image1.createGraphics();
        g.drawImage(image2, x, y, null);
        g.dispose();
    }

    public static void overlayImages(BufferedImage image1, BufferedImage image2, int x, int y, int sizeX, int sizeY) {
        Graphics2D g = image1.createGraphics();
        g.drawImage(image2, x, y, sizeX, sizeY, null);
        g.dispose();
    }

    public static BufferedImage readImage(String name) throws IOException {
        if (!name.startsWith("/")) throw new IOException("Resource's name should start with a /.");

        InputStream inputStream = GraphicsUtils.class.getResourceAsStream(name);
        if (inputStream == null) throw new IOException("No file found.");

        return ImageIO.read(inputStream);
    }

    public static void writeImage(BufferedImage image, String location) throws IOException {
        ImageIO.write(image, "PNG", new File(location));
    }

    public static void overlayText(BufferedImage image, String text, Color color, Font font, int x, int y) {
        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, x, y);
        g.dispose();
    }
}

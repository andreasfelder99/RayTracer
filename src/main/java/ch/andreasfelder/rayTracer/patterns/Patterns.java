package ch.andreasfelder.rayTracer.patterns;

import ch.andreasfelder.vector.Vector3;

import javax.swing.*;
import java.awt.*;
import java.awt.image.MemoryImageSource;

import static ch.andreasfelder.rayTracer.SphereColor.*;

public class Patterns extends JPanel {
    private Image generateTextureImage(int width, int height) {
        int[] pixels = new int[width * height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                // Convert to polar coordinates
                double centerX = width / 2.0;
                double centerY = height / 2.0;
                double dx = x - centerX;
                double dy = y - centerY;
                double distance = Math.sqrt(dx * dx + dy * dy);
                double angle = Math.atan2(dy, dx);

                double spiralEffect = Math.sin(5 * angle + distance / 10.0);


                double red =  Math.sin(spiralEffect);
                double green = Math.sin(spiralEffect);
                double blue = Math.sin(spiralEffect);

                Vector3 colorVector = new Vector3(red, green, blue);
                Vector3 correctedColor = gammaCorrectOutput(colorVector);
                int intColor = tosRGB(correctedColor);

                pixels[x + y * width] = intColor;
            }
        }

        MemoryImageSource imageSource = new MemoryImageSource(width, height, pixels, 0, width);
        return createImage(imageSource);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Rectangle r = g.getClipBounds();
        Image image = generateTextureImage(r.width, r.height);
        g.drawImage(image, 0, 0, this);
    }
}
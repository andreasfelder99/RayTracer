package ch.andreasfelder.week1;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class ColorInterpolation extends JPanel {
    private int width = 800;
    private int height = 600;
    private BufferedImage image;

    public ColorInterpolation() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        interpolateColors();
    }

    private void interpolateColors() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float t = (float) x / (width - 1);
                Color color = interpolateLinearRGB(new Color(1.0f, 0.0f, 0.0f), new Color(0.0f, 1.0f, 0.0f), t);
                image.setRGB(x, y, color.getRGB());
            }
        }
    }

    private Color interpolateLinearRGB(Color c1, Color c2, float t) {
        float r = (1 - t) * c1.getRed() / 255.0f + t * c2.getRed() / 255.0f;
        float g = (1 - t) * c1.getGreen() / 255.0f + t * c2.getGreen() / 255.0f;
        float b = (1 - t) * c1.getBlue() / 255.0f + t * c2.getBlue() / 255.0f;

        r = clipToRange(r);
        g = clipToRange(g);
        b = clipToRange(b);

        r = gammaCorrection(r);
        g = gammaCorrection(g);
        b = gammaCorrection(b);

        return new Color(r, g, b);
    }

    private float clipToRange(float value) {
        return Math.max(0.0f, Math.min(1.0f, value));
    }

    private float gammaCorrection(float value) {
        return (float) Math.pow(value, 1.0 / 2.2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Color Interpolation");
        ColorInterpolation panel = new ColorInterpolation();
        frame.add(panel);
        frame.setSize(panel.width, panel.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
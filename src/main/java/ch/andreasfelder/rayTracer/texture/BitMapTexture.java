package ch.andreasfelder.rayTracer.texture;

import ch.andreasfelder.rayTracer.SphereColor;
import ch.andreasfelder.vector.Vector2;
import ch.andreasfelder.vector.Vector3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BitMapTexture implements Texture{
    private final BufferedImage textureImage;
    private final PlanarProjection projection = new PlanarProjection();

    public BitMapTexture(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        textureImage = image;
    }

    @Override
    public Vector3 getTextureColor(Vector3 position) {
        Vector2 projectedPosition = projection.project(position);

        int width = textureImage.getWidth() - 1;
        int height = textureImage.getHeight() - 1;

        int x = (int) (projectedPosition.x() * width);
        int y = (int) (projectedPosition.y() * height);

        int rgb = textureImage.getRGB(x, y);
        float r = ((rgb >> 16) & 255) / 255.0f;
        float g = ((rgb >> 8) & 255) / 255.0f;
        float b = (rgb & 255) / 255.0f;

        Vector3 color = new Vector3(r, g, b);

        return color.pow(2.2f);
    }
}

package ch.andreasfelder.week2;

import ch.andreasfelder.vector.Vector2;
import ch.andreasfelder.vector.Vector3;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class simpleRayTracer extends JPanel {
    private int width = 600;
    private int height = 600;
    private BufferedImage image;

    private Scene scene;
    private final float epsilon = 1e-4f;

    public simpleRayTracer(Vector3 eye, Vector3 lookAt, float FOV) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        render(eye, lookAt, FOV);
    }

    private void render(Vector3 eye, Vector3 lookAt, float FOV) {
        scene = new Scene(new Sphere[]{
                new Sphere(new Vector3(-1001, 0, 0), 1000, Color.RED, Color.BLACK),
                new Sphere(new Vector3(1001, 0, 0), 1000, Color.BLUE, Color.BLACK),
                new Sphere(new Vector3(0, 0, 1001), 1000, Color.GRAY, Color.BLACK),
                new Sphere(new Vector3(0, -1001, 0), 1000, Color.GRAY, Color.BLACK),
                new Sphere(new Vector3(0, 1001, 0), 1000, Color.WHITE, Color.WHITE),
                new Sphere(new Vector3(-0.6, -0.7, 0.6), 0.3F, Color.YELLOW, Color.black),
                new Sphere(new Vector3(0.3, -0.4, 0.3), 0.6F, Color.CYAN, Color.black)
        });

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Ray ray = createEyeRay(eye, lookAt, FOV, new Vector2(x, y));
                Hitpoint hitPoint = findClosestHitpoint(scene, ray);
                Color color = computeColor(scene, hitPoint);
                image.setRGB(x, y, color.getRGB());
            }
        }
    }

    private Ray createEyeRay(Vector3 eye, Vector3 lookAt, float fov, Vector2 pixel) {
        Vector3 f = lookAt.subtract(eye);
        f = Vector3.normalize(f);
        Vector3 r = Vector3.cross(new Vector3(0, 1, 0), f);
        r = Vector3.normalize(r);
        Vector3 u = Vector3.cross(f, r);

        float px = (float) ((2 * (pixel.x() + 0.5f) / width - 1) *  Math.tan(fov / 2));
        float py = (float) ((1 - 2 * (pixel.y() + 0.5f) / height) *  Math.tan(fov / 2));

        Vector3 d = f.add(r.multiply((float) px)).add(u.multiply((float) py));
        return new Ray(eye, Vector3.normalize(d));
    }

    private Hitpoint findClosestHitpoint(Scene scene, Ray ray) {
        float minDistance = Float.POSITIVE_INFINITY;
        Sphere closestSphere = null;
        Vector3 hitPosition = null;

        for (Sphere sphere : scene.getSpheres()) {
            Vector3 o_c1 = ray.origin().subtract(sphere.getCenter());
            float a = 1;
            float b = 2 * Vector3.dot(ray.direction(), o_c1);
            float c = Vector3.dot(o_c1, o_c1) - sphere.getRadius() * sphere.getRadius();

            float discriminant = b * b - 4 * a * c;
            if (discriminant >= 0) {
                float t1 = (-b + (float) Math.sqrt(discriminant)) / (2 * a);
                float t2 = (-b - (float) Math.sqrt(discriminant)) / (2 * a);

                if (t1 > epsilon && t1 < minDistance) {
                    minDistance = t1;
                    closestSphere = sphere;
                    hitPosition = ray.origin().add(ray.direction().multiply(t1));
                }

                if (t2 > epsilon && t2 < minDistance) {
                    minDistance = t2;
                    closestSphere = sphere;
                    hitPosition = ray.origin().add(ray.direction().multiply(t2));
                }
            }
        }

        if (closestSphere == null) return null;
        return new Hitpoint(hitPosition, closestSphere);
    }

    private Color computeColor(Scene scene, Hitpoint hitPoint) {
        if (hitPoint == null) return Color.BLACK;
        return hitPoint.getSphere().getColor();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Ray Tracer");

        //simpleRayTracer panel = new simpleRayTracer(new Vector3(0, 0, -4), new Vector3(0, 0, 6), Math.toRadians(36));
        simpleRayTracer panel = new simpleRayTracer(new Vector3(-0.9, -0.5, 0.9), new Vector3(0, 0, 0), (float) Math.toRadians(110));


        frame.add(panel);
        frame.setSize(panel.width, panel.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
package ch.andreasfelder.week2;

import ch.andreasfelder.vector.Vector2;
import ch.andreasfelder.vector.Vector3;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class simpleRayTracer extends JPanel {
    private int width = 800;
    private int height = 600;
    private BufferedImage image;

    private Vector3[] eyeRay;
    private Vector3 hitPoint;

    private Scene scene;

    public simpleRayTracer(Vector3 eye, Vector3 lookAt, float FOV){
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        render(eye, lookAt, FOV);
    }

    private void render(Vector3 eye, Vector3 lookAt, float FOV) {
        scene = new Scene(new Sphere[]{
                new Sphere(new Vector3(-1001, 0, 0), 1000, java.awt.Color.RED),
                new Sphere(new Vector3(1001, 0, 0), 1000, Color.BLUE),
                new Sphere(new Vector3(0, 0, 1001), 1000, Color.GRAY),
                new Sphere(new Vector3(0, -1001, 0), 1000, Color.GRAY),
                new Sphere(new Vector3(0, 1001, 0), 1000, Color.WHITE),
                new Sphere(new Vector3(-0.6, -0.7, 0.6), 0.3F, Color.YELLOW),
                new Sphere(new Vector3(0.3, -0.4, 0.3), 0.6F, Color.CYAN)

        });

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                eyeRay = createEyeRay(eye, lookAt, FOV, new Vector2(x, y));
                hitPoint = findClosestHitpoint(scene, eyeRay);
                Color color = computeColor(scene, hitPoint);
                image.setRGB(x, y, color.getRGB());
            }
        }
    }

    private Vector3[] createEyeRay(Vector3 eye, Vector3 lookAt, float fov, Vector2 pixel) {
        Vector3[] eyeRay = new Vector3[2];

        Vector3 f = lookAt.subtract(eye);
        f = Vector3.normalize(f);

        Vector3 r = Vector3.cross(new Vector3(0, 1, 0), f);
        r = Vector3.normalize(r);

        Vector3 u = Vector3.cross(r, f);

        // Normalize pixel coordinates to range [-1, 1]
        float px = (2 * (pixel.x() + 0.5f) / width - 1) * (float) Math.tan(fov / 2) * width / height;
        float py = (1 - 2 * (pixel.y() + 0.5f) / height) * (float) Math.tan(fov / 2);

        Vector3 d = f.add(r.multiply(px)).add(u.multiply(py));

        eyeRay[0] = Vector3.normalize(eye);
        eyeRay[1] = Vector3.normalize(d);

        return eyeRay;
    }

    private Vector3 findClosestHitpoint(Scene scene, Vector3[] eyeRay){
        Vector3 hitPoint = new Vector3(0, 0, 0);
        float minDistance = Float.POSITIVE_INFINITY;

        for (Sphere sphere : scene.getSpheres()){
            Vector3 center = sphere.getCenter();
            float radius = sphere.getRadius();

            Vector3 o = eyeRay[0];
            Vector3 c_1 = eyeRay[1];

            Vector3 o_c1 = o.subtract(center);

            float a = 1;
            float b = 2 * Vector3.dot(c_1, o_c1);
            float c = Vector3.dot(o_c1, o_c1) - radius * radius;

            float discriminant = b * b - 4 * a * c;

            if (discriminant >= 0){
                float t1 = (-b + (float) Math.sqrt(discriminant)) / (2 * a);
                float t2 = (-b - (float) Math.sqrt(discriminant)) / (2 * a);

                if (t1 > 0 && t1 < minDistance){
                    minDistance = t1;
                    hitPoint = o.add(c_1.multiply(t1));
                }

                if (t2 > 0 && t2 < minDistance){
                    minDistance = t2;
                    hitPoint = o.add(c_1.multiply(t2));
                }
            }
        }

        return hitPoint;
    }

    private Color computeColor(Scene scene, Vector3 hitPoint) {
        for (Sphere sphere : scene.getSpheres()) {
            if (hitPoint != null && hitPoint.subtract(sphere.getCenter()).length() <= sphere.getRadius()) {
                return sphere.getColor();
            }
        }
        return Color.BLACK;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Ray Tracer");
        simpleRayTracer panel = new simpleRayTracer(new Vector3(0, 0, -4), new Vector3(0, 0, 3), 36);
        frame.add(panel);
        frame.setSize(panel.width, panel.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

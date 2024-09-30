package ch.andreasfelder.rayTracer;

import ch.andreasfelder.vector.Vector2;
import ch.andreasfelder.vector.Vector3;
import ch.andreasfelder.rayTracer.brdf.BrdfNormal;
import ch.andreasfelder.rayTracer.brdf.BrdfReflective;
import ch.andreasfelder.rayTracer.brdf.IBrdf;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class simpleRayTracer extends JPanel {
    private int width = 600;
    private int height = 600;
    private BufferedImage image;

    private Scene scene;
    private final float epsilon = 1e-4f;
    private final float p = 0.2f;
    private final float e_correction = (float) ((2 * Math.PI) / (1 - p));

    private final int interations = 500;

    public simpleRayTracer(Vector3 eye, Vector3 lookAt, float FOV) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        render(eye, lookAt, FOV);
    }

    private void render(Vector3 eye, Vector3 lookAt, float FOV) {
        final IBrdf brdfNormal = new BrdfNormal();
        final IBrdf brdfReflect = new BrdfReflective(SphereColor.WHITE, 2);
        scene = new Scene(new Sphere[]{
                new Sphere(new Vector3(-1001, 0, 0), 1000, SphereColor.RED, SphereColor.BLACK, brdfNormal),
                new Sphere(new Vector3(1001, 0, 0), 1000, SphereColor.BLUE, SphereColor.BLACK, brdfNormal),
                new Sphere(new Vector3(0, 0, 1001), 1000, SphereColor.GRAY, SphereColor.BLACK, brdfNormal),
                new Sphere(new Vector3(0, -1001, 0), 1000, SphereColor.GRAY, SphereColor.BLACK, brdfNormal),
                new Sphere(new Vector3(0, 1001, 0), 1000, SphereColor.WHITE, SphereColor.WHITE, brdfNormal),
                new Sphere(new Vector3(-0.6, -0.7, 0.6), 0.3F, SphereColor.YELLOW, SphereColor.BLACK, brdfNormal),
                new Sphere(new Vector3(0.3, -0.4, 0.3), 0.6F, SphereColor.CYAN, SphereColor.BLACK, brdfReflect)
        });

        Random random = new Random();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Ray ray = createEyeRay(eye, lookAt, FOV, new Vector2(x, y));

                Vector3 color = new Vector3(0, 0, 0);
                for (int i = 0; i < interations; i++) {
                    color = color.add(computeColor(scene, ray, random));
                }
                color = color.multiply(1.0f / interations);
                color = SphereColor.gammaCorrectToOutput(color);
                image.setRGB(x, y, SphereColor.tosRGB(color));
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

    private Vector3 computeColor(Scene scene, Ray ray, Random random) {
        Hitpoint hitPoint = findClosestHitpoint(scene, ray);
        if (hitPoint == null) return SphereColor.BLACK;

        Vector3 emission = hitPoint.getSphere().getEmission();
        if (random.nextFloat() < p)
            return emission;

        Vector3 n = hitPoint.getNormal();
        Vector3 omega_r = randomVector(n, random);
        Vector3 omega_r_hat = Vector3.normalize(omega_r);
        IBrdf brdf = hitPoint.getSphere().getBRDF();

        Vector3 color = hitPoint.getSphere().getColor();

        float factor = Vector3.dot(omega_r_hat, n) * e_correction;
        Vector3 adjustColor = brdf.calculate(color, Vector3.normalize(ray.direction()), n, omega_r);

        Vector3 computedColor = computeColor(scene, new Ray(hitPoint.getPosition(), omega_r), random);
        Vector3 adjustment = adjustColor.multiply(factor).multiply(computedColor);

        return emission.add(adjustment);
    }

    public Vector3 randomVector(Vector3 n, Random random) {
        while (true) {
            Vector3 r = new Vector3(
                    random.nextFloat() * 2 - 1,
                    random.nextFloat() * 2 - 1,
                    random.nextFloat() * 2 - 1);

            if (r.length() > 1)
                continue;

            r = Vector3.normalize(r);

            if (Vector3.dot(r, n) >= 0)
                return r;
            return r.multiply(-1);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Ray Tracer");

        simpleRayTracer panel = new simpleRayTracer(new Vector3(0, 0, -4), new Vector3(0, 0, 6), (float) Math.toRadians(36));
        //simpleRayTracer panel = new simpleRayTracer(new Vector3(-0.9, -0.5, 0.9), new Vector3(0, 0, 0), (float) Math.toRadians(110));


        frame.add(panel);
        frame.setSize(panel.width, panel.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
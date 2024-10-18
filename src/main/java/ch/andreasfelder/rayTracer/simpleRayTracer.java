package ch.andreasfelder.rayTracer;

import ch.andreasfelder.rayTracer.texture.Texture;
import ch.andreasfelder.vector.Vector2;
import ch.andreasfelder.vector.Vector3;
import ch.andreasfelder.rayTracer.brdf.IBrdf;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class simpleRayTracer extends JPanel {
    private int width = 600;
    private int height = 600;
    private BufferedImage image;

    private Scene scene;
    private final float epsilon = 1e-3f;
    private final float p = 0.1f;
    private final float e_correction = (float) ((2 * Math.PI) / (1 - p));

    private final int iterations = 10;

    private final float antiAliasingSetting = 0.25f;

    public simpleRayTracer(Vector3 eye, Vector3 lookAt, float FOV) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        scene = Scenes.getGalaxyScene();
        render(eye, lookAt, FOV);
    }

    private void render(Vector3 eye, Vector3 lookAt, float FOV) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new RenderTask(eye, lookAt, FOV, 0, width, 0, height));
    }

    private class RenderTask extends RecursiveTask<Void> {
        private static final int THRESHOLD = 50;
        private Vector3 eye, lookAt;
        private float FOV;
        private int startX, endX, startY, endY;

        public RenderTask(Vector3 eye, Vector3 lookAt, float FOV, int startX, int endX, int startY, int endY) {
            this.eye = eye;
            this.lookAt = lookAt;
            this.FOV = FOV;
            this.startX = startX;
            this.endX = endX;
            this.startY = startY;
            this.endY = endY;
        }

        @Override
        protected Void compute() {
            if ((endX - startX) * (endY - startY) <= THRESHOLD) {
                renderSection();
            } else {
                int midX = (startX + endX) / 2;
                int midY = (startY + endY) / 2;
                invokeAll(
                        new RenderTask(eye, lookAt, FOV, startX, midX, startY, midY),
                        new RenderTask(eye, lookAt, FOV, midX, endX, startY, midY),
                        new RenderTask(eye, lookAt, FOV, startX, midX, midY, endY),
                        new RenderTask(eye, lookAt, FOV, midX, endX, midY, endY)
                );
            }
            return null;
        }

        private void renderSection() {
            Random random = new Random();
            for (int x = startX; x < endX; x++) {
                for (int y = startY; y < endY; y++) {

                    float gaussX = (float) (random.nextGaussian(0, antiAliasingSetting) * (1.0 / 600));
                    float gaussY = (float) (random.nextGaussian(0, antiAliasingSetting) * (1.0 / 600));

                    Ray ray = createEyeRay(eye, lookAt, FOV, new Vector2(x + gaussX, y + gaussY));

                    Vector3 color = new Vector3(0, 0, 0);

                    for (int i = 0; i < iterations; i++) {
                        color = color.add(computeColor(scene, ray, random));
                    }

                    color = color.multiply(1.0f / iterations);
                    color = SphereColor.gammaCorrectOutput(color);
                    image.setRGB(x, y, SphereColor.tosRGB(color));
                }
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

        Vector3 d = f.add(r.multiply(px)).add(u.multiply(py));
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

        Texture texture = hitPoint.getSphere().getTexture();
        Vector3 localPosition = Vector3.normalize(hitPoint.getPosition().subtract(hitPoint.getSphere().getCenter()));
        Vector3 color = texture.getTextureColor(localPosition);

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

        //Textures / Patterns
        //Patterns panel = new Patterns();

        frame.add(panel);
        frame.setSize(panel.width, panel.height);
        //Patterns
        //frame.setSize(600,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
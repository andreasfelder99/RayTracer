package ch.andreasfelder.rayTracer;

import ch.andreasfelder.vector.Vector3;

public class SphereColor {
    public static final Vector3 BLACK = new Vector3(0.0, 0.0, 0.0);
    public static final Vector3 WHITE = new Vector3(1.0, 1.0, 1.0);
    public static final Vector3 RED = new Vector3(0.8, 0.0, 0.0);
    public static final Vector3 BLUE = new Vector3(0.0, 0.0, 0.8);
    public static final Vector3 GREEN = new Vector3(0.0, 0.8, 0.0);
    public static final Vector3 GRAY = new Vector3(0.35, 0.35, 0.35);
    public static final Vector3 YELLOW = new Vector3(0.8, 0.8, 0.0);
    public static final Vector3 CYAN = new Vector3(0.0, 0.8, 0.8);
    public static final Vector3 MAGENTA = new Vector3(0.9, 0, 0.9);

    public static Vector3 gammaCorrectOutput(Vector3 value) {
        return value.pow(1 / 2.2F);
    }

    public static int tosRGB(Vector3 color) {
        Vector3 clipped = new Vector3(clipTo1(color.x()), clipTo1(color.y()), clipTo1(color.z()));
        int red = (int) (clipped.x() * 255);
        int green = (int) (clipped.y() * 255);
        int blue = (int) (clipped.z() * 255);

        return (255 << 24) | (red << 16) | (green << 8) | blue;
    }

    public static float clipTo1(float value) {
        return Math.min(1, Math.max(0, value));
    }
}

package ch.andreasfelder.week2;

import ch.andreasfelder.vector.Vector3;

import java.awt.*;

public class Sphere {
    private final Vector3 center;
    private final float radius;
    private final Color color;
    private final Color emission;

    public Sphere(Vector3 center, float radius, Color color, Color emission) {
        this.center = center;
        this.radius = radius;
        this.color = color;
        this.emission = emission;
    }

    public Vector3 getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public Color getEmission() {
        return emission;
    }
}

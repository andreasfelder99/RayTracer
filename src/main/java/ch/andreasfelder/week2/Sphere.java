package ch.andreasfelder.week2;

import ch.andreasfelder.vector.Vector3;

public class Sphere {
    private final Vector3 center;
    private final float radius;
    private final Vector3 color;
    private final Vector3 emission;

    public Sphere(Vector3 center, float radius, Vector3 color, Vector3 emission) {
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

    public Vector3 getColor() {
        return color;
    }

    public Vector3 getEmission() {
        return emission;
    }
}

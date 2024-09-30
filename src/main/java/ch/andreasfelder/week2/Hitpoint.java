package ch.andreasfelder.week2;

import ch.andreasfelder.vector.Vector3;

public class Hitpoint {
    private Vector3 position;
    private Sphere sphere;
    private Vector3 normal;

    public Hitpoint(Vector3 position, Sphere sphere) {
        this.position = position;
        this.sphere = sphere;
        this.normal = computeNormal(position, sphere);
    }

    public Vector3 getPosition() {
        return position;
    }

    public Sphere getSphere() {
        return sphere;
    }

    public Vector3 getNormal() {
        return normal;
    }

    private Vector3 computeNormal(Vector3 hitPosition, Sphere sphere) {
        return Vector3.normalize(hitPosition.subtract(sphere.getCenter()));
    }
}

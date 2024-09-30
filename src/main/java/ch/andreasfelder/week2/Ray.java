package ch.andreasfelder.week2;

import ch.andreasfelder.vector.Vector3;

public class Ray {
    private Vector3 origin;
    private Vector3 direction;

    public Ray(Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = Vector3.normalize(direction);
    }

    public Vector3 origin() {
        return origin;
    }

    public Vector3 direction() {
        return direction;
    }

    public Vector3 pointAt(double t) {
        return origin.add(direction.multiply((float) t));
    }
}

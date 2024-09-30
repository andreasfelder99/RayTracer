package ch.andreasfelder.rayTracer;

public class Scene {
    private final Sphere[] spheres;

    public Scene(Sphere[] spheres) {
        this.spheres = spheres;
    }

    public Sphere[] getSpheres() {
        return spheres;
    }
}

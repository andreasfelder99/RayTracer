package ch.andreasfelder.week2;

public class Scene {
    private final Sphere[] spheres;

    public Scene(Sphere[] spheres) {
        this.spheres = spheres;
    }

    public Sphere[] getSpheres() {
        return spheres;
    }
}

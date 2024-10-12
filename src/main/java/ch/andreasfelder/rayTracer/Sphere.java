package ch.andreasfelder.rayTracer;

import ch.andreasfelder.rayTracer.texture.BitMapTexture;
import ch.andreasfelder.rayTracer.texture.Texture;
import ch.andreasfelder.vector.Vector3;
import ch.andreasfelder.rayTracer.brdf.IBrdf;

public class Sphere {
    private final Vector3 center;
    private final float radius;
    private final Vector3 emission;
    private final IBrdf BRDF;
    private final Texture texture;

    public Sphere(Vector3 center, float radius, Vector3 emission, IBrdf BRDF, Texture texture) {
        this.center = center;
        this.radius = radius;
        this.emission = emission;
        this.BRDF = BRDF;
        this.texture = texture;
    }

    public Vector3 getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }

    public Vector3 getEmission() {
        return emission;
    }

    public IBrdf getBRDF() {
        return BRDF;
    }

    public Texture getTexture() {
        return texture;
    }
}

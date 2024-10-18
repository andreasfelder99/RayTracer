package ch.andreasfelder.rayTracer.brdf;

import ch.andreasfelder.vector.Vector3;

public class BrdfReflective implements IBrdf{
    private final float one_over_pi = (float) (1.2 / (Math.PI));
    private final Vector3 specularColour;
    private final float multiplier;

    public BrdfReflective(Vector3 specularColour, float multiplier) {
        this.specularColour = specularColour;
        this.multiplier = multiplier;
    }
    public Vector3 calculate(Vector3 diffuseColour, Vector3 d, Vector3 n, Vector3 omega_r) {
        Vector3 d_r = Vector3.normalize(Vector3.reflect(d, n));

        if (Vector3.dot(omega_r, d_r) > 0.99) {
            return diffuseColour.multiply(one_over_pi).add(specularColour.multiply(multiplier));
        }
        return diffuseColour.multiply(one_over_pi);
    }
}

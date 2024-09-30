package ch.andreasfelder.week2.brdf;

import ch.andreasfelder.vector.Vector3;

public class BrdfNormal implements IBrdf {
    public Vector3 calculate(Vector3 diffuseColour, Vector3 d, Vector3 n, Vector3 omega_r) {
        return diffuseColour.multiply((float) (1.0 / Math.PI));
    }
}

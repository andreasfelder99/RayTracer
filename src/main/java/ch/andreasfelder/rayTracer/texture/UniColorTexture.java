package ch.andreasfelder.rayTracer.texture;

import ch.andreasfelder.vector.Vector3;

public class UniColorTexture implements Texture{
    private final Vector3 color;

    public UniColorTexture(Vector3 color) {
        this.color = color;
    }
    @Override
    public Vector3 getTextureColor(Vector3 pos) {
        return color;
    }
}

package ch.andreasfelder.rayTracer.texture;

import ch.andreasfelder.vector.Vector3;

public interface Texture {
    Vector3 getTextureColor(Vector3 pos);
}

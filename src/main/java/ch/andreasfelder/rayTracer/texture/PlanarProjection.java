package ch.andreasfelder.rayTracer.texture;

import ch.andreasfelder.vector.Vector2;
import ch.andreasfelder.vector.Vector3;

public class PlanarProjection {
    public Vector2 project(Vector3 pos) {
        return new Vector2((pos.x() + 1.0f) / 2.0f, (pos.y() * -1.0f + 1.0f) / 2.0f);
    }
}
